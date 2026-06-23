package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class SistemaJackut {
    private Map<String, Usuario> usuarios;
    private Map<String, String> sessoes;
    private Map<String, Comunidade> comunidades;
    private static final String ARQUIVO_DADOS = "usuarios.dat";

    public SistemaJackut() {
        usuarios = new HashMap<>();
        sessoes = new HashMap<>();
        comunidades = new java.util.LinkedHashMap<>();
        carregarDados();
    }

    // tenta carregar os dados (usuarios e comunidades) a partir do arquivo de dados; se falhar, inicia mapas vazios
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<?,?> map = (Map<?,?>) obj;
                boolean oldFormat = false;
                if (!map.isEmpty()) {
                    Object sampleVal = map.values().iterator().next();
                    if (sampleVal instanceof Usuario) {
                        oldFormat = true;
                    }
                } else {
                    // empty map -> treat as usuarios map (backward compatibility)
                    oldFormat = true;
                }
                if (oldFormat) {
                    usuarios = (Map<String, Usuario>) map;
                    comunidades = new HashMap<>();
                } else {
                    Object u = map.get("usuarios");
                    Object c = map.get("comunidades");
                    if (u instanceof Map) {
                        usuarios = (Map<String, Usuario>) u;
                    } else {
                        usuarios = new HashMap<>();
                    }
                    if (c instanceof Map) {
                        comunidades = (Map<String, Comunidade>) c;
                    } else {
                        comunidades = new HashMap<>();
                    }
                }
            } else {
                usuarios = new HashMap<>();
                comunidades = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            usuarios = new HashMap<>();
            comunidades = new HashMap<>();
        }
    }

    // persiste usuarios e comunidades em disco no arquivo definido por ARQUIVO_DADOS.
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            Map<String, Object> dados = new HashMap<>();
            dados.put("usuarios", usuarios);
            dados.put("comunidades", comunidades);
            oos.writeObject(dados);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        comunidades.clear();
        // remove persisted data so tests start with a clean slate
        try {
            java.io.File f = new java.io.File(ARQUIVO_DADOS);
            if (f.exists()) f.delete();
        } catch (Exception e) {
            // ignore
        }
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

    public void criarComunidade(String sessao, String nome, String descricao) throws Exception {
        if (sessao == null || sessao.isEmpty() || !sessoes.containsKey(sessao)) {
            throw new Exception("Usuário não cadastrado.");
        }
        String dono = sessoes.get(sessao);
        if (!usuarios.containsKey(dono)) {
            throw new Exception("Usuário não cadastrado.");
        }
        if (nome == null || nome.isEmpty()) {
            throw new Exception("Nome inválido.");
        }
        if (comunidades.containsKey(nome)) {
            throw new Exception("Comunidade com esse nome j� existe.");
        }
        Comunidade c = new Comunidade(nome, descricao, dono);
        comunidades.put(nome, c);
        // add to owner's list of communities
        Usuario u = usuarios.get(dono);
        if (u != null) u.adicionarComunidade(nome);
    }

    public String getDescricaoComunidade(String nome) throws Exception {
        if (!comunidades.containsKey(nome)) {
            throw new Exception("Comunidade n�o existe.");
        }
        return comunidades.get(nome).getDescricao();
    }

    public String getDonoComunidade(String nome) throws Exception {
        if (!comunidades.containsKey(nome)) {
            throw new Exception("Comunidade n�o existe.");
        }
        return comunidades.get(nome).getDono();
    }

    public String getMembrosComunidade(String nome) throws Exception {
        if (!comunidades.containsKey(nome)) {
            throw new Exception("Comunidade n�o existe.");
        }
        return comunidades.get(nome).getMembrosFormato();
    }

    public void adicionarComunidade(String sessao, String nome) throws Exception {
        if (sessao == null || sessao.isEmpty() || !sessoes.containsKey(sessao)) {
            throw new Exception("Usu�rio n�o cadastrado.");
        }
        String login = sessoes.get(sessao);
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usu�rio n�o cadastrado.");
        }
        if (!comunidades.containsKey(nome)) {
            throw new Exception("Comunidade n�o existe.");
        }
        Comunidade c = comunidades.get(nome);
        if (c.temMembro(login) || usuarios.get(login).pertenceComunidade(nome)) {
            throw new Exception("Usuario j� faz parte dessa comunidade.");
        }
        c.adicionarMembro(login);
        usuarios.get(login).adicionarComunidade(nome);
    }

    public String getComunidades(String idOuLogin) throws Exception {
        if (idOuLogin == null || idOuLogin.isEmpty()) {
            throw new Exception("Usu�rio n�o cadastrado.");
        }
        String login = idOuLogin;
        if (sessoes.containsKey(idOuLogin)) {
            login = sessoes.get(idOuLogin);
        }
        if (!usuarios.containsKey(login)) {
            throw new Exception("Usu�rio n�o cadastrado.");
        }
        Usuario u = usuarios.get(login);
        return u.listaComunidadesFormato();
    }

    public void encerrarSistema() {
        salvarDados();
        usuarios.clear();
        comunidades.clear();
    }
}

