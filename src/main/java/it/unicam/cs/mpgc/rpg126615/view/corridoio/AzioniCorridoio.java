package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public record AzioniCorridoio(DoubleConsumer suCombatti, Runnable suProssimoPiano, Runnable suPianoPrecedente,
                              Runnable suMenu, Consumer<DatiSalvataggio> suPartitaCaricata) {
}