package com.tlp2.queenspuzzle.controller;


import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.PartidaDAO;
import com.tlp2.queenspuzzle.model.*;
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

public class GameplayController implements Initializable {

    @FXML private GridPane gridTabuleiro;
    @FXML private Label labelNome;
    @FXML private Label labelNivel;
    @FXML private Label labelPontos;
    @FXML private Label labelRainhas;
    @FXML private Label labelTempo;
    @FXML private Label labelMensagem;
    @FXML private Label labelMoedas;
    @FXML private Button btnDica;

    private Tabuleiro tabuleiro;
    private SessaoJogo sessao;
    private int tempoRestante;
    private Timeline timer;
    private int dicasUsadas;
    private int maxDicas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        sessao.iniciarNovaRun();

        int nivel = sessao.getNivelAtual();
        tabuleiro = new Tabuleiro(nivel);
        maxDicas = 1 + sessao.getUpgradeAtual().getDicasExtras();
        dicasUsadas = 0;

        tempoRestante = 60 + sessao.getUpgradeAtual().getTempoBonus();

        atualizarInfos();
        desenharTabuleiro();
        iniciarTimer();
    }

    private void desenharTabuleiro() {
        gridTabuleiro.getChildren().clear();
        gridTabuleiro.setGridLinesVisible(false);

        int n = tabuleiro.getTamanho();
        double tamanho = 320.0 / n; // tamanho de cada célula

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int linha = i;
                final int col = j;

                StackPane celula = criarCelula(linha, col, tamanho);
                celula.setOnMouseClicked(e -> aoClicarCelula(linha, col));

                gridTabuleiro.add(celula, j, i);
            }
        }
    }

    private StackPane criarCelula(int linha, int col, double tamanho) {
        StackPane celula = new StackPane();

        Rectangle fundo = new Rectangle(tamanho, tamanho);

        if ((linha + col) % 2 == 0) {
            fundo.setFill(Color.web("#f0d9b5")); // bege claro
        } else {
            fundo.setFill(Color.web("#b58863")); // marrom
        }

        fundo.setStroke(Color.web("#666666"));
        fundo.setStrokeWidth(0.5);

        celula.getChildren().add(fundo);

        if (tabuleiro.temRainha(linha, col)) {
            Text rainha = new Text("♛");
            rainha.setStyle("-fx-font-size: " + (tamanho * 0.65) + ";");

            // Rainha em conflito fica vermelha
            if (tabuleiro.estaEmConflito(linha, col)) {
                rainha.setFill(Color.RED);
            } else {
                rainha.setFill(Color.web("#1a1a2e"));
            }
            celula.getChildren().add(rainha);
        }

        return celula;
    }

    private void aoClicarCelula(int linha, int col) {
        tabuleiro.alternarRainha(linha, col);
        desenharTabuleiro();
        atualizarInfos();

        if (tabuleiro.estaSolucionado()) {
            timer.stop();
            int pontos = calcularPontos();
            sessao.adicionarPontos(pontos);
            sessao.adicionarMoedas(pontos / 10);

            sessao.adicionarItem(new Item("Coroa Real", "Completou nível " + tabuleiro.getTamanho(), 1));

            labelMensagem.setText("✓ Parabéns! +" + pontos + " pontos!");
            labelMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            atualizarInfos();

            Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
                salvarPartida();
                MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml");
            }));
            espera.play();
        }
    }

    private int calcularPontos() {
        int base = tabuleiro.getTamanho() * 100;
        int bonusTempo = tempoRestante * 5;
        int bonusUpgrade = sessao.getUpgradeAtual().getPontosBonus();
        return base + bonusTempo + bonusUpgrade;
    }

    private void iniciarTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoRestante--;
            labelTempo.setText("Tempo: " + tempoRestante + "s");

            if (tempoRestante <= 10) {
                labelTempo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }

            if (tempoRestante <= 0) {
                timer.stop();
                labelMensagem.setText("Tempo esgotado! Tente novamente.");
                labelMensagem.setStyle("-fx-text-fill: red;");
                salvarPartida();

                Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), ev ->
                        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml")));
                espera.play();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void atualizarInfos() {
        Jogador j = sessao.getJogadorAtual();
        labelNome.setText("Jogador: " + j.getNome());
        labelNivel.setText("Nível: " + tabuleiro.getTamanho() + "x" + tabuleiro.getTamanho());
        labelPontos.setText("Pontos: " + sessao.getPontuacaoRodada());
        labelMoedas.setText("Moedas: " + sessao.getMoedas());
        labelRainhas.setText("Rainhas: " + tabuleiro.contarRainhas() + "/" + tabuleiro.getTamanho());
        btnDica.setText("Dica (" + (maxDicas - dicasUsadas) + " restantes)");
    }

    private void salvarPartida() {
        Jogador jogador = sessao.getJogadorAtual();
        int pontos = sessao.getPontuacaoRodada();

        jogador.setPontuacaoTotal(jogador.getPontuacaoTotal() + pontos);

        if (tabuleiro.estaSolucionado() && tabuleiro.getTamanho() >= jogador.getNivelMaximo()) {
            jogador.setNivelMaximo(Math.min(jogador.getNivelMaximo() + 1, 10));
        }

        new JogadorDAO().atualizar(jogador);
        new PartidaDAO().salvar(new Partida(jogador.getId(), pontos, tabuleiro.getTamanho()));
    }

    @FXML
    private void aoClicarDica() {
        if (dicasUsadas >= maxDicas) {
            labelMensagem.setText("Sem dicas disponíveis!");
            return;
        }

        int[] dica = tabuleiro.getDica();
        if (dica != null) {
            labelMensagem.setText("Dica: tente a linha " + (dica[0] + 1) + ", coluna " + (dica[1] + 1));
            dicasUsadas++;
            atualizarInfos();
        } else {
            labelMensagem.setText("Nenhuma dica disponível agora.");
        }
    }

    @FXML
    private void aoClicarLimpar() {
        tabuleiro.limpar();
        desenharTabuleiro();
        labelMensagem.setText("");
        atualizarInfos();
    }

    @FXML
    private void aoClicarInventario() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/tlp2/queenspuzzle/view/Inventario.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stageInv = new javafx.stage.Stage();
            stageInv.setTitle("Inventário");
            stageInv.setScene(new javafx.scene.Scene(root, 400, 350));
            stageInv.initOwner(MainApp.getStagePrincipal());
            stageInv.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir inventário: " + e.getMessage());
        }
    }

    @FXML
    private void aoClicarLoja() {
        if (timer != null) timer.stop();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Loja.fxml");
    }

    @FXML
    private void aoClicarMenu() {
        if (timer != null) timer.stop();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
