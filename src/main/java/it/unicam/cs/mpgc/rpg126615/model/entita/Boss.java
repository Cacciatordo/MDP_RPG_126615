package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Boss extends Nemico {

    private static final String PREFISSO_NOME = "Guardiano del Piano ";
    private static final double HP_BASE = 45.0;
    private static final double HP_PER_PIANO = 12.0;
    private static final double MOLTIPLICATORE_HP = 1.957;
    private static final double ATTACCO_BASE = 8.0;
    private static final double ATTACCO_PER_PIANO = 2.8;
    private static final double ARMATURA_PER_PIANO = 0.93;
    private static final int ARMATURA_MINIMA = 1;
    private static final int MOLTIPLICATORE_ESPERIENZA = 3;

    public Boss(int piano) {
        this(PREFISSO_NOME + validaPiano(piano),
                (int) Math.round((HP_BASE + piano * HP_PER_PIANO) * MOLTIPLICATORE_HP),
                (int) Math.round(ATTACCO_BASE + piano * ATTACCO_PER_PIANO),
                Math.max(ARMATURA_MINIMA, (int) Math.round(piano * ARMATURA_PER_PIANO)),
                calcolaEsperienza(piano) * MOLTIPLICATORE_ESPERIENZA);
    }

    protected Boss(String nome, int hpMax, int attaccoBase, int armatura, int esperienzaData) {
        super(nome, hpMax, attaccoBase, armatura, esperienzaData);
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}