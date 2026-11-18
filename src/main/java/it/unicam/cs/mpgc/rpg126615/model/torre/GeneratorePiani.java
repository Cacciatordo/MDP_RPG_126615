package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Boss;
import it.unicam.cs.mpgc.rpg126615.model.entita.BossFinale;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratorePiani {

    private static final double PROBABILITA_RIPOSO = 0.2;
    private static final double PROBABILITA_CASSA = 0.2;

    private static final List<PianoGarantito> PIANI_GARANTITI = elencaPianiGarantiti();

    private final Random random;

    public GeneratorePiani(Random random) {
        this.random = random;
    }

    public List<PianoTorre> generaPiani(int numeroPiani) {
        Map<Integer, PianoGarantito> garantiti = assegnaPianiGarantiti(numeroPiani);
        List<PianoTorre> piani = new ArrayList<>();
        for (int numero = 1; numero <= numeroPiani; numero++) {
            piani.add(creaPiano(numero, numeroPiani, garantiti.get(numero)));
        }
        return piani;
    }

    public static boolean isNumeroTriangolare(int n) {
        if (n <= 0) {
            return false;
        }
        int discriminante = 8 * n + 1;
        int radice = (int) Math.sqrt(discriminante);
        return radice * radice == discriminante;
    }

    private PianoTorre creaPiano(int numero, int numeroPiani, PianoGarantito garantito) {
        if (numero == numeroPiani) {
            return new PianoBossFinale(numero, new BossFinale(numero));
        }
        if (isPianoBoss(numero, numeroPiani)) {
            return new PianoBoss(numero, new Boss(numero));
        }
        if (garantito != null) {
            return creaPianoGarantito(numero, garantito);
        }
        return switch (estraiTipo()) {
            case RIPOSO -> new PianoRiposo(numero);
            case CASSA -> new PianoCassa(numero, scegliContenutoCassa());
            default -> new PianoCombattimento(numero, new Nemico(numero));
        };
    }

    private PianoTorre creaPianoGarantito(int numero, PianoGarantito garantito) {
        return switch (garantito.tipo()) {
            case RIPOSO -> new PianoRiposo(numero);
            case CASSA -> new PianoCassa(numero, garantito.contenuto());
            default -> new PianoCombattimento(numero, new Nemico(numero));
        };
    }

    private Map<Integer, PianoGarantito> assegnaPianiGarantiti(int numeroPiani) {
        List<Integer> pianiLiberi = elencaPianiLiberi(numeroPiani);
        Collections.shuffle(pianiLiberi, this.random);
        Map<Integer, PianoGarantito> assegnati = new HashMap<>();
        int quantita = Math.min(pianiLiberi.size(), PIANI_GARANTITI.size());
        for (int i = 0; i < quantita; i++) {
            assegnati.put(pianiLiberi.get(i), PIANI_GARANTITI.get(i));
        }
        return assegnati;
    }

    private static List<Integer> elencaPianiLiberi(int numeroPiani) {
        List<Integer> liberi = new ArrayList<>();
        for (int numero = 1; numero < numeroPiani; numero++) {
            if (!isPianoBoss(numero, numeroPiani)) {
                liberi.add(numero);
            }
        }
        return liberi;
    }

    private static List<PianoGarantito> elencaPianiGarantiti() {
        List<PianoGarantito> garantiti = new ArrayList<>();
        garantiti.add(new PianoGarantito(TipoPiano.RIPOSO, null));
        for (ContenutoCassa contenuto : ContenutoCassa.values()) {
            garantiti.add(new PianoGarantito(TipoPiano.CASSA, contenuto));
        }
        garantiti.add(new PianoGarantito(TipoPiano.COMBATTIMENTO, null));
        return garantiti;
    }

    private static boolean isPianoBoss(int numero, int numeroPiani) {
        return isNumeroTriangolare(numero) && numero != numeroPiani - 1;
    }

    private TipoPiano estraiTipo() {
        double estratto = this.random.nextDouble();
        if (estratto < PROBABILITA_RIPOSO) {
            return TipoPiano.RIPOSO;
        }
        if (estratto < PROBABILITA_RIPOSO + PROBABILITA_CASSA) {
            return TipoPiano.CASSA;
        }
        return TipoPiano.COMBATTIMENTO;
    }

    private ContenutoCassa scegliContenutoCassa() {
        ContenutoCassa[] contenuti = ContenutoCassa.values();
        return contenuti[this.random.nextInt(contenuti.length)];
    }

    private record PianoGarantito(TipoPiano tipo, ContenutoCassa contenuto) {
    }
}