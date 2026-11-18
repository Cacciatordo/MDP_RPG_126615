package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Nemico extends EntitaAstratta {

    private static final String PREFISSO_NOME = "Mostro del Piano ";
    private static final int HP_BASE = 40;
    private static final int HP_PER_PIANO = 12;
    private static final int ATTACCO_BASE = 6;
    private static final int ATTACCO_PER_PIANO = 2;
    private static final int PIANI_PER_PUNTO_ARMATURA = 2;

    public static final int ESPERIENZA_BASE = 30;
    public static final int ESPERIENZA_PER_PIANO = 15;

    private final int esperienzaData;

    public Nemico(int piano) {
        this(PREFISSO_NOME + validaPiano(piano),
                HP_BASE + piano * HP_PER_PIANO,
                ATTACCO_BASE + piano * ATTACCO_PER_PIANO,
                (piano - 1) / PIANI_PER_PUNTO_ARMATURA,
                calcolaEsperienza(piano));
    }

    protected Nemico(String nome, int hpMax, int attaccoBase, int armatura, int esperienzaData) {
        super(nome, hpMax, attaccoBase, armatura);
        this.esperienzaData = esperienzaData;
    }

    public int getEsperienzaData() {
        return esperienzaData;
    }

    @Override
    public int attacca(Combattente bersaglio) {
        int danno = Math.max(0, getAttacco() - bersaglio.getDifesa());
        bersaglio.subisciDanno(danno);
        return danno;
    }

    protected static int calcolaEsperienza(int piano) {
        return ESPERIENZA_BASE + piano * ESPERIENZA_PER_PIANO;
    }

    protected static int validaPiano(int piano) {
        if (piano <= 0) {
            throw new IllegalArgumentException("Il numero del piano deve essere maggiore di zero: " + piano);
        }
        return piano;
    }
}