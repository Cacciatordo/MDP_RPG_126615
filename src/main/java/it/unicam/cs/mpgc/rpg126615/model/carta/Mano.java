package it.unicam.cs.mpgc.rpg126615.model.carta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Mano {

    public static final int CAPIENZA_MASSIMA = 5;

    private final List<Carta> carte = new ArrayList<>();

    public boolean aggiungiCarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException("La carta non può essere nulla");
        }
        if (isPiena()) {
            return false;
        }
        carte.add(carta);
        return true;
    }

    public boolean rimuoviCarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException("La carta da rimuovere non può essere nulla");
        }
        return carte.remove(carta);
    }

    public boolean pescaDa(Mazzo mazzo) {
        if (mazzo == null) {
            throw new IllegalArgumentException("Il mazzo non può essere nullo");
        }
        if (isPiena()) {
            return false;
        }
        Optional<Carta> pescata = mazzo.pesca();
        pescata.ifPresent(carte::add);
        return pescata.isPresent();
    }

    public List<Carta> getCarte() {
        return Collections.unmodifiableList(carte);
    }

    public int getDimensione() {
        return carte.size();
    }

    public boolean isPiena() {
        return carte.size() >= CAPIENZA_MASSIMA;
    }

    public void svuota() {
        carte.clear();
    }
}