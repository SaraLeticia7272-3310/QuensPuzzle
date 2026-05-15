package com.tlp2.nightfallz.Model;

import java.time.LocalDate;

public class ranking {

    private String nomeJogador;
    private int pontuacao;
    private int wave;
    private LocalDate data;

    public ranking(String nomeJogador, int pontuacao, int wave, LocalDate data) {
        this.nomeJogador = nomeJogador;
        this.pontuacao = pontuacao;
        this.wave = wave;
        this.data = data;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public int getWave() {
        return wave;
    }

    public LocalDate getData() {
        return data;
    }
}