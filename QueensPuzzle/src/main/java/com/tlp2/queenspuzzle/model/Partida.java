package com.tlp2.queenspuzzle.model;

/**
 * Representa uma partida jogada.
 * Model: contém os dados de uma run.
 */
public class Partida {

    private int id;
    private int jogadorId;
    private int pontuacao;
    private int nivel;

    // Construtor
    public Partida(int jogadorId, int pontuacao, int nivel) {
        this.jogadorId = jogadorId;
        this.pontuacao = pontuacao;
        this.nivel = nivel;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJogadorId() { return jogadorId; }
    public void setJogadorId(int jogadorId) { this.jogadorId = jogadorId; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
}
