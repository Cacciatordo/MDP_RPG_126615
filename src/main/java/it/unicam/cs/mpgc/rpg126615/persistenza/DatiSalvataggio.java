package it.unicam.cs.mpgc.rpg126615.persistenza;

import it.unicam.cs.mpgc.rpg126615.model.entita.StatoGiocatore;

import java.util.List;

public record DatiSalvataggio(int pianoCorrente, StatoGiocatore giocatore, List<DatiPiano> piani) {
}