package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.model.*;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.dao.UpgradeDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author saral
 */

public class LojaController implements Initializable {

    @FXML 
    private Label labelMoedas;
    
    @FXML 
    private Label labelMensagem;

    private SessaoJogo sessao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        atualizarMoedas();
    }

    private void atualizarMoedas() {
        labelMoedas.setText("Suas moedas: " + sessao.getMoedas());
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
    private void aoClicarComprarPocao() {
        if (sessao.gastarMoedas(20)) {
            sessao.adicionarItem(new Item("Poção de Tempo", "+30s quando usada durante a partida", 1));
            labelMensagem.setText("✓ Poção de Tempo adquirida! Use no inventário durante a partida.");
            atualizarMoedas();
        } 
        else {
            labelMensagem.setText("Moedas insuficientes! (custo: 20)");
        }
    }

    @FXML
    private void aoClicarComprarChave() {
        if (sessao.gastarMoedas(15)) {
            sessao.adicionarItem(new Item("Chave de Dica", "+1 dica quando usada durante a partida", 1));
            labelMensagem.setText("✓ Chave de Dica adquirida! Use no inventário durante a partida.");
            atualizarMoedas();
        } 
        else {
            labelMensagem.setText("Moedas insuficientes! (custo: 15)");
        }
    }

    @FXML
    private void aoClicarComprarAmuleto() {
        if (sessao.gastarMoedas(25)) {
            sessao.adicionarItem(new Item("Amuleto Real", "+100 pontos quando usado durante a partida", 1));
            labelMensagem.setText("✓ Amuleto Real adquirido! Use no inventário durante a partida.");
            atualizarMoedas();
        } 
        else {
            labelMensagem.setText("Moedas insuficientes! (custo: 25)");
        }
    }
    
    @FXML
    private void aoClicarVoltar() {

        Jogador j = sessao.getJogadorAtual();

        sessao.setNivelAtual(j.getNivelMaximo());

        if (j.getNivelMaximo() >= 8) {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Boss.fxml");
        } else {
            MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
        }
    }

    @FXML
    private void aoClicarUpgrades() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml");
    }

    @FXML
    private void aoClicarMenu() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
