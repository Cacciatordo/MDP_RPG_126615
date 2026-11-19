package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import java.util.Objects;
import java.util.function.IntConsumer;

public record AzioniBattaglia(Runnable attacca, Runnable terminaTurno, IntConsumer giocaCarta, Runnable esci) {

    public AzioniBattaglia {
        Objects.requireNonNull(attacca, "L'azione di attacco non può essere nulla");
        Objects.requireNonNull(terminaTurno, "L'azione di fine turno non può essere nulla");
        Objects.requireNonNull(giocaCarta, "L'azione di gioco della carta non può essere nulla");
        Objects.requireNonNull(esci, "L'azione di uscita non può essere nulla");
    }
}