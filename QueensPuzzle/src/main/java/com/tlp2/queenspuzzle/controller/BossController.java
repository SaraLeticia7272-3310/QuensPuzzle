package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.PartidaDAO;
import com.tlp2.queenspuzzle.model.*;
import com.tlp2.queenspuzzle.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller do Boss: tabuleiro 8x8 com regiões coloridas e tempo reduzido.
 */
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

    // Mesma paleta suave
    private static final String[] CORES_REGIAO = {
        "#c9e4ff", "#bbc5ff", "#d8b7fe", "#fcc5fe",
        "#ef82db", "#a8d8ea", "#b5ead7", "#ffdac1"
    };
    private static final String[] BORDAS_REGIAO = {
        "#7ab8e8", "#8b9fe8", "#b088e8", "#e099e8",
        "#c050b0", "#70adc0", "#80c0a0", "#e0a080"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        tabuleiro = new Tabuleiro(8);
        tempoRestante = 60 + sessao.getUpgradeAtual().getTempoBonus();

        labelStatus.setText("DESAFIO FINAL — Tabuleiro 8×8, tempo reduzido!");
        desenharTabuleiro();
        iniciarTimer();
    }

    private void desenharTabuleiro() {
        gridTabuleiro.getChildren().clear();
        int n = tabuleiro.getTamanho();
        double tam = 336.0 / n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int linha = i;
                final int col = j;
                StackPane celula = criarCelula(linha, col, tam, n);
                celula.setOnMouseClicked(e -> aoClicarCelula(linha, col));
                gridTabuleiro.add(celula, j, i);
            }
        }
        labelRainhas.setText("Rainhas: " + tabuleiro.contarRainhas() + "/8");
    }

    private StackPane criarCelula(int linha, int col, double tam, int n) {
        StackPane celula = new StackPane();
        int regiao = tabuleiro.getRegiao(linha, col);
        String corFundo = CORES_REGIAO[regiao % CORES_REGIAO.length];
        String corBorda = BORDAS_REGIAO[regiao % BORDAS_REGIAO.length];

        Rectangle fundo = new Rectangle(tam, tam);
        fundo.setFill(Color.web(corFundo));
        fundo.setStroke(Color.web(corBorda));
        fundo.setStrokeWidth(0.5);
        celula.getChildren().add(fundo);

        // Bordas de região
        adicionarBordas(celula, linha, col, tam, n, regiao, corBorda);

        if (tabuleiro.temRainha(linha, col)) {
            boolean conflito = tabuleiro.estaEmConflito(linha, col);
            Circle circulo = new Circle(tam * 0.38);
            circulo.setFill(conflito ? Color.web("#ff444488") : Color.web("#ffffff99"));
            circulo.setStroke(conflito ? Color.web("#cc0000") : Color.web("#7b4f00"));
            circulo.setStrokeWidth(1.5);
            Text coroa = new Text("♛");
            coroa.setStyle("-fx-font-size: " + (tam * 0.48) + ";");
            coroa.setFill(conflito ? Color.WHITE : Color.web("#c8860a"));
            celula.getChildren().addAll(circulo, coroa);
        }
        celula.setStyle("-fx-cursor: hand;");
        return celula;
    }

    private void adicionarBordas(StackPane celula, int linha, int col, double tam, int n, int regiao, String corBorda) {
        if (linha == 0 || tabuleiro.getRegiao(linha-1, col) != regiao) {
            Line l = new Line(0, 0, tam, 0); l.setStroke(Color.web(corBorda)); l.setStrokeWidth(2.5);
            celula.getChildren().add(l); StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (linha == n-1 || tabuleiro.getRegiao(linha+1, col) != regiao) {
            Line l = new Line(0, tam-1.25, tam, tam-1.25); l.setStroke(Color.web(corBorda)); l.setStrokeWidth(2.5);
            celula.getChildren().add(l); StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (col == 0 || tabuleiro.getRegiao(linha, col-1) != regiao) {
            Line l = new Line(0, 0, 0, tam); l.setStroke(Color.web(corBorda)); l.setStrokeWidth(2.5);
            celula.getChildren().add(l); StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (col == n-1 || tabuleiro.getRegiao(linha, col+1) != regiao) {
            Line l = new Line(tam-1.25, 0, tam-1.25, tam); l.setStroke(Color.web(corBorda)); l.setStrokeWidth(2.5);
            celula.getChildren().add(l); StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
    }

    private void aoClicarCelula(int linha, int col) {
        tabuleiro.alternarRainha(linha, col);
        desenharTabuleiro();

        if (tabuleiro.estaSolucionado()) {
            timer.stop();
            int pontos = 800 + (tempoRestante * 10) + sessao.getUpgradeAtual().getPontosBonus();
            sessao.adicionarPontos(pontos);

            labelMensagem.setText("BOSS DERROTADO! +" + pontos + " pontos!");
            labelMensagem.setStyle("-fx-text-fill: #c8860a; -fx-font-weight: bold;");

            Jogador j = sessao.getJogadorAtual();
            j.setPontuacaoTotal(j.getPontuacaoTotal() + pontos);
            j.setNivelMaximo(Math.min(j.getNivelMaximo() + 1, 8));
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
            if (tempoRestante <= 15) labelTempo.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

            if (tempoRestante <= 0) {
                timer.stop();
                labelMensagem.setText("Você não venceu o boss desta vez!");
                Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), ev ->
                        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml")));
                espera.play();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML private void aoClicarLimpar() { tabuleiro.limpar(); desenharTabuleiro(); }
    @FXML private void aoClicarMenu() { if (timer != null) timer.stop(); MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml"); }
}
