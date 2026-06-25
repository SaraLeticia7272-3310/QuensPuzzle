package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.UpgradeDAO;
import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.model.Jogador;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import com.tlp2.queenspuzzle.model.Upgrade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author saral
 */

public class MenuPrincipalController {

    @FXML 
    private TextField campoNome;
    
    @FXML
    private Label labelMensagem;
    
    
    @FXML
    private void aoClicarJogar() {
        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {
            labelMensagem.setText("Digite seu nome para jogar!");
            return;
        }

        JogadorDAO jogadorDAO = new JogadorDAO();
        UpgradeDAO upgradeDAO = new UpgradeDAO();

        Jogador jogador = jogadorDAO.buscarPorNome(nome);
        if (jogador == null) {
            jogador = new Jogador(nome);
            jogador = jogadorDAO.salvar(jogador);
            Upgrade upgrade = new Upgrade(jogador.getId());
            upgradeDAO.salvar(upgrade);
        }

        Upgrade upgrade = upgradeDAO.buscarPorJogador(jogador.getId());

        SessaoJogo sessao = SessaoJogo.getInstance();

        boolean mesmJogador = sessao.getJogadorAtual() != null && sessao.getJogadorAtual().getId() == jogador.getId();
        if (!mesmJogador) {
            sessao.resetarSessao(); 
            sessao.setMoedas(50);   
        }

        sessao.setJogadorAtual(jogador);
        sessao.setUpgradeAtual(upgrade);
        
        sessao.setNivelAtual(jogador.getNivelMaximo());

        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
    }

    @FXML
    private void aoClicarLoja() {
        SessaoJogo sessao = SessaoJogo.getInstance();
        if (sessao.getJogadorAtual() == null || sessao.getUpgradeAtual() == null) {
            labelMensagem.setText("Digite seu nome para acessar a loja!");
            return;
        }
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Loja.fxml");
    }

    @FXML
    private void aoClicarRanking() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Ranking.fxml");
    }

    @FXML
    private void aoClicarComoJogar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tlp2/queenspuzzle/view/ComoJogar.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Como Jogar");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void aoClicarCreditos() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Creditos.fxml");
    }

    @FXML
    private void aoClicarEncerrar() {
        System.exit(0);
    }
}
