package it.unicam.cs.mpgc.rpg126615.model.persistenza;

import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.Torre;

import java.util.ArrayList;
import java.util.List;

public final class MappaturaPiani {

    private MappaturaPiani() {
    }

    public static List<DatiPiano> creaDati(Torre torre) {
        List<DatiPiano> datiPiani = new ArrayList<>();
        for (int numero = 1; numero <= torre.getNumeroPiani(); numero++) {
            PianoTorre piano = torre.getPiano(numero);
            String informazione = piano.haNemico() ? piano.getNemico().getNome() : "";
            datiPiani.add(new DatiPiano(piano.getTipo(), isSuperato(piano), informazione));
        }
        return datiPiani;
    }

    public static boolean corrisponde(Torre torre, List<DatiPiano> datiPiani) {
        if (datiPiani.size() != torre.getNumeroPiani()) {
            return false;
        }
        for (int indice = 0; indice < datiPiani.size(); indice++) {
            if (datiPiani.get(indice).tipo() != torre.getPiano(indice + 1).getTipo()) {
                return false;
            }
        }
        return true;
    }

    public static void ripristina(Torre torre, List<DatiPiano> datiPiani) {
        for (int indice = 0; indice < datiPiani.size(); indice++) {
            ripristinaPiano(torre.getPiano(indice + 1), datiPiani.get(indice).completato());
        }
    }

    private static boolean isSuperato(PianoTorre piano) {
        return piano.isCompletato() || (piano.haNemico() && !piano.getNemico().isVivo());
    }

    private static void ripristinaPiano(PianoTorre piano, boolean superato) {
        if (piano.haNemico()) {
            piano.getNemico().impostaSconfitto(superato);
        } else if (superato) {
            piano.ripristinaCompletato();
        }
    }
}