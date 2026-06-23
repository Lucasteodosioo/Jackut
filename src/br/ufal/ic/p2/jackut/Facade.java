package br.ufal.ic.p2.jackut;

public class Facade {
    private SistemaJackut sistema;

    public Facade() {
        sistema = new SistemaJackut();
    }

    public void zerarSistema() {
        sistema.zerarSistema();
    }

    public void criarUsuario(String login, String senha, String nome) throws Exception {
        sistema.criarUsuario(login, senha, nome);
    }

    public String getAtributoUsuario(String login, String atributo) throws Exception {
        return sistema.getAtributoUsuario(login, atributo);
    }

    public String abrirSessao(String login, String senha) throws Exception {
        return sistema.abrirSessao(login, senha);
    }

    public void editarPerfil(String id, String atributo, String valor) throws Exception {
        sistema.editarPerfil(id, atributo, valor);
    }

    public void adicionarAmigo(String id, String amigo) throws Exception {
        sistema.adicionarAmigo(id, amigo);
    }

    public boolean ehAmigo(String login, String amigo) throws Exception {
        return sistema.ehAmigo(login, amigo);
    }

    public String getAmigos(String login) throws Exception {
        return sistema.getAmigos(login);
    }

    public void enviarRecado(String id, String destinatario, String recado) throws Exception {
        sistema.enviarRecado(id, destinatario, recado);
    }

    public String lerRecado(String id) throws Exception {
        return sistema.lerRecado(id);
    }

    public void criarComunidade(String sessao, String nome, String descricao) throws Exception {
        sistema.criarComunidade(sessao, nome, descricao);
    }

    public String getDescricaoComunidade(String nome) throws Exception {
        return sistema.getDescricaoComunidade(nome);
    }

    public String getDonoComunidade(String nome) throws Exception {
        return sistema.getDonoComunidade(nome);
    }

    public String getMembrosComunidade(String nome) throws Exception {
        return sistema.getMembrosComunidade(nome);
    }

    public void adicionarComunidade(String sessao, String nome) throws Exception {
        sistema.adicionarComunidade(sessao, nome);
    }

    public String getComunidades(String idOuLogin) throws Exception {
        return sistema.getComunidades(idOuLogin);
    }

    public void encerrarSistema() {
        sistema.encerrarSistema();
    }
}

