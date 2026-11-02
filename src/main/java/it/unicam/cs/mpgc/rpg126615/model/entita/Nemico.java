package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Nemico extends EntitaAstratta {

    private static final String PREFISSO_NOME = "Mostro del Piano ";

    private final int esperienzaData;

    public Nemico(int piano) {
        this(PREFISSO_NOME + validaPiano(piano),
                30 + piano * 10,
                5 + piano * 2,
                20 + piano * 5);
    }

    protected Nemico(String nome, int hpMax, int attaccoBase, int esperienzaData) {
        super(nome, hpMax, attaccoBase);
        this.esperienzaData = esperienzaData;
    }

    public int getEsperienzaData() {
        return esperienzaData;
    }

    protected static int validaPiano(int piano) {
        if (piano <= 0) {
            throw new IllegalArgumentException("Il numero del piano deve essere maggiore di zero: " + piano);
        }
        return piano;
    }
}