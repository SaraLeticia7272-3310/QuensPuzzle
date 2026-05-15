package com.tlp2.queenspuzzle.controller;


import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.PartidaDAO;
import com.tlp2.queenspuzzle.model.Jogador;
import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.model.Partida;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import com.tlp2.queenspuzzle.model.Tabuleiro;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class BossController implements Initializable {

    @FXML private GridPane gridTabuleiro;
    @FXML private Label labelTempo;
    @FXML private Label labelRainhas;
    @FXML private Label labelMensagem;
    @FXML private Label labelStatus;

    private Tabuleiro tabuleiro;
    private SessaoJogo sessao;
    private int tempoRestante;
    private Timeline timer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        tabuleiro = new Tabuleiro(8); 

        tempoRestante = 45 + sessao.getUpgradeAtual().getTempoBonus();

        labelStatus.setText("⚠ DESAFIO FINAL: Tabuleiro 8x8 em tempo reduzido!");
        desenharTabuleiro();
        iniciarTimer();
    }

    private void desenharTabuleiro() {
        gridTabuleiro.getChildren().clear();
        int n = tabuleiro.getTamanho();
        double tamanho = 320.0 / n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int linha = i;
                final int col = j;

                StackPane celula = new StackPane();
                Rectangle fundo = new Rectangle(tamanho, tamanho);

                if ((linha + col) % 2 == 0) {
                    fundo.setFill(Color.web("#c0392b")); // vermelho (tema boss)
                } else {
                    fundo.setFill(Color.web("#7b241c"));
                }
                fundo.setStroke(Color.BLACK);

                celula.getChildren().add(fundo);

                if (tabuleiro.temRainha(linha, col)) {
                    Text rainha = new Text("♛");
                    rainha.setStyle("-fx-font-size: " + (tamanho * 0.65) + ";");
                    if (tabuleiro.estaEmConflito(linha, col)) {
                        rainha.setFill(Color.YELLOW);
                    } else {
                        rainha.setFill(Color.WHITE);
                    }
                    celula.getChildren().add(rainha);
                }

                celula.setOnMouseClicked(e -> aoClicarCelula(linha, col));
                gridTabuleiro.add(celula, j, i);
            }
        }
        labelRainhas.setText("Rainhas: " + tabuleiro.contarRainhas() + "/8");
    }

    private void aoClicarCelula(int linha, int col) {
        tabuleiro.alternarRainha(linha, col);
        desenharTabuleiro();

        if (tabuleiro.estaSolucionado()) {
            timer.stop();
            int pontos = 800 + (tempoRestante * 10) + sessao.getUpgradeAtual().getPontosBonus();
            sessao.adicionarPontos(pontos);

            labelMensagem.setText("★ BOSS DERROTADO! +" + pontos + " pontos!");
            labelMensagem.setStyle("-fx-text-fill: gold; -fx-font-weight: bold;");

            Jogador j = sessao.getJogadorAtual();
            j.setPontuacaoTotal(j.getPontuacaoTotal() + pontos);
            j.setNivelMaximo(Math.min(j.getNivelMaximo() + 1, 10));
            new JogadorDAO().atualizar(j);
            new PartidaDAO().salvar(new Partida(j.getId(), pontos, 8));

            Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), e ->
                    MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml")));
            espera.play();
        }
    }

    private void iniciarTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoRestante--;
            labelTempo.setText("Tempo: " + tempoRestante + "s");
            labelTempo.setStyle(tempoRestante <= 10 ?
                    "-fx-text-fill: yellow; -fx-font-weight: bold;" : "-fx-text-fill: white;");

            if (tempoRestante <= 0) {
                timer.stop();
                labelMensagem.setText("Boss venceu! Tente novamente.");
                Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), ev ->
                        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml")));
                espera.play();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void aoClicarLimpar() {
        tabuleiro.limpar();
        desenharTabuleiro();
    }

    @FXML
    private void aoClicarMenu() {
        if (timer != null) timer.stop();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
