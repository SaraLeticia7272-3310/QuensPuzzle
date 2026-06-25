package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.model.Jogador;
import com.tlp2.queenspuzzle.model.LinhaRanking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author saral
 */

public class RankingController implements Initializable {

    @FXML
    private TableView<LinhaRanking> tabelaRanking;

    @FXML
    private TableColumn<LinhaRanking, String> colPosicao;

    @FXML
    private TableColumn<LinhaRanking, String> colNome;

    @FXML
    private TableColumn<LinhaRanking, Integer> colPontos;

    @FXML
    private TableColumn<LinhaRanking, String> colNivel;

    @FXML
    private Label labelMensagem;

    private List<Jogador> listaRanking;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        carregarRanking();
    }

    private void carregarRanking() {
        listaRanking = new JogadorDAO().getRanking();

        ObservableList<LinhaRanking> dados = FXCollections.observableArrayList();

        for (int i = 0; i < listaRanking.size(); i++) {
            Jogador j = listaRanking.get(i);
            String posicao;
            switch (i) {
                case 0:
                    posicao = "🥇";
                    break;
                case 1:
                    posicao = "🥈";
                    break;
                case 2:
                    posicao = "🥉";
                    break;
                default:
                    posicao = String.valueOf(i + 1);
                    break;
            }
            
            dados.add(new LinhaRanking(
                    posicao,
                    j.getNome(),
                    j.getPontuacaoTotal(),
                    j.getNivelMaximo() + "x" + j.getNivelMaximo()
            ));
        }

        tabelaRanking.setItems(dados);
    }

    @FXML
    private void aoClicarExportarCSV() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Salvar Ranking CSV");
        chooser.setInitialFileName("ranking_queens.csv");

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

        File arquivo = chooser.showSaveDialog(MainApp.getStagePrincipal());

        if (arquivo != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                pw.println("Posicao,Nome,Pontuacao,Nivel");
                for (int i = 0; i < listaRanking.size(); i++) {
                    Jogador j = listaRanking.get(i);
                    pw.println(
                            (i + 1) + "," +
                            j.getNome() + "," +
                            j.getPontuacaoTotal() + "," +
                            j.getNivelMaximo()
                    );
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

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));

        File arquivo = chooser.showSaveDialog(MainApp.getStagePrincipal());

        if (arquivo != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
                pw.println("RANKING");
                for (int i = 0; i < listaRanking.size(); i++) {
                    Jogador j = listaRanking.get(i);
                    pw.println(
                            (i + 1) + " - " +
                            j.getNome() + " - " +
                            j.getPontuacaoTotal() + " pontos - Nível " +
                            j.getNivelMaximo() + "x" +
                            j.getNivelMaximo()
                    );
                }
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