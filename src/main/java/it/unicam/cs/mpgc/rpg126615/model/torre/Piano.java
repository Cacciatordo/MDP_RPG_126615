package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class Piano {

    private final int numero;
    private final Nemico nemico;

    public Piano(int numero, Nemico nemico) {
        this.numero = numero;
        this.nemico = nemico;
    }

    public int getNumero() {
        return numero;
    }

    public Nemico getNemico() {
        return nemico;
    }
}