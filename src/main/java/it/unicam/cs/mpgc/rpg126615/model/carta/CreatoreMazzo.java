package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

import java.util.List;

public interface CreatoreMazzo {

    List<Carta> creaMazzoIniziale(Livellabile riferimento);
}