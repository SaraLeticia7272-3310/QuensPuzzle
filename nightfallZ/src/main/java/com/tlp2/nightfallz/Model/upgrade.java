package com.tlp2.nightfallz.Model;

public class upgrade {

    private String nome;
    private int custo;
    private String efeito;

    public upgrade(String nome, int custo, String efeito) {
        this.nome = nome;
        this.custo = custo;
        this.efeito = efeito;
    }

    public String getNome() {
        return nome;
    }

    public int getCusto() {
        return custo;
    }

    public String getEfeito() {
        return efeito;
    }
}