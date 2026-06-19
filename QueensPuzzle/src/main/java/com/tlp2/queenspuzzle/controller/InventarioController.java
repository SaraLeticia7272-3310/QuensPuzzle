package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.model.Item;
import static com.tlp2.queenspuzzle.model.Item.Tipo.AMULETO_PONTOS;
import static com.tlp2.queenspuzzle.model.Item.Tipo.CHAVE_DICA;
import static com.tlp2.queenspuzzle.model.Item.Tipo.POCAO_TEMPO;
import com.tlp2.queenspuzzle.model.SessaoJogo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller do Inventário — abre como Stage separado durante o Gameplay.
 * Itens usáveis podem ser clicados para aplicar efeito imediato no jogo.
 */
public class InventarioController implements Initializable {

    @FXML private Label labelTitulo;
    @FXML private VBox listaItens;
    @FXML private Label labelMensagem;

    // Callback chamado pelo GameplayController para receber o efeito do item usado
    private Consumer<Item> onItemUsado;

    public void setOnItemUsado(Consumer<Item> callback) {
        this.onItemUsado = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SessaoJogo sessao = SessaoJogo.getInstance();

        String nome = sessao.getJogadorAtual() != null
                ? sessao.getJogadorAtual().getNome() : "Jogador";
        labelTitulo.setText("🎒 Inventário de " + nome);

        renderizarItens();
    }

    private void renderizarItens() {
        SessaoJogo sessao = SessaoJogo.getInstance();
        List<Item> itens = sessao.getInventario();

        listaItens.getChildren().clear();

        if (itens.isEmpty()) {
            Label vazio = new Label("Inventário vazio.\nCompre itens na Loja durante o jogo!");
            vazio.setStyle("-fx-text-fill: #9b80c4; -fx-font-size: 13; -fx-font-style: italic;");
            vazio.setAlignment(Pos.CENTER);
            listaItens.getChildren().add(vazio);
            return;
        }

        for (Item item : itens) {
            listaItens.getChildren().add(criarCardItem(item));
        }
    }

    private HBox criarCardItem(Item item) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10, 14, 10, 14));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #d8b7fe;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;"
        );

        // Emoji do item
        Label emoji = new Label(item.getEmoji());
        emoji.setStyle("-fx-font-size: 26;");

        // Info do item
        VBox info = new VBox(2);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nomeLbl = new Label(item.getNome() + "  ×" + item.getQuantidade());
        nomeLbl.setStyle("-fx-text-fill: #4a3060; -fx-font-size: 13; -fx-font-weight: bold;");

        Label descLbl = new Label(item.getDescricao());
        descLbl.setStyle("-fx-text-fill: #9b80c4; -fx-font-size: 10;");

        info.getChildren().addAll(nomeLbl, descLbl);

        card.getChildren().addAll(emoji, info);

      // Botão "Usar" apenas para itens usáveis
        if (item.isUsavel()) {
            Button btnUsar = new Button("Usar");
            btnUsar.setStyle(
                "-fx-background-color: #bbc5ff;" +
                "-fx-text-fill: #3a2080;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #8b9fe8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 12;"
            );
            btnUsar.setOnAction(e -> usarItem(item, nomeLbl));
            card.getChildren().add(btnUsar);
        } else {
            Label trofeulbl = new Label("Troféu");
            trofeulbl.setStyle("-fx-text-fill: #c8a000; -fx-font-size: 11; -fx-font-style: italic;");
            card.getChildren().add(trofeulbl);
        }

        return card;
    }

    private void usarItem(Item item, Label nomeLbl) {
        SessaoJogo sessao = SessaoJogo.getInstance();

        if (item.getQuantidade() <= 0) {
            labelMensagem.setText("Sem unidades deste item!");
            labelMensagem.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        // Diminui quantidade
        item.setQuantidade(item.getQuantidade() - 1);

        // Remove do inventário se zerou
        if (item.getQuantidade() == 0) {
            sessao.getInventario().remove(item);
        }

        // Dispara o efeito no GameplayController
        if (onItemUsado != null) {
            onItemUsado.accept(item);
        }

        // Mensagem de feedback
        switch (item.getTipo()) {
            case POCAO_TEMPO:
                labelMensagem.setText("✓ +30 segundos adicionados!");
                break;
            case CHAVE_DICA:
                labelMensagem.setText("✓ +1 dica disponível!");
                break;
            case AMULETO_PONTOS:
                labelMensagem.setText("✓ +100 pontos adicionados!");
                break;
            default:
                labelMensagem.setText("✓ Item usado!");
        }
        labelMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Atualiza a lista
        renderizarItens();
    }

    @FXML
    private void aoClicarFechar() {
        Stage stage = (Stage) listaItens.getScene().getWindow();
        stage.close();
    }
}
