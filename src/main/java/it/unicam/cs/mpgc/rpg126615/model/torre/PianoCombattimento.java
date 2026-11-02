package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class PianoCombattimento extends PianoAstratto {

    public PianoCombattimento(int numero, Nemico nemico) {
        super(numero, TipoPiano.COMBATTIMENTO, nemico);
    }
}