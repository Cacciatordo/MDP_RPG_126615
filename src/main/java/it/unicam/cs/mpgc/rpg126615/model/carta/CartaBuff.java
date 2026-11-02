package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class CartaBuff extends CartaStatistica {

    public CartaBuff(String nome, String descrizione, int valoreBase,
                     Livellabile riferimentoLivello, StatisticaBersaglio bersaglio) {
        super(nome, descrizione, valoreBase, riferimentoLivello, bersaglio);
    }

    @Override
    public TipoCarta getTipo() {
        return switch (getBersaglio()) {
            case ATTACCO -> TipoCarta.POTENZIAMENTO_ATTACCO;
            case SCUDO -> TipoCarta.POTENZIAMENTO_DIFESA;
        };
    }

    @Override
    protected void produciEffetto(ContestoCarta contesto) {
        contesto.applicaBonusGiocatore(getBersaglio(), getValoreEffettivo());
    }
}