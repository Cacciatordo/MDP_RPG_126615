package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;

public class InterazioneCassa {

    private static final double DISTANZA_MASSIMA = 110;

    private final Partita partita;
    private final double centroCassa;

    public InterazioneCassa(Partita partita, double centroCassa) {
        this.partita = partita;
        this.centroCassa = centroCassa;
    }

    public boolean isDisponibile(double centroEroe) {
        PianoTorre piano = this.partita.getPianoCorrente();
        boolean cassaDaAprire = piano.getTipo() == TipoPiano.CASSA && !piano.isCompletato();
        return cassaDaAprire && Math.abs(centroEroe - this.centroCassa) <= DISTANZA_MASSIMA;
    }

    public String apri() {
        return this.partita.entraNelPianoCorrente();
    }
}