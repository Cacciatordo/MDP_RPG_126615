package it.unicam.cs.mpgc.rpg126615.model.torre;

import java.util.List;

public class Torre {

    private final List<Piano> piani;
    
    public Torre(List<Piano> piani) {
        this.piani = List.copyOf(piani);
    }

    public int getNumeroPiani() {
        return piani.size();
    }

    public Piano getPiano(int numero) {
        return piani.get(numero - 1);
    }
}