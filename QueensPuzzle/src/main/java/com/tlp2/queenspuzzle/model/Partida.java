package com.tlp2.queenspuzzle.model;

public class Partida {

    private int id;
    private int jogadorId;
    private int pontuacao;
    private int nivel;

    public Partida(int jogadorId, int pontuacao, int nivel) {
        this.jogadorId = jogadorId;
        this.pontuacao = pontuacao;
        this.nivel = nivel;
    }

    public int getId() {
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public int getJogadorId() { 
        return jogadorId; 
    }
    
    public void setJogadorId(int jogadorId) { 
        this.jogadorId = jogadorId; 
    }

    public int getPontuacao() { 
        return pontuacao; 
    }
    
    public void setPontuacao(int pontuacao) { 
        this.pontuacao = pontuacao; 
    }

    public int getNivel() { 
        return nivel; 
    }
    
    public void setNivel(int nivel) { 
        this.nivel = nivel; 
    }
}
