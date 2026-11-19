package it.unicam.cs.mpgc.rpg126615.view.stile;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public final class Componenti {

    private Componenti() {
    }

    public static Label etichetta(String testo, int dimensione, String colore) {
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }

    public static Label etichetta(int dimensione, String colore) {
        return etichetta("", dimensione, colore);
    }

    public static Label etichettaCentrata(String testo, int dimensione, String colore, double larghezzaMassima) {
        Label etichetta = etichetta(testo, dimensione, colore);
        etichetta.setWrapText(true);
        etichetta.setTextAlignment(TextAlignment.CENTER);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setMaxWidth(larghezzaMassima);
        return etichetta;
    }

    public static void bloccaDimensione(Region regione, double larghezza, double altezza) {
        regione.setMinSize(larghezza, altezza);
        regione.setPrefSize(larghezza, altezza);
        regione.setMaxSize(larghezza, altezza);
    }

    public static void aggiungiEffettoSopra(Button bottone, String stileNormale, String stileSopra) {
        bottone.setStyle(stileNormale);
        bottone.setCursor(Cursor.HAND);
        bottone.setOnMouseEntered(evento -> bottone.setStyle(stileSopra));
        bottone.setOnMouseExited(evento -> bottone.setStyle(stileNormale));
    }
}