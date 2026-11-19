package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Ferita {

    private int pianiRimanenti;

    public void applica(int numeroPiani) {
        if (numeroPiani <= 0) {
            throw new IllegalArgumentException("I piani di ferita devono essere maggiori di 0");
        }
        pianiRimanenti += numeroPiani;
    }

    public void avanzaPiano() {
        if (pianiRimanenti > 0) {
            pianiRimanenti--;
        }
    }

    public boolean isAttiva() {
        return pianiRimanenti > 0;
    }

    public int getPianiRimanenti() {
        return pianiRimanenti;
    }

    public void ripristina(int pianiRimanenti) {
        this.pianiRimanenti = pianiRimanenti;
    }
}