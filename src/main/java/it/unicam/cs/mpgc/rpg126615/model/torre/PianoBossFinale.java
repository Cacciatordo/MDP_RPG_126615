package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class PianoBossFinale extends PianoAstratto {

    public PianoBossFinale(int numero, Nemico nemico) {
        super(numero, TipoPiano.BOSS_FINALE, nemico);
    }
}