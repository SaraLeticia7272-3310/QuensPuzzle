package com.tlp2.nightfallz.Model;

public class arma {

    private String nome;
    private int dano;
    private int municao;

    public arma(String nome, int dano, int municao) {
        this.nome = nome;
        this.dano = dano;
        this.municao = municao;
    }

    public boolean atirar() {

        if (municao > 0) {
            municao--;
            return true;
        }

        return false;
    }

    // GETTERS E SETTERS

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getMunicao() {
        return municao;
    }

    public void setMunicao(int municao) {
        this.municao = municao;
    }
}