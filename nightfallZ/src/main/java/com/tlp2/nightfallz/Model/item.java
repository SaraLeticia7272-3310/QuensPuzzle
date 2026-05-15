package com.tlp2.nightfallz.Model;

public class item {

    private String nome;
    private String descricao;

    public item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}