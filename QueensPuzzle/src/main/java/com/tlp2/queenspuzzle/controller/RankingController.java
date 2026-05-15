package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.model.Jogador;
import com.tlp2.queenspuzzle.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
        sb.append("══════════════════════════════\n");
        sb.append("       RANKING - TOP 10       \n");
        sb.append("══════════════════════════════\n\n");

        if (listaRanking.isEmpty()) {
            sb.append("Nenhum jogador cadastrado ainda.");
        } else {
            for (int i = 0; i < listaRanking.size(); i++) {
                Jogador j = listaRanking.get(i);
                sb.append(String.format("%2d. %-20s %6d pts   Nv.%d\n",
                        i + 1, j.getNome(), j.getPontuacaoTotal(), j.getNivelMaximo()));
            }
        }

        areaRanking.setText(sb.toString());
    }

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
