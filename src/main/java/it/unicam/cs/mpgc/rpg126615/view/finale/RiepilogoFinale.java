package it.unicam.cs.mpgc.rpg126615.view.finale;

public record RiepilogoFinale(String nomeEroe, int livello, int pianiScalati) {

    public RiepilogoFinale {
        if (nomeEroe == null || nomeEroe.isBlank()) {
            throw new IllegalArgumentException("Il nome dell'eroe non può essere vuoto");
        }
        if (livello <= 0 || pianiScalati <= 0) {
            throw new IllegalArgumentException("Livello e piani scalati devono essere positivi: "
                    + livello + ", " + pianiScalati);
        }
    }
}