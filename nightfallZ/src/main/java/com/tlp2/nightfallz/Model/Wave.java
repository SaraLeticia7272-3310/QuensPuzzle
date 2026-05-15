package com.tlp2.nightfallz.Model;

public class Wave {

    private int numero;
    private int quantidadeZumbis;
    private String dificuldade;

    public Wave(int numero, int quantidadeZumbis, String dificuldade) {
        this.numero = numero;
        this.quantidadeZumbis = quantidadeZumbis;
        this.dificuldade = dificuldade;
    }

    public int getNumero() {
        return numero;
    }

    public int getQuantidadeZumbis() {
        return quantidadeZumbis;
    }

    public String getDificuldade() {
        return dificuldade;
    }
}