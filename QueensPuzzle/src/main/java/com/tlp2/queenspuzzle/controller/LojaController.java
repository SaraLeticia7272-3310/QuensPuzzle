package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.model.*;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import com.tlp2.queenspuzzle.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller da Loja.
 * O jogador compra itens com moedas internas.
 */
public class LojaController implements Initializable {

    @FXML private Label labelMoedas;
    @FXML private Label labelMensagem;

    private SessaoJogo sessao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        atualizarMoedas();
    }

    private void atualizarMoedas() {
        labelMoedas.setText("Suas moedas: " + sessao.getMoedas());
    }

    /**
     * Compra Poção de Tempo (+30s na próxima run) por 20 moedas.
     */
    @FXML
    private void aoClicarComprarPocao() {
        if (sessao.gastarMoedas(20)) {
            sessao.getUpgradeAtual().setTempoBonus(
                    sessao.getUpgradeAtual().getTempoBonus() + 30);
            sessao.adicionarItem(new Item("Poção de Tempo", "+30s na próxima run", 1));
            labelMensagem.setText("✓ Poção de Tempo adquirida! (+30s)");
            atualizarMoedas();
        } else {
            labelMensagem.setText("Moedas insuficientes! (custo: 20)");
        }
    }

    /**
     * Compra Chave de Dica (1 dica extra) por 15 moedas.
     */
    @FXML
    private void aoClicarComprarChave() {
        if (sessao.gastarMoedas(15)) {
            sessao.getUpgradeAtual().setDicasExtras(
                    sessao.getUpgradeAtual().getDicasExtras() + 1);
            sessao.adicionarItem(new Item("Chave de Dica", "+1 dica na próxima run", 1));
            labelMensagem.setText("✓ Chave de Dica adquirida!");
            atualizarMoedas();
        } else {
            labelMensagem.setText("Moedas insuficientes! (custo: 15)");
        }
    }

    /**
     * Compra Amuleto de Pontos (+100 pontos bônus) por 25 moedas.
     */
    @FXML
    private void aoClicarComprarAmuleto() {
        if (sessao.gastarMoedas(25)) {
            sessao.getUpgradeAtual().setPontosBonus(
                    sessao.getUpgradeAtual().getPontosBonus() + 100);
            sessao.adicionarItem(new Item("Amuleto Real", "+100 pontos bônus", 1));
            labelMensagem.setText("✓ Amuleto Real adquirido!");
            atualizarMoedas();
        } else {
            labelMensagem.setText("Moedas insuficientes! (custo: 25)");
        }
    }

    /**
     * Volta para Gameplay.
     */
    @FXML
    private void aoClicarVoltar() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
    }

    /**
     * Volta para Upgrades.
     */
    @FXML
    private void aoClicarUpgrades() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml");
    }
}
