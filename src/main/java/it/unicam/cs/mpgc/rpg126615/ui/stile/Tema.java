package it.unicam.cs.mpgc.rpg126615.ui.stile;

import javafx.scene.text.Font;

import java.io.InputStream;

public final class Tema {

    private static final String FILE_FONT = "/assets/font/PressStart2P-Regular.ttf";
    private static final double DIMENSIONE_CARICAMENTO_FONT = 12;

    public static final String FONT = caricaFont();

    public static final String SFONDO = "#0a0608";
    public static final String PANNELLO = "#1c1410";
    public static final String BORDO = "#6b6058";

    public static final String TESTO = "#f5ede0";
    public static final String TESTO_SECONDARIO = "#b8a898";

    public static final String ORO = "#fbbf24";
    public static final String VERDE = "#22c55e";

    private Tema() {
    }

    private static String caricaFont() {
        InputStream file = Tema.class.getResourceAsStream(FILE_FONT);
        if (file == null) {
            throw new IllegalStateException("Font non trovato: " + FILE_FONT);
        }
        Font font = Font.loadFont(file, DIMENSIONE_CARICAMENTO_FONT);
        if (font == null) {
            throw new IllegalStateException("Font non valido: " + FILE_FONT);
        }
        return font.getFamily();
    }
}