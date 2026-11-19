package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import java.util.Objects;

public record EventiCorridoio(Runnable eroeSpostato, Runnable sfidaRichiesta, Runnable salvataggioRichiesto,
                              Runnable caricamentoRichiesto, Runnable menuRichiesto) {

    public EventiCorridoio {
        Objects.requireNonNull(eroeSpostato, "L'evento di spostamento non può essere nullo");
        Objects.requireNonNull(sfidaRichiesta, "L'evento di sfida non può essere nullo");
        Objects.requireNonNull(salvataggioRichiesto, "L'evento di salvataggio non può essere nullo");
        Objects.requireNonNull(caricamentoRichiesto, "L'evento di caricamento non può essere nullo");
        Objects.requireNonNull(menuRichiesto, "L'evento di ritorno al menu non può essere nullo");
    }
}