package br.ufal.ic;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String senha;
    private String nome;
    private java.util.Map<String, String> atributos;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.atributos = new java.util.HashMap<>();
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna o valor de um atributo do perfil. Se o atributo for "nome" retorna o nome principal.
    // Retorna null se o atributo não estiver definido.
    public String getAtributo(String atributo) {
        if ("nome".equalsIgnoreCase(atributo)) {
            return nome;
        }
        if (atributo == null) return null;
        if (atributos.containsKey(atributo)) {
            return atributos.get(atributo);
        }
        return null;
    }

    // Define/atualiza um atributo do perfil do usuário. Atributos nulos são ignorados.
    public void setAtributo(String atributo, String valor) {
        if (atributo == null) return;
        atributos.put(atributo, valor);
    }
}

