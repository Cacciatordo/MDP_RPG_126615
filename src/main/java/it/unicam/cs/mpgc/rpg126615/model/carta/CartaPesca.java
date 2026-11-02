package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class CartaPesca extends CartaAstratta {

    public static final int CARTE_DA_PESCARE = 1;

    public CartaPesca(String nome, String descrizione, Livellabile riferimentoLivello) {
        super(nome, descrizione, COSTO_PA_STANDARD, 0, riferimentoLivello);
    }

    @Override
    public TipoCarta getTipo() {
        return TipoCarta.PESCA;
    }

    @Override
    public boolean isGiocabile(Mano mano) {
        return !mano.isPiena();
    }

    @Override
    protected void produciEffetto(ContestoCarta contesto) {
        contesto.richiediPescata(CARTE_DA_PESCARE);
    }
}