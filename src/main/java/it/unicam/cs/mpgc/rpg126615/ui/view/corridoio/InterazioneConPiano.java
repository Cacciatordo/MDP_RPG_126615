package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;

public class InterazioneConPiano {

    private static final double DISTANZA_MASSIMA = 110;

    private final Partita partita;
    private final double centroEntita;
    private final TipoPiano tipo;

    public InterazioneConPiano(Partita partita, double centroEntita, TipoPiano tipo) {
        this.partita = partita;
        this.centroEntita = centroEntita;
        this.tipo = tipo;
    }

    public boolean isDisponibile(double centroEroe) {
        PianoTorre piano = this.partita.getPianoCorrente();
        boolean daUsare = piano.getTipo() == this.tipo && !piano.isCompletato();
        return daUsare && Math.abs(centroEroe - this.centroEntita) <= DISTANZA_MASSIMA;
    }

    public String esegui() {
        return this.partita.entraNelPianoCorrente();
    }
}