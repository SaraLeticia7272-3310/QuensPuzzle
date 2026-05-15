package com.tlp2.nightfallz.Util;

import javafx.animation.AnimationTimer;

public abstract class loop extends AnimationTimer {

    private long ultimoFrame = 0;

    @Override
    public void handle(long agora) {

        if (ultimoFrame == 0) {
            ultimoFrame = agora;
            return;
        }

        double delta = (agora - ultimoFrame) / 1_000_000_000.0;

        atualizar(delta);

        ultimoFrame = agora;
    }

    public abstract void atualizar(double delta);
}