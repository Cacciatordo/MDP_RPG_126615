package it.unicam.cs.mpgc.rpg126615.model.torre;

import java.util.List;

public class Torre {

    private static final double LIVELLI_PER_PIANO = 0.55;
    private static final double LIVELLI_PER_RADICE_DEL_PIANO = 0.3;
    private static final double LIVELLO_BASE = 0.2;

    private final List<PianoTorre> piani;

    public Torre(List<PianoTorre> piani) {
        this.piani = List.copyOf(piani);
    }

    public int getNumeroPiani() {
        return piani.size();
    }

    public PianoTorre getPiano(int numero) {
        return piani.get(numero - 1);
    }

    public static int getLivelloConsigliato(int numeroPiano) {
        double stima = LIVELLI_PER_PIANO * numeroPiano
                + LIVELLI_PER_RADICE_DEL_PIANO * Math.sqrt(numeroPiano)
                + LIVELLO_BASE;
        return Math.max(1, (int) Math.round(stima));
    }
}