package com.tlp2.queenspuzzle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda os dados da sessão atual do jogo.
 * Singleton: existe só uma instância, compartilhada entre todas as telas.
 * 
 * Isso permite passar informações entre controllers sem complicação.
 */
public class SessaoJogo {

    // Instância única (padrão Singleton simples)
    private static SessaoJogo instancia;

    private Jogador jogadorAtual;
    private Upgrade upgradeAtual;
    private int pontuacaoRodada;      // pontos da run atual
    private int pontuacaoAcumulada;   // pontos acumulados na sessão (não zerados ao iniciar run)
    private int nivelAtual;           // tamanho do tabuleiro desta run
    private int moedas;               // moeda interna do jogo
    private List<Item> inventario;    // itens acumulados de todas as runs da sessão

    // Construtor privado (Singleton)
    private SessaoJogo() {
        this.inventario = new ArrayList<>();
        this.nivelAtual = 4;
        this.moedas = 0;
    }

    /**
     * Retorna a instância única da sessão.
     */
    public static SessaoJogo getInstance() {
        if (instancia == null) {
            instancia = new SessaoJogo();
        }
        return instancia;
    }

    /**
     * Inicia uma nova run.
     * Reseta apenas a pontuação da rodada; inventário e pontuação acumulada são mantidos.
     */
    public void iniciarNovaRun() {
        this.pontuacaoRodada = 0;
        // inventário e pontuacaoAcumulada NÃO são resetados aqui
    }

    /**
     * Reseta completamente a sessão (ao trocar de jogador).
     */
    public void resetarSessao() {
        this.pontuacaoRodada = 0;
        this.pontuacaoAcumulada = 0;
        this.inventario = new ArrayList<>();
        this.moedas = 0;
    }

    /**
     * Adiciona item ao inventário da run.
     */
    public void adicionarItem(Item item) {
        inventario.add(item);
    }

    // Getters e Setters
    public Jogador getJogadorAtual() { return jogadorAtual; }
    public void setJogadorAtual(Jogador jogador) { this.jogadorAtual = jogador; }

    public Upgrade getUpgradeAtual() { return upgradeAtual; }
    public void setUpgradeAtual(Upgrade upgrade) { this.upgradeAtual = upgrade; }

    public int getPontuacaoRodada() { return pontuacaoRodada; }
    public void setPontuacaoRodada(int pontos) { this.pontuacaoRodada = pontos; }
    public void adicionarPontos(int pontos) {
        this.pontuacaoRodada += pontos;
        this.pontuacaoAcumulada += pontos;
    }

    public int getPontuacaoAcumulada() { return pontuacaoAcumulada; }
    public void setPontuacaoAcumulada(int pontos) { this.pontuacaoAcumulada = pontos; }

    public int getNivelAtual() { return nivelAtual; }
    public void setNivelAtual(int nivel) { this.nivelAtual = nivel; }

    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }
    public void adicionarMoedas(int valor) { this.moedas += valor; }
    public boolean gastarMoedas(int valor) {
        if (moedas >= valor) {
            moedas -= valor;
            return true;
        }
        return false;
    }

    public List<Item> getInventario() { return inventario; }
}
