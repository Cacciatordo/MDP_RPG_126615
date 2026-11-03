package it.unicam.cs.mpgc.rpg126615.persistenza;

import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;

public record DatiPiano(TipoPiano tipo, boolean completato, String informazione) {
}