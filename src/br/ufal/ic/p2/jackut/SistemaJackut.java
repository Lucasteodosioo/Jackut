package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class SistemaJackut {
    private Map<String, Usuario> usuarios;
    private Map<String, String> sessoes;
    private static final String ARQUIVO_DADOS = "usuarios.dat";

    public SistemaJackut() {
        usuarios = new HashMap<>();
        sessoes = new HashMap<>();
        carregarDados();
    }

    // tenta carregar o mapa de usuários a partir do arquivo de dados, se falhar, inicia mapa vazio
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            usuarios = (Map<String, Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            usuarios = new HashMap<>();
        }
    }

    // persiste o mapa de usuários em disco no arquivo definido por ARQUIVO_DADOS.
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zerarSistema() {
        usuarios.clear();
    }

    public String abrirSessao(String login, String senha) throws Exception {
        if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
            throw new Exception("Login ou senha inválidos.");
        }

        if (!usuarios.containsKey(login)) {
            throw new Exception("Login ou senha inválidos.");
        }

        Usuario usuario = usuarios.get(login);
        if (!usuario.getSenha().equals(senha)) {
            throw new Exception("Login ou senha inválidos.");
        }

        String sessionId = Long.toString(System.currentTimeMillis()) + "-" + login;
        sessoes.put(sessionId, login);
        return sessionId;
    }

    // nnvia um pedido de amizade ou aceita um pedido existente
    public void adicionarAmigo(String id, String amigo) throws Exception {
        if (id == null || id.isEmpty() || !sessoes.containsKey(id)) {
            throw new Exception("Usuário não cadastrado.");
        }
        String remetente = sessoes.get(id);
        if (!usuarios.containsKey(remetente)) {
            throw new Exception("Usuário não cadastrado.");
        }
        if (!usuarios.containsKey(amigo)) {
            throw new Exception("Usuário não cadastrado.");
        }
        if (remetente.equals(amigo)) {
            throw new Exception("Usuário não pode adicionar a si mesmo como amigo.");
        }

        Usuario userRem = usuarios.get(remetente);
        Usuario userDest = usuarios.get(amigo);

        if (userRem.ehAmigo(amigo)) {
            throw new Exception("Usuário já está adicionado como amigo.");
        }

        if (userDest.temPedidoDe(remetente)) {
            throw new Exception("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        if (userRem.temPedidoDe(amigo)) {
            // remover pedido e adicionar como amigos mutuamente
            userRem.removerPedido(amigo);
            userRem.adicionarAmigo(amigo);
            userDest.adicionarAmigo(remetente);
            return;
        }

        userDest.receberPedido(remetente);
    }

    public boolean ehAmigo(String login, String amigo) throws Exception {
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usuário não cadastrado.");
        }
        Usuario u = usuarios.get(login);
        return u.ehAmigo(amigo);
    }

    public String getAmigos(String login) throws Exception {
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usuário não cadastrado.");
        }
        Usuario u = usuarios.get(login);
        return u.listaAmigosFormato();
    }

    public void enviarRecado(String id, String destinatario, String recado) throws Exception {
        if (id == null || id.isEmpty() || !sessoes.containsKey(id)) {
            throw new Exception("Usuário não cadastrado.");
        }
        String remetente = sessoes.get(id);
        if (!usuarios.containsKey(remetente)) {
            throw new Exception("Usuário não cadastrado.");
        }
        if (!usuarios.containsKey(destinatario)) {
            throw new Exception("Usuário não cadastrado.");
        }
        if (remetente.equals(destinatario)) {
            throw new Exception("Usuário não pode enviar recado para si mesmo.");
        }
        Usuario userDest = usuarios.get(destinatario);
        userDest.receberRecado(recado);
    }

    public String lerRecado(String id) throws Exception {
        if (id == null || id.isEmpty() || !sessoes.containsKey(id)) {
            throw new Exception("Usuário não cadastrado.");
        }
        String login = sessoes.get(id);
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usuário não cadastrado.");
        }
        Usuario u = usuarios.get(login);
        String rec = u.lerRecado();
        if (rec == null) {
            throw new Exception("Não há recados.");
        }
        return rec;
    }

    public void criarUsuario(String login, String senha, String nome) throws Exception {
        if (login == null || login.isEmpty()) {
            throw new Exception("Login inválido.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new Exception("Senha inválida.");
        }
        if (usuarios.containsKey(login)) {
            throw new Exception("Conta com esse nome já existe.");
        }

        Usuario usuario = new Usuario(login, senha, nome);
        usuarios.put(login, usuario);
    }

    public String getAtributoUsuario(String login, String atributo) throws Exception {
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usuário não cadastrado.");
        }
        Usuario usuario = usuarios.get(login);
        String val = usuario.getAtributo(atributo);
        if (val == null) {
            throw new Exception("Atributo não preenchido.");
        }
        return val;
    }

    public void editarPerfil(String id, String atributo, String valor) throws Exception {
        if (id == null || id.isEmpty() || !sessoes.containsKey(id)) {
            throw new Exception("Usuário não cadastrado.");
        }
        String login = sessoes.get(id);
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usuário não cadastrado.");
        }
        Usuario usuario = usuarios.get(login);
        usuario.setAtributo(atributo, valor);
    }

    public void encerrarSistema() {
        salvarDados();
        usuarios.clear();
    }
}

