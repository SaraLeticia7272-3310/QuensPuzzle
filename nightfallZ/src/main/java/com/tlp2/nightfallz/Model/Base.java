package com.tlp2.nightfallz.Model;

public class Base {

    private int vida;

    public Base() {
        this.vida = 200;
    }

    public void receberDano(int dano) {
        vida -= dano;

        if (vida < 0) {
            vida = 0;
        }
    }

    public boolean destruida() {
        return vida <= 0;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
}