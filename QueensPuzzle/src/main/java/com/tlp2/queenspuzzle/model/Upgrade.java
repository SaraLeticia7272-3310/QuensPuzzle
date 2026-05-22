package com.tlp2.queenspuzzle.model;

/**
 * Representa os upgrades permanentes de um jogador.
 * São melhorias que persistem entre as runs.
 */
public class Upgrade {

    private int id;
    private int jogadorId;
    private int dicasExtras;    // quantidade de dicas disponíveis por run
    private int tempoBonus;     // segundos extras por run
    private int pontosBonus;    // pontos extras ao completar

    // Construtor
    public Upgrade(int jogadorId) {
        this.jogadorId = jogadorId;
        this.dicasExtras = 0;
        this.tempoBonus = 0;
        this.pontosBonus = 0;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJogadorId() { return jogadorId; }
    public void setJogadorId(int jogadorId) { this.jogadorId = jogadorId; }

    public int getDicasExtras() { return dicasExtras; }
    public void setDicasExtras(int dicasExtras) { this.dicasExtras = dicasExtras; }

    public int getTempoBonus() { return tempoBonus; }
    public void setTempoBonus(int tempoBonus) { this.tempoBonus = tempoBonus; }

    public int getPontosBonus() { return pontosBonus; }
    public void setPontosBonus(int pontosBonus) { this.pontosBonus = pontosBonus; }
}
