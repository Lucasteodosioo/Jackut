package br.ufal.ic;

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

    // Tenta carregar o mapa de usuários a partir do arquivo de dados. Se falhar, inicia mapa vazio.
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            usuarios = (Map<String, Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            usuarios = new HashMap<>();
        }
    }

    // Persiste o mapa de usuários em disco no arquivo definido por ARQUIVO_DADOS.
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

    public void criarUsuario(String login, String senha, String nome) throws Exception {
        //validações
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
        // Salva os dados antes de limpar a memória para garantir persistência entre execuções.
        salvarDados();
        usuarios.clear();
    }
}

