package com.tlp2.nightfallz.Model;

public class zumbi {

    protected double x;
    protected double y;

    protected int vida;
    protected int dano;
    protected double velocidade;

    public zumbi(double x, double y) {
        this.x = x;
        this.y = y;

        this.vida = 30;
        this.dano = 5;
        this.velocidade = 1.0;
    }

    public void mover() {
        x -= velocidade;
    }

    public void receberDano(int dano) {
        vida -= dano;
    }

    public boolean morto() {
        return vida <= 0;
    }

    // GETTERS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getVida() {
        return vida;
    }

    public int getDano() {
        return dano;
    }

    public double getVelocidade() {
        return velocidade;
    }
}