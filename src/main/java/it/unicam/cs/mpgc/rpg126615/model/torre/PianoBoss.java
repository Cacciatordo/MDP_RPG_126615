package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class PianoBoss extends PianoAstratto {

    public PianoBoss(int numero, Nemico nemico) {
        super(numero, TipoPiano.BOSS, nemico);
    }
}