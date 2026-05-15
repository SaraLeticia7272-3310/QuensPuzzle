package com.tlp2.nightfallz.Model;

public class jogador {

    private String nome;
    private int vida;
    private int pontos;
    private int moedas;
    private int waveAtual;
    private arma arma;
    private inventario inventario;

    public jogador() {
        this.vida = 100;
        this.pontos = 0;
        this.moedas = 0;
        this.waveAtual = 1;
        this.arma = new arma("Pistola", 10, 15);
        this.inventario = new inventario();
    }

    public jogador(String nome) {
        this();
        this.nome = nome;
    }

    public void receberDano(int dano) {
        this.vida -= dano;

        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    public void adicionarPontos(int valor) {
        this.pontos += valor;
    }

    public void adicionarMoedas(int valor) {
        this.moedas += valor;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    // GETTERS E SETTERS

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public int getWaveAtual() {
        return waveAtual;
    }

    public void setWaveAtual(int waveAtual) {
        this.waveAtual = waveAtual;
    }

    public arma getArma() {
        return arma;
    }

    public void setArma(arma arma) {
        this.arma = arma;
    }

    public inventario getInventario() {
        return inventario;
    }

    public void setInventario(inventario inventario) {
        this.inventario = inventario;
    }
}