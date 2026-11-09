package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.TipoCarta;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;

public final class StileCarta {

    private static final String BLU = "#3b82f6";
    private static final String ARANCIONE = "#f97316";
    private static final String VIOLA = "#a855f7";

    private StileCarta() {
    }

    public static String coloreDi(TipoCarta tipo) {
        return switch (tipo) {
            case POTENZIAMENTO_ATTACCO -> Tema.ROSSO;
            case POTENZIAMENTO_DIFESA -> BLU;
            case INDEBOLIMENTO_ATTACCO -> ARANCIONE;
            case INDEBOLIMENTO_DIFESA -> VIOLA;
            case CURA -> Tema.VERDE;
            case PESCA -> Tema.ORO;
        };
    }
}