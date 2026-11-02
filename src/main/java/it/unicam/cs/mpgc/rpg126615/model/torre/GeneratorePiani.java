package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Boss;
import it.unicam.cs.mpgc.rpg126615.model.entita.BossFinale;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratorePiani {

    private static final Map<Integer, TipoPiano> PIANI_GARANTITI = Map.of(
            2, TipoPiano.CASSA,
            4, TipoPiano.RIPOSO,
            8, TipoPiano.CASSA,
            12, TipoPiano.RIPOSO);

    private static final double PROBABILITA_RIPOSO = 0.2;
    private static final double PROBABILITA_CASSA = 0.2;

    private final Random random;

    public GeneratorePiani(Random random) {
        this.random = random;
    }

    public List<PianoTorre> generaPiani(int numeroPiani) {
        List<PianoTorre> piani = new ArrayList<>();
        for (int numero = 1; numero <= numeroPiani; numero++) {
            piani.add(creaPiano(numero, numeroPiani));
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

    private PianoTorre creaPiano(int numero, int numeroPiani) {
        if (numero == numeroPiani) {
            return new PianoBossFinale(numero, new BossFinale());
        }
        if (isNumeroTriangolare(numero)) {
            return new PianoBoss(numero, new Boss(numero));
        }
        return switch (scegliTipo(numero)) {
            case RIPOSO -> new PianoRiposo(numero);
            case CASSA -> new PianoCassa(numero, scegliContenutoCassa());
            default -> new PianoCombattimento(numero, new Nemico(numero));
        };
    }

    private TipoPiano scegliTipo(int numero) {
        TipoPiano garantito = PIANI_GARANTITI.get(numero);
        if (garantito != null) {
            return garantito;
        }
        double estratto = random.nextDouble();
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
        return contenuti[random.nextInt(contenuti.length)];
    }
}