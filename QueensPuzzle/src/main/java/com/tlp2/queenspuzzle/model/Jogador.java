package com.tlp2.queenspuzzle.model;

public class Jogador {

    private int id;
    private String nome;
    private int pontuacaoTotal;
    private int nivelMaximo;

    public Jogador() {
        this.nivelMaximo = 4; 
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.pontuacaoTotal = 0;
        this.nivelMaximo = 4;
    }

    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNome() { 
        return nome; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public int getPontuacaoTotal() { 
        return pontuacaoTotal; 
    }
    
    public void setPontuacaoTotal(int pontuacaoTotal) { 
        this.pontuacaoTotal = pontuacaoTotal; 
    }
    
    public int getNivelMaximo() { 
        return nivelMaximo; 
    }
    
    public void setNivelMaximo(int nivelMaximo) { 
        this.nivelMaximo = nivelMaximo; 
    }

    @Override
    public String toString() {
        return nome + " - Pontos: " + pontuacaoTotal;
    }
}
