package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public interface PianoTorre {

    int getNumero();

    TipoPiano getTipo();

    Nemico getNemico();

    boolean haNemico();

    boolean isCompletato();

    void ripristinaCompletato();

    String entra(Giocatore giocatore);
}