package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Comunidade implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String descricao;
    private String dono;
    private Set<String> membros;

    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome;
        this.descricao = descricao;
        this.dono = dono;
        this.membros = new LinkedHashSet<>();
        if (dono != null) {
            this.membros.add(dono);
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDono() {
        return dono;
    }

    public String getMembrosFormato() {
        if (membros == null || membros.isEmpty()) return "{}";
        return "{" + String.join(",", membros) + "}";
    }

    public void adicionarMembro(String login) {
        if (login == null) return;
        membros.add(login);
    }

    public boolean temMembro(String login) {
        if (login == null) return false;
        return membros.contains(login);
    }
}
