package com.tlp2.queenspuzzle.model;

/**
 *
 * @author saral
 */

public class Item {

    public enum Tipo {
        POCAO_TEMPO,    
        CHAVE_DICA,     
        AMULETO_PONTOS, 
        COROA_REAL,     
        GENERICO        
    }

    private String nome;
    private String descricao;
    private int quantidade;
    private Tipo tipo;

    public Item(String nome, String descricao, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.tipo = inferirTipo(nome);
    }

    public Item(String nome, String descricao, int quantidade, Tipo tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }

    private Tipo inferirTipo(String nome) {
        if (nome == null) return Tipo.GENERICO;
        switch (nome) {
            case "Poção de Tempo": return Tipo.POCAO_TEMPO;
            case "Chave de Dica":  return Tipo.CHAVE_DICA;
            case "Amuleto Real":   return Tipo.AMULETO_PONTOS;
            case "Coroa Real":     return Tipo.COROA_REAL;
            default:               return Tipo.GENERICO;
        }
    }

    public boolean isUsavel() {
        return tipo != Tipo.COROA_REAL && tipo != Tipo.GENERICO;
    }

    public String getNome(){ 
        return nome; 
    }
    
    public String getDescricao(){ 
        return descricao; 
    }
    
    public int getQuantidade(){ 
        return quantidade; 
    }
    
    public Tipo getTipo(){ 
        return tipo; 
    }

    public void setQuantidade(int quantidade){ 
        this.quantidade = quantidade; 
    }

    public String getEmoji() {
        switch (tipo) {
            case POCAO_TEMPO:    return "🕛";
            case CHAVE_DICA:     return "🗝";
            case AMULETO_PONTOS: return "💎";
            case COROA_REAL:     return "👑";
            default:             return "🎁";
        }
    }

    @Override
    public String toString() {
        return nome + " x" + quantidade + " — " + descricao;
    }
}
