package com.tlp2.queenspuzzle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {

    public static void tocar(String arquivo) {
        try {
            URL resource = SoundManager.class.getResource("/sons/" + arquivo);
            if (resource == null) {
                System.err.println("Som não encontrado: " + arquivo);
                return;
            }
            MediaPlayer player = new MediaPlayer(new Media(resource.toString()));
            player.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar som '" + arquivo + "': " + e.getMessage());
        }
    }

    public static void botao() {
        tocar("button_click.wav");
    }

    public static void popTabuleiro() {
        tocar("pop_tabuleiro.wav");
    }

    public static void erro() {
        tocar("error.wav");
    }

    public static void win() {
        tocar("win.wav");
    }

    public static void lose() {
        tocar("lose.wav");
    }

    public static void moeda() {
        tocar("coin.wav");
    }
    
    public static void tictac() {
        tocar("relogio.wav");
    }
}
