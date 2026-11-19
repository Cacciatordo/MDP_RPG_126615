package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.TipoCarta;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;

public final class StileCarta {

    public static final String COLORE_ATTACCO_BASE = "#ff3311";
    public static final String ICONA_ATTACCO_BASE = "⚔";

    private static final String AZZURRO = "#38bdf8";
    private static final String VIOLA = "#a855f7";
    private static final String ARANCIONE = "#f97316";

    private StileCarta() {
    }

    public static String coloreDi(TipoCarta tipo) {
        return switch (tipo) {
            case POTENZIAMENTO_ATTACCO -> Tema.ORO;
            case POTENZIAMENTO_DIFESA -> AZZURRO;
            case INDEBOLIMENTO_ATTACCO -> VIOLA;
            case INDEBOLIMENTO_DIFESA -> ARANCIONE;
            case CURA -> Tema.VERDE;
            case PESCA -> Tema.TESTO;
        };
    }

    public static String iconaDi(TipoCarta tipo) {
        return switch (tipo) {
            case POTENZIAMENTO_ATTACCO -> "⚔";
            case POTENZIAMENTO_DIFESA -> "🛡";
            case INDEBOLIMENTO_ATTACCO -> "☠";
            case INDEBOLIMENTO_DIFESA -> "💥";
            case CURA -> "✚";
            case PESCA -> "♠";
        };
    }
}