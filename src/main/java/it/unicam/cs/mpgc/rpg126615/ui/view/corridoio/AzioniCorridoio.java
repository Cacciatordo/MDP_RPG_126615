package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import java.util.function.DoubleConsumer;

public record AzioniCorridoio(DoubleConsumer suCombatti, Runnable suProssimoPiano, Runnable suPianoPrecedente,
                              Runnable suMenu, Runnable suPartitaCaricata) {
}