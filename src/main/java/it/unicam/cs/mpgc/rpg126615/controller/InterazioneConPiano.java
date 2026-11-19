package it.unicam.cs.mpgc.rpg126615.controller;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;

import java.util.List;

public class InterazioneConPiano {

    private static final double DISTANZA_MASSIMA = 45;

    private final Partita partita;
    private final double centroEntita;
    private final List<TipoPiano> tipi;

    public InterazioneConPiano(Partita partita, double centroEntita, List<TipoPiano> tipi) {
        this.partita = partita;
        this.centroEntita = centroEntita;
        this.tipi = tipi;
    }

    public boolean isDisponibile(double centroEroe) {
        PianoTorre piano = this.partita.getPianoCorrente();
        boolean daUsare = this.tipi.contains(piano.getTipo()) && !piano.isCompletato();
        return daUsare && Math.abs(centroEroe - this.centroEntita) <= DISTANZA_MASSIMA;
    }

    public String esegui() {
        return this.partita.entraNelPianoCorrente();
    }
}