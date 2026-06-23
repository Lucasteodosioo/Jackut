package br.ufal.ic.p2.jackut;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String senha;
    private String nome;
    private java.util.Map<String, String> atributos;
    private java.util.Set<String> amigos;
    private java.util.Set<String> pedidosRecebidos;
    private java.util.Queue<String> recados;
    private java.util.LinkedHashSet<String> comunidades;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.atributos = new java.util.HashMap<>();
        this.amigos = new java.util.LinkedHashSet<>();
        this.pedidosRecebidos = new java.util.LinkedHashSet<>();
        this.recados = new java.util.LinkedList<>();
        this.comunidades = new java.util.LinkedHashSet<>();
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

    public String listaAmigosFormato() {
        if (amigos == null || amigos.isEmpty()) {
            return "{}";
        }
        return "{" + String.join(",", amigos) + "}";
    }

    public void receberRecado(String recado) {
        if (recado == null) return;
        recados.add(recado);
    }

    public String lerRecado() {
        return recados.poll();
    }

    public void adicionarComunidade(String nome) {
        if (nome == null) return;
        comunidades.add(nome);
    }

    public boolean pertenceComunidade(String nome) {
        if (nome == null) return false;
        return comunidades.contains(nome);
    }

    public String listaComunidadesFormato() {
        if (comunidades == null || comunidades.isEmpty()) return "{}";
        return "{" + String.join(",", comunidades) + "}";
    }
}

