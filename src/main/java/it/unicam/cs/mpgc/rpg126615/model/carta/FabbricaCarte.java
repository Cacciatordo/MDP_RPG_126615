package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabbricaCarte {

    private static final String VALORE = CartaAstratta.SEGNAPOSTO_VALORE;
    private static final int VALORE_BASE_STATISTICA = 10;
    private static final int COPIE_STANDARD = 2;
    private static final int COPIE_PESCA = 1;

    public List<Carta> creaMazzoIniziale(Livellabile riferimento) {
        List<Carta> carte = new ArrayList<>();
        aggiungiCopie(carte, COPIE_STANDARD, () -> potenziamentoAttacco(riferimento));
        aggiungiCopie(carte, COPIE_STANDARD, () -> barrieraDifensiva(riferimento));
        aggiungiCopie(carte, COPIE_STANDARD, () -> indebolimento(riferimento));
        aggiungiCopie(carte, COPIE_STANDARD, () -> spaccaArmatura(riferimento));
        aggiungiCopie(carte, COPIE_PESCA, () -> pescaRapida(riferimento));
        return carte;
    }

    private static void aggiungiCopie(List<Carta> carte, int copie, Supplier<Carta> nuovaCarta) {
        for (int i = 0; i < copie; i++) {
            carte.add(nuovaCarta.get());
        }
    }

    private static Carta potenziamentoAttacco(Livellabile riferimento) {
        return new CartaBuff("Potenziamento Attacco",
                "Il tuo attacco sale di " + VALORE + " per lo scontro",
                VALORE_BASE_STATISTICA, riferimento, StatisticaBersaglio.ATTACCO);
    }

    private static Carta barrieraDifensiva(Livellabile riferimento) {
        return new CartaBuff("Barriera Difensiva",
                "Il tuo scudo sale di " + VALORE + " per lo scontro",
                VALORE_BASE_STATISTICA, riferimento, StatisticaBersaglio.SCUDO);
    }

    private static Carta indebolimento(Livellabile riferimento) {
        return new CartaDebuff("Indebolimento",
                "Attacco nemico -" + VALORE + " per lo scontro",
                VALORE_BASE_STATISTICA, riferimento, StatisticaBersaglio.ATTACCO);
    }

    private static Carta spaccaArmatura(Livellabile riferimento) {
        return new CartaDebuff("Spacca-Armatura",
                "Armatura nemica -" + VALORE + " per lo scontro",
                VALORE_BASE_STATISTICA, riferimento, StatisticaBersaglio.SCUDO);
    }

    private static Carta pescaRapida(Livellabile riferimento) {
        return new CartaPesca("Pesca Rapida", "Pesca una carta dal mazzo", riferimento);
    }
}