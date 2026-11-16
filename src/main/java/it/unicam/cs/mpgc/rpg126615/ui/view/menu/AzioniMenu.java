package it.unicam.cs.mpgc.rpg126615.ui.view.menu;

import java.util.Objects;
import java.util.function.BiConsumer;

public record AzioniMenu(BiConsumer<String, Integer> nuovaPartita, Runnable caricaPartita) {

    public AzioniMenu {
        Objects.requireNonNull(nuovaPartita, "L'azione di nuova partita non può essere nulla");
        Objects.requireNonNull(caricaPartita, "L'azione di caricamento non può essere nulla");
    }
}