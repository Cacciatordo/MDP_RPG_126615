package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Nemico extends Personaggio {

    private final int esperienzaData;

    public Nemico(String nome, int hpMax, int attaccoBase, int esperienzaData) {
        super(nome, hpMax, attaccoBase);
        this.esperienzaData = esperienzaData;
    }

    public int getEsperienzaData() {
        return esperienzaData;
    }
}