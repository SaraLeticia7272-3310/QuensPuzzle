package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.UpgradeDAO;
import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.model.Jogador;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import com.tlp2.queenspuzzle.model.Upgrade;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller do Menu Principal.
 * Gerencia os cliques dos botões do menu.
 */
public class MenuPrincipalController {

    @FXML private TextField campoNome;
    @FXML private Label labelMensagem;

    /**
     * Botão JOGAR: valida nome e inicia o jogo.
     */
    @FXML
    private void aoClicarJogar() {
        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {
            labelMensagem.setText("Digite seu nome para jogar!");
            return;
        }

        JogadorDAO jogadorDAO = new JogadorDAO();
        UpgradeDAO upgradeDAO = new UpgradeDAO();

        // Busca jogador existente ou cria novo
        Jogador jogador = jogadorDAO.buscarPorNome(nome);
        if (jogador == null) {
            jogador = new Jogador(nome);
            jogador = jogadorDAO.salvar(jogador);

            // Cria upgrade padrão para novo jogador
            Upgrade upgrade = new Upgrade(jogador.getId());
            upgradeDAO.salvar(upgrade);
        }

        // Carrega upgrade do jogador
        Upgrade upgrade = upgradeDAO.buscarPorJogador(jogador.getId());

        // Salva na sessão
        SessaoJogo sessao = SessaoJogo.getInstance();
        sessao.setJogadorAtual(jogador);
        sessao.setUpgradeAtual(upgrade);
        sessao.setMoedas(50); // moedas iniciais por sessão

        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Gameplay.fxml");
    }

    /**
     * Botão RANKING
     */
    @FXML
    private void aoClicarRanking() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Ranking.fxml");
    }

    /**
     * Botão CRÉDITOS
     */
    @FXML
    private void aoClicarCreditos() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Creditos.fxml");
    }

    /**
     * Botão ENCERRAR
     */
    @FXML
    private void aoClicarEncerrar() {
        System.exit(0);
    }
}
