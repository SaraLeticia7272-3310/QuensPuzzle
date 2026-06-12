package com.tlp2.queenspuzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage stagePrincipal;

    @Override
    public void start(Stage stage) throws Exception {
        stagePrincipal = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml"));

        Scene scene = new Scene(root, 720, 720);
        stage.setTitle("Queens Puzzle");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void trocarTela(String caminhoFxml) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(caminhoFxml));
            stagePrincipal.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println("Erro ao trocar tela: " + e.getMessage());
        }
    }

    public static Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
