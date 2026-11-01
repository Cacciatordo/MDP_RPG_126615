package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.ArrayList;
import java.util.List;

public class Torre {

    private static final int NUMERO_PIANI = 20;

    private final List<Piano> piani = new ArrayList<>();

    public Torre() {
        for (int numero = 1; numero <= NUMERO_PIANI; numero++) {
            if (isNumeroTriangolare(numero)) {
                piani.add(new Piano(numero, TipoPiano.BOSS, new Nemico("Boss del Piano " + numero,
                        60 + numero * 20, 8 + numero * 2, 60 + numero * 10)));
            } else {
                piani.add(new Piano(numero, TipoPiano.COMBATTIMENTO, new Nemico("Mostro del Piano " + numero,
                        30 + numero * 10, 5 + numero * 2, 20 + numero * 5)));
            }
        }
    }

    public int getNumeroPiani() {
        return piani.size();
    }

    public Piano getPiano(int numero) {
        return piani.get(numero - 1);
    }

    public static boolean isNumeroTriangolare(int n) {
        if (n <= 0) {
            return false;
        }
        int discriminante = 8 * n + 1;
        int radice = (int) Math.sqrt(discriminante);
        return radice * radice == discriminante;
    }
}