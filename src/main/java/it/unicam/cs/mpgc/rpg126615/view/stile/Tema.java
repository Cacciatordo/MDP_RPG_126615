package it.unicam.cs.mpgc.rpg126615.view.stile;

import javafx.scene.text.Font;

import java.io.InputStream;

public final class Tema {

    private static final String FILE_FONT = "/assets/font/PressStart2P-Regular.ttf";
    private static final double DIMENSIONE_CARICAMENTO_FONT = 12;

    public static final String FONT = caricaFont();
    public static final String ERRORE = "#ff6b6b";
    public static final String SFONDO = "#0a0608";
    public static final String PANNELLO = "#1c1410";
    public static final String PANNELLO_SOPRA = "#3d2a1e";
    public static final String BORDO = "#6b6058";

    public static final String TESTO = "#f5ede0";
    public static final String TESTO_SECONDARIO = "#b8a898";

    public static final String ORO = "#fbbf24";
    public static final String VERDE = "#22c55e";
    public static final String ROSSO = "#cc2200";

    public static final String ESPERIENZA = "#a855f7";
    public static final String DIFESA = "#f97316";
    public static final String AVVISO = "#4a1d6e";
    public static final String AVVISO_GRAVE = "#7f1d1d";

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