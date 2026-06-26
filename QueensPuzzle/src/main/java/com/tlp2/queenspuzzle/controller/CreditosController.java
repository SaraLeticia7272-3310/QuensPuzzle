package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.MainApp;
import com.tlp2.queenspuzzle.SoundManager;
import javafx.fxml.FXML;

/**
 *
 * @author saral
 */

public class CreditosController {

    @FXML
    private void aoClicarVoltar() {
        SoundManager.botao();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
