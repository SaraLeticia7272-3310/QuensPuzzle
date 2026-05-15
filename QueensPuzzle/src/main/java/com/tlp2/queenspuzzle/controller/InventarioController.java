package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.model.Item;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InventarioController implements Initializable {

    @FXML private TextArea areaItens;
    @FXML private Label labelTitulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SessaoJogo sessao = SessaoJogo.getInstance();
        List<Item> itens = sessao.getInventario();

        String nome = sessao.getJogadorAtual() != null ?
                sessao.getJogadorAtual().getNome() : "Jogador";

        labelTitulo.setText("Inventário de " + nome);

        StringBuilder sb = new StringBuilder();
        if (itens.isEmpty()) {
            sb.append("Inventário vazio.\n\nJogue para coletar itens!");
        } else {
            sb.append("Itens coletados nesta run:\n\n");
            for (Item item : itens) {
                sb.append("► ").append(item.toString()).append("\n");
            }
        }
        areaItens.setText(sb.toString());
    }

    @FXML
    private void aoClicarFechar() {
        Stage stage = (Stage) areaItens.getScene().getWindow();
        stage.close();
    }
}
