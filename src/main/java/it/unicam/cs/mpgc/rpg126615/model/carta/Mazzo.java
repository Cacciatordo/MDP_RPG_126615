package it.unicam.cs.mpgc.rpg126615.model.carta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {

    private static final int COSTO_PA_STANDARD = 1;
    private static final int VALORE_BASE_STATISTICA = 10;
    private static final int COPIE_STANDARD = 2;
    private static final int COPIE_PESCA = 1;

    private final List<Carta> pilaPesca = new ArrayList<>();
    private final List<Carta> pilaScarti = new ArrayList<>();

    public Mazzo() {
        pilaPesca.addAll(creaCarteIniziali());
        Collections.shuffle(pilaPesca);
    }

    public Carta pesca() {
        if (pilaPesca.isEmpty()) {
            riciclaScarti();
        }
        if (pilaPesca.isEmpty()) {
            return null;
        }
        return pilaPesca.remove(pilaPesca.size() - 1);
    }

    public void scarta(Carta carta) {
        pilaScarti.add(carta);
    }

    public int getCarteRimanenti() {
        return pilaPesca.size();
    }

    private void riciclaScarti() {
        pilaPesca.addAll(pilaScarti);
        pilaScarti.clear();
        Collections.shuffle(pilaPesca);
    }

    private List<Carta> creaCarteIniziali() {
        List<Carta> carte = new ArrayList<>();
        for (int i = 0; i < COPIE_STANDARD; i++) {
            carte.add(new CartaBuff("Potenziamento Attacco",
                    "Il tuo attacco sale di " + VALORE_BASE_STATISTICA + " per lo scontro",
                    COSTO_PA_STANDARD, TipoCarta.POTENZIAMENTO_ATTACCO, VALORE_BASE_STATISTICA));
            carte.add(new CartaBuff("Barriera Difensiva",
                    "Il tuo scudo sale di " + VALORE_BASE_STATISTICA + " per lo scontro",
                    COSTO_PA_STANDARD, TipoCarta.POTENZIAMENTO_DIFESA, VALORE_BASE_STATISTICA));
            carte.add(new CartaDebuff("Indebolimento",
                    "Attacco nemico -" + VALORE_BASE_STATISTICA + " per lo scontro",
                    COSTO_PA_STANDARD, TipoCarta.INDEBOLIMENTO_ATTACCO, VALORE_BASE_STATISTICA));
            carte.add(new CartaDebuff("Spacca-Armatura",
                    "Armatura nemica -" + VALORE_BASE_STATISTICA + " per lo scontro",
                    COSTO_PA_STANDARD, TipoCarta.INDEBOLIMENTO_DIFESA, VALORE_BASE_STATISTICA));
        }
        for (int i = 0; i < COPIE_PESCA; i++) {
            carte.add(new CartaPesca("Pesca Rapida", "Pesca una carta dal mazzo", COSTO_PA_STANDARD, 1));
        }
        return carte;
    }
}