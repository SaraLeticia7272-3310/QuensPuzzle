package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.MainApp;
import javafx.fxml.FXML;

public class CreditosController {

    @FXML
    private void aoClicarVoltar() {
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
