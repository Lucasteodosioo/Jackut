package br.ufal.ic;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String senha;
    private String nome;
    private java.util.Map<String, String> atributos;
    private java.util.Set<String> amigos;
    private java.util.Set<String> pedidosRecebidos;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.atributos = new java.util.HashMap<>();
        this.amigos = new java.util.LinkedHashSet<>();
        this.pedidosRecebidos = new java.util.LinkedHashSet<>();
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
    
    public void setAtributo(String atributo, String valor) {
        if (atributo == null) return;
        atributos.put(atributo, valor);
    }

    // Marca que este usuário recebeu um pedido de amizade do usuário 'from'.
    public void receberPedido(String from) {
        if (from == null) return;
        pedidosRecebidos.add(from);
    }
    
    public void removerPedido(String from) {
        if (from == null) return;
        pedidosRecebidos.remove(from);
    }
    
    public boolean temPedidoDe(String from) {
        if (from == null) return false;
        return pedidosRecebidos.contains(from);
    }
    
    public void adicionarAmigo(String loginAmigo) {
        if (loginAmigo == null) return;
        amigos.add(loginAmigo);
    }
    
    public boolean ehAmigo(String loginAmigo) {
        if (loginAmigo == null) return false;
        return amigos.contains(loginAmigo);
    }

    // Retorna os amigos neste formato: {a,b,c} preservando ordem de inserção.
    public String listaAmigosFormato() {
        if (amigos == null || amigos.isEmpty()) {
            return "{}";
        }
        return "{" + String.join(",", amigos) + "}";
    }
}

