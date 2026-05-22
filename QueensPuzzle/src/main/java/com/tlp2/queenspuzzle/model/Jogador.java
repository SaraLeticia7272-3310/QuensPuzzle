package com.tlp2.queenspuzzle.model;

/**
 * Representa um jogador do jogo.
 * Model: contém os dados do jogador.
 */
public class Jogador {

    private int id;
    private String nome;
    private int pontuacaoTotal;
    private int nivelMaximo;

    // Construtor vazio
    public Jogador() {
        this.nivelMaximo = 5; // começa com tabuleiro 5x5 (mínimo com regiões)
    }

    // Construtor com nome
    public Jogador(String nome) {
        this.nome = nome;
        this.pontuacaoTotal = 0;
        this.nivelMaximo = 5;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public void setPontuacaoTotal(int pontuacaoTotal) { this.pontuacaoTotal = pontuacaoTotal; }

    public int getNivelMaximo() { return nivelMaximo; }
    public void setNivelMaximo(int nivelMaximo) { this.nivelMaximo = nivelMaximo; }

    @Override
    public String toString() {
        return nome + " - Pontos: " + pontuacaoTotal;
    }
}
