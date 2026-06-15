package com.tlp2.queenspuzzle.model;

/**
 *
 * @author saral
 */

public class LinhaRanking {
    
    private String posicao;
    private String nome;
    private Integer pontos;
    private String nivel;

    public LinhaRanking(String posicao, String nome, Integer pontos, String nivel) {
        this.posicao = posicao;
        this.nome = nome;
        this.pontos = pontos;
        this.nivel = nivel;
    }

    public String getPosicao() {
        return posicao;
    }

    public String getNome() {
        return nome;
    }

    public Integer getPontos() {
        return pontos;
    }

    public String getNivel() {
        return nivel;
    }
}
