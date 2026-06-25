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
 *
 * @author saral
 */

public class UpgradesController implements Initializable {

    @FXML 
    private Label labelNome;
    
    @FXML 
    private Label labelPontosDisponiveis;
    
    @FXML
    private Label labelDicas;
    
    @FXML
    private Label labelTempo;
    
    @FXML
    private Label labelPontosBonus;
    
    @FXML
    private Label labelMensagem;
    
    @FXML
    private Label labelProximoNivel;

    private SessaoJogo sessao;
    private int pontosDisponiveis;
    private static final int CUSTO_DICA = 200;
    private static final int CUSTO_TEMPO = 150;
    private static final int CUSTO_PONTOS = 100;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
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

        sessao.setPontuacaoAcumulada(pontosDisponiveis);

        int nivelAtual = sessao.getNivelAtual();
        int proximoNivel = (nivelAtual >= 8) ? 5 : nivelAtual + 1;
        labelProximoNivel.setText("Próxima run: tabuleiro " + proximoNivel + "×" + proximoNivel);
    }

    @FXML
    private void aoClicarComprarDica() {
        if (pontosDisponiveis >= CUSTO_DICA) {
            pontosDisponiveis -= CUSTO_DICA;
            sessao.getUpgradeAtual().setDicasExtras(sessao.getUpgradeAtual().getDicasExtras() + 1);
            salvarUpgrade();
            labelMensagem.setText("✓ 1 dica extra adquirida!");
            atualizarLabels();
        }
        else {
            labelMensagem.setText("Pontos insuficientes!");
        }
    }

    @FXML
    private void aoClicarComprarTempo() {
        if (pontosDisponiveis >= CUSTO_TEMPO) {
            pontosDisponiveis -= CUSTO_TEMPO;
            sessao.getUpgradeAtual().setTempoBonus(sessao.getUpgradeAtual().getTempoBonus() + 15);
            salvarUpgrade();
            labelMensagem.setText("✓ +15 segundos por run!");
            atualizarLabels();
        } else {
            labelMensagem.setText("Pontos insuficientes!");
        }
    }
    
    @FXML
    private void aoClicarComprarPontos() {
        if (pontosDisponiveis >= CUSTO_PONTOS) {
            pontosDisponiveis -= CUSTO_PONTOS;
            sessao.getUpgradeAtual().setPontosBonus(sessao.getUpgradeAtual().getPontosBonus() + 50);
            salvarUpgrade();
            labelMensagem.setText("✓ 50 pontos bônus por vitória!");
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
        } 
        else {
            dao.salvar(u);
        }
    }

    @FXML
    private void aoClicarJogarNovamente() {

       Jogador j = sessao.getJogadorAtual();

        sessao.setNivelAtual(j.getNivelMaximo());

        if (j.getNivelMaximo() >= 8) {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Boss.fxml");
        } else {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
        }
    }
    
    @FXML
    private void aoClicarLoja() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Loja.fxml");
    }

    @FXML
    private void aoClicarMenu() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
