package it.unicam.cs.mpgc.rpg126615.model.carta;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    public static final int CAPIENZA_MASSIMA = 5;

    private final List<Carta> carte = new ArrayList<>();

    public boolean isPiena() {
        return carte.size() >= CAPIENZA_MASSIMA;
    }

    public int getDimensione() {
        return carte.size();
    }

    public List<Carta> getCarte() {
        return carte;
    }

    public boolean aggiungiCarta(Carta carta) {
        if (isPiena()) {
            return false;
        }
        carte.add(carta);
        return true;
    }

    public void rimuoviCarta(Carta carta) {
        carte.remove(carta);
    }

    public void svuota() {
        carte.clear();
    }
}