package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class CartaDebuff extends CartaStatistica {

    public CartaDebuff(String nome, String descrizione, int valoreBase,
                       Livellabile riferimentoLivello, StatisticaBersaglio bersaglio) {
        super(nome, descrizione, valoreBase, riferimentoLivello, bersaglio);
    }

    @Override
    public TipoCarta getTipo() {
        return switch (getBersaglio()) {
            case ATTACCO -> TipoCarta.INDEBOLIMENTO_ATTACCO;
            case SCUDO -> TipoCarta.INDEBOLIMENTO_DIFESA;
        };
    }

    @Override
    protected void produciEffetto(ContestoCarta contesto) {
        contesto.applicaMalusNemico(getBersaglio(), getValoreEffettivo());
    }
}