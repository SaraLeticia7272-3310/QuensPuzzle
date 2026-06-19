package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.dao.UpgradeDAO;
import com.tlp2.queenspuzzle.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller da tela de Upgrades.
 * O jogador gasta pontos da rodada para melhorar permanentemente.
 */
public class UpgradesController implements Initializable {

    @FXML private Label labelNome;
    @FXML private Label labelPontosDisponiveis;
    @FXML private Label labelDicas;
    @FXML private Label labelTempo;
    @FXML private Label labelPontosBonus;
    @FXML private Label labelMensagem;
    @FXML private Label labelProximoNivel;

    private SessaoJogo sessao;
    private int pontosDisponiveis;

    // Custo de cada upgrade em pontos
    private static final int CUSTO_DICA = 200;
    private static final int CUSTO_TEMPO = 150;
    private static final int CUSTO_PONTOS = 100;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        // Usa pontuacaoAcumulada para que os pontos não sumam ao navegar entre telas
        pontosDisponiveis = sessao.getPontuacaoAcumulada();
        atualizarLabels();
    }

    private void atualizarLabels() {
        Jogador j = sessao.getJogadorAtual();
        Upgrade u = sessao.getUpgradeAtual();

        labelNome.setText("Jogador: " + j.getNome());
        labelPontosDisponiveis.setText("Pontos disponíveis: " + pontosDisponiveis);
        labelDicas.setText("Dicas extras: " + u.getDicasExtras() + " (custo: " + CUSTO_DICA + " pts)");
        labelTempo.setText("Tempo bônus: +" + u.getTempoBonus() + "s (custo: " + CUSTO_TEMPO + " pts)");
        labelPontosBonus.setText("Bônus de pontos: +" + u.getPontosBonus() + " (custo: " + CUSTO_PONTOS + " pts)");

        // Mantém pontuacaoAcumulada sincronizada com pontosDisponiveis
        sessao.setPontuacaoAcumulada(pontosDisponiveis);

        int nivelAtual = sessao.getNivelAtual();
        int proximoNivel = (nivelAtual >= 8) ? 5 : nivelAtual + 1;
        labelProximoNivel.setText("Próxima run: tabuleiro " + proximoNivel + "×" + proximoNivel);
    }

    /**
     * Compra +1 dica extra por run.
     */
    @FXML
    private void aoClicarComprarDica() {
        if (pontosDisponiveis >= CUSTO_DICA) {
            pontosDisponiveis -= CUSTO_DICA;
            sessao.getUpgradeAtual().setDicasExtras(
                    sessao.getUpgradeAtual().getDicasExtras() + 1);
            salvarUpgrade();
            labelMensagem.setText("✓ +1 dica extra adquirida!");
            atualizarLabels();
        } else {
            labelMensagem.setText("Pontos insuficientes!");
        }
    }

    /**
     * Compra +15 segundos por run.
     */
    @FXML
    private void aoClicarComprarTempo() {
        if (pontosDisponiveis >= CUSTO_TEMPO) {
            pontosDisponiveis -= CUSTO_TEMPO;
            sessao.getUpgradeAtual().setTempoBonus(
                    sessao.getUpgradeAtual().getTempoBonus() + 15);
            salvarUpgrade();
            labelMensagem.setText("✓ +15 segundos por run!");
            atualizarLabels();
        } else {
            labelMensagem.setText("Pontos insuficientes!");
        }
    }

    /**
     * Compra +50 pontos bônus por run completada.
     */
    @FXML
    private void aoClicarComprarPontos() {
        if (pontosDisponiveis >= CUSTO_PONTOS) {
            pontosDisponiveis -= CUSTO_PONTOS;
            sessao.getUpgradeAtual().setPontosBonus(
                    sessao.getUpgradeAtual().getPontosBonus() + 50);
            salvarUpgrade();
            labelMensagem.setText("✓ +50 pontos bônus por vitória!");
            atualizarLabels();
        } else {
            labelMensagem.setText("Pontos insuficientes!");
        }
    }

    private void salvarUpgrade() {
        UpgradeDAO dao = new UpgradeDAO();
        Upgrade u = sessao.getUpgradeAtual();
        if (u.getId() > 0) {
            dao.atualizar(u);
        } else {
            dao.salvar(u);
        }
    }

    /**
     * Botão JOGAR NOVAMENTE: avança para o próximo nível em ciclo (5→6→7→8→5→...).
     */
    @FXML
    private void aoClicarJogarNovamente() {
        Jogador j = sessao.getJogadorAtual();

        // Próximo nível em ciclo: 5→6→7→8→5→...
        int nivelAtual = sessao.getNivelAtual();
        int proximoNivel = (nivelAtual >= 8) ? 5 : nivelAtual + 1;
        sessao.setNivelAtual(proximoNivel);

        // Boss desbloqueado a partir do nível 8 (só quando chega no 8, não nos ciclos subsequentes antes)
        if (proximoNivel == 8 && j.getNivelMaximo() >= 8) {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Boss.fxml");
        } else {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
        }
    }

    /**
     * Botão LOJA
     */
    @FXML
    private void aoClicarLoja() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Loja.fxml");
    }

    /**
     * Botão MENU PRINCIPAL
     */
    @FXML
    private void aoClicarMenu() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
