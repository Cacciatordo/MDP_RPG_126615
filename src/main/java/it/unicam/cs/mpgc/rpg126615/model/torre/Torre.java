package it.unicam.cs.mpgc.rpg126615.model.torre;

import java.util.List;

public class Torre {

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
}