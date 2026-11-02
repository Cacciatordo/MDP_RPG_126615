package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class CartaCura extends CartaAstratta {

    private static final int CURA_PER_LIVELLO = 3;

    public CartaCura(String nome, String descrizione, int curaBase, Livellabile riferimentoLivello) {
        super(nome, descrizione, COSTO_PA_STANDARD, curaBase, riferimentoLivello);
        if (curaBase <= 0) {
            throw new IllegalArgumentException("Il valore di cura deve essere positivo: " + curaBase);
        }
    }

    @Override
    public TipoCarta getTipo() {
        return TipoCarta.CURA;
    }

    @Override
    public int getValoreEffettivo() {
        return getValoreBase() + getLivelloRiferimento() * CURA_PER_LIVELLO;
    }

    @Override
    protected void produciEffetto(ContestoCarta contesto) {
        contesto.richiediCura(getValoreEffettivo());
    }
}