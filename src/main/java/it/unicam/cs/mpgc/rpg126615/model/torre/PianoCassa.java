package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public class PianoCassa extends PianoAstratto {

    private final ContenutoCassa contenuto;

    public PianoCassa(int numero, ContenutoCassa contenuto) {
        super(numero, TipoPiano.CASSA);
        this.contenuto = contenuto;
    }

    @Override
    protected String eseguiEffetto(Giocatore giocatore) {
        segnaCompletato();
        return contenuto.applica(giocatore, getNumero());
    }
}