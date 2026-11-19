package it.unicam.cs.mpgc.rpg126615.controller;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.menu.AzioniMenu;
import it.unicam.cs.mpgc.rpg126615.view.menu.VistaIniziale;
import javafx.scene.Scene;

import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ControllerMenu {

    private static final String MESSAGGIO_PIANI_NON_VALIDI = "Inserisci un numero intero di piani da "
            + Partita.PIANI_MINIMI + " a " + Partita.PIANI_MASSIMI + ".";

    private final DialoghiSalvataggio dialoghi;
    private final BiConsumer<String, Integer> suNuovaPartita;
    private final Consumer<DatiSalvataggio> suPartitaCaricata;
    private final VistaIniziale vista;

    public ControllerMenu(DimensioniFinestra dimensioni, DialoghiSalvataggio dialoghi,
                          BiConsumer<String, Integer> suNuovaPartita, Consumer<DatiSalvataggio> suPartitaCaricata) {
        this.dialoghi = dialoghi;
        this.suNuovaPartita = suNuovaPartita;
        this.suPartitaCaricata = suPartitaCaricata;
        this.vista = new VistaIniziale(dimensioni, new AzioniMenu(
                this::iniziaNuovaPartita,
                this::scegliPartitaDaCaricare));
    }

    public Scene getScena() {
        return this.vista.getScena();
    }

    private void iniziaNuovaPartita(String nomeEroe, String testoPiani) {
        OptionalInt piani = leggiNumeroPiani(testoPiani);
        if (piani.isEmpty()) {
            this.vista.mostraErrore(MESSAGGIO_PIANI_NON_VALIDI);
            return;
        }
        this.vista.cancellaErrore();
        this.suNuovaPartita.accept(nomeEroe, piani.getAsInt());
    }

    private void scegliPartitaDaCaricare() {
        this.dialoghi.scegliDaCaricare().ifPresent(this.suPartitaCaricata);
    }

    private static OptionalInt leggiNumeroPiani(String testoInserito) {
        String testo = testoInserito.trim();
        if (testo.isEmpty()) {
            return OptionalInt.of(Partita.PIANI_PREDEFINITI);
        }
        try {
            int piani = Integer.parseInt(testo);
            return Partita.isNumeroPianiValido(piani) ? OptionalInt.of(piani) : OptionalInt.empty();
        } catch (NumberFormatException errore) {
            return OptionalInt.empty();
        }
    }
}