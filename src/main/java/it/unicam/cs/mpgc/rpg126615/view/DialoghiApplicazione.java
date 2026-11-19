package it.unicam.cs.mpgc.rpg126615.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialoghiApplicazione {

    public void mostraErroreCaricamento(String dettaglio) {
        new Alert(Alert.AlertType.ERROR, "Impossibile caricare il salvataggio: " + dettaglio).showAndWait();
    }

    public boolean confermaRitornoAlMenu() {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION,
                "I progressi non salvati andranno persi.", ButtonType.OK, ButtonType.CANCEL);
        conferma.setTitle("Menu Principale");
        conferma.setHeaderText("Tornare al menu iniziale?");
        return conferma.showAndWait().filter(risposta -> risposta == ButtonType.OK).isPresent();
    }
}