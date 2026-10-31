package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.ArrayList;
import java.util.List;

public class Torre {

    private static final int NUMERO_PIANI = 20;

    private final List<Piano> piani = new ArrayList<>();

    public Torre() {
        for (int numero = 1; numero <= NUMERO_PIANI; numero++) {
            Nemico nemico = new Nemico("Mostro del Piano " + numero,
                    30 + numero * 10, 5 + numero * 2, 20 + numero * 5);
            piani.add(new Piano(numero, nemico));
        }
    }

    public int getNumeroPiani() {
        return piani.size();
    }

    public Piano getPiano(int numero) {
        return piani.get(numero - 1);
    }
}