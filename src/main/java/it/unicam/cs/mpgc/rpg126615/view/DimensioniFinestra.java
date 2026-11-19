package it.unicam.cs.mpgc.rpg126615.view;

public record DimensioniFinestra(double larghezza, double altezza) {

    public DimensioniFinestra {
        if (larghezza <= 0 || altezza <= 0) {
            throw new IllegalArgumentException("Le dimensioni della finestra devono essere positive: "
                    + larghezza + " x " + altezza);
        }
    }
}