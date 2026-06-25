package com.tlp2.queenspuzzle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ComoJogarController {

    @FXML
    private Button btnFechar;

    @FXML
    private void aoClicarFechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }

}