package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.model.Jogador;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller do Ranking.
 * Mostra o top 10 e permite exportar para CSV ou TXT.
 */
public class RankingController implements Initializable {

    @FXML private TextArea areaRanking;
    @FXML private Label labelMensagem;

    private List<Jogador> listaRanking;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarRanking();
    }

    private void carregarRanking() {
        listaRanking = new JogadorDAO().getRanking();

        StringBuilder sb = new StringBuilder();
        sb.append("  Pos   Nome                  Pontos    Nível\n");
        sb.append("  ─────────────────────────────────────────\n");

        if (listaRanking.isEmpty()) {
            sb.append("\n  Nenhum jogador cadastrado ainda.\n");
            sb.append("  Jogue e apareça aqui!");
        } else {
            for (int i = 0; i < listaRanking.size(); i++) {
                Jogador j = listaRanking.get(i);
                String medalha = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  " + (i + 1) + ".";
                sb.append(String.format("  %-4s  %-20s  %6d    %dx%d\n",
                        medalha, j.getNome(), j.getPontuacaoTotal(),
                        j.getNivelMaximo(), j.getNivelMaximo()));
            }
        }

        areaRanking.setText(sb.toString());
    }

    /**
     * Exporta ranking como CSV.
     */
    @FXML
    private void aoClicarExportarCSV() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Salvar Ranking CSV");
        chooser.setInitialFileName("ranking_queens.csv");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));

        File arquivo = chooser.showSaveDialog(MainApp.getStagePrincipal());
        if (arquivo != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                pw.println("Posicao,Nome,Pontuacao,NivelMaximo");
                for (int i = 0; i < listaRanking.size(); i++) {
                    Jogador j = listaRanking.get(i);
                    pw.println((i + 1) + "," + j.getNome() + ","
                            + j.getPontuacaoTotal() + "," + j.getNivelMaximo());
                }
                labelMensagem.setText("✓ CSV salvo em: " + arquivo.getName());
            } catch (IOException e) {
                labelMensagem.setText("Erro ao salvar CSV.");
            }
        }
    }

    /**
     * Exporta ranking como TXT formatado.
     */
    @FXML
    private void aoClicarExportarTXT() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Salvar Ranking TXT");
        chooser.setInitialFileName("ranking_queens.txt");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT", "*.txt"));

        File arquivo = chooser.showSaveDialog(MainApp.getStagePrincipal());
        if (arquivo != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                pw.print(areaRanking.getText());
                labelMensagem.setText("✓ TXT salvo em: " + arquivo.getName());
            } catch (IOException e) {
                labelMensagem.setText("Erro ao salvar TXT.");
            }
        }
    }

    @FXML
    private void aoClicarVoltar() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
