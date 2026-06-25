package com.tlp2.queenspuzzle.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author saral
 */

public class SessaoJogo {

    private static SessaoJogo instancia;
    private Jogador jogadorAtual;
    private Upgrade upgradeAtual;
    private int pontuacaoRodada;      
    private int pontuacaoAcumulada;   
    private int nivelAtual;           
    private int moedas;               
    private List<Item> inventario;
    
    private SessaoJogo() {
        this.inventario = new ArrayList<>();
        this.nivelAtual = 4;
        this.moedas = 0;
    }

    public static SessaoJogo getInstance() {
        if (instancia == null) {
            instancia = new SessaoJogo();
        }
        return instancia;
    }

    public void iniciarNovaRun() {
        this.pontuacaoRodada = 0;
    }

    public void resetarSessao() {
        this.pontuacaoRodada = 0;
        this.pontuacaoAcumulada = 0;
        this.inventario = new ArrayList<>();
        this.moedas = 0;
    }

    public void adicionarItem(Item item) {
        inventario.add(item);
    }

    public Jogador getJogadorAtual() { 
        return jogadorAtual; 
    }
    
    public void setJogadorAtual(Jogador jogador) { 
        this.jogadorAtual = jogador; 
    }

    public Upgrade getUpgradeAtual() { 
        return upgradeAtual;
    }
    
    public void setUpgradeAtual(Upgrade upgrade) { 
        this.upgradeAtual = upgrade; 
    }

    public int getPontuacaoRodada() { 
        return pontuacaoRodada; 
    }
    
    public void setPontuacaoRodada(int pontos) { 
        this.pontuacaoRodada = pontos; 
    }
    
    public void adicionarPontos(int pontos) {
        this.pontuacaoRodada += pontos;
        this.pontuacaoAcumulada += pontos;
    }

    public int getPontuacaoAcumulada() { 
        return pontuacaoAcumulada; 
    }
    
    public void setPontuacaoAcumulada(int pontos) { 
        this.pontuacaoAcumulada = pontos; 
    }

    public int getNivelAtual() { 
        return nivelAtual; 
    }
    
    public void setNivelAtual(int nivel) { 
        this.nivelAtual = nivel; 
    }

    public int getMoedas() { 
        return moedas; 
    }
    
    public void setMoedas(int moedas) {
        this.moedas = moedas; 
    }
    
    public void adicionarMoedas(int valor) { 
        this.moedas += valor; 
    }
    
    public boolean gastarMoedas(int valor) {
        if (moedas >= valor) {
            moedas -= valor;
            return true;
        }
        return false;
    }

    public List<Item> getInventario() { 
        return inventario; 
    }
}