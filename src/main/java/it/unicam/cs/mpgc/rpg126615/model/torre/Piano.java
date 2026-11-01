package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class Piano {

    private final int numero;
    private final TipoPiano tipo;
    private final Nemico nemico;

    public Piano(int numero, TipoPiano tipo, Nemico nemico) {
        this.numero = numero;
        this.tipo = tipo;
        this.nemico = nemico;
    }

    public int getNumero() {
        return numero;
    }

    public TipoPiano getTipo() {
        return tipo;
    }

    public Nemico getNemico() {
        return nemico;
    }
}