package it.unicam.cs.mpgc.rpg126615.persistenza;

import it.unicam.cs.mpgc.rpg126615.model.entita.StatoGiocatore;

import java.util.List;

public record DatiSalvataggio(int pianoCorrente, StatoGiocatore giocatore, List<DatiPiano> piani) {

    public DatiSalvataggio {
        if (giocatore == null) {
            throw new IllegalArgumentException("Lo stato del giocatore non può essere nullo");
        }
        if (piani == null || piani.isEmpty()) {
            throw new IllegalArgumentException("La torre salvata deve contenere almeno un piano");
        }
        if (pianoCorrente <= 0 || pianoCorrente > piani.size()) {
            throw new IllegalArgumentException("Il piano corrente (" + pianoCorrente
                    + ") non è tra 1 e " + piani.size());
        }
        piani = List.copyOf(piani);
    }
}