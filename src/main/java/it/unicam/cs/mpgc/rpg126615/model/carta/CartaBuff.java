package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class CartaBuff extends CartaStatistica {

    public CartaBuff(String nome, String descrizione, int valoreBase,
                     Livellabile riferimentoLivello, StatisticaBersaglio bersaglio) {
        super(nome, descrizione, valoreBase, riferimentoLivello, bersaglio);
    }

    @Override
    public TipoCarta getTipo() {
        return getBersaglio().getTipoPotenziamento();
    }

    @Override
    protected void produciEffetto(ContestoCarta contesto) {
        contesto.applicaBonusGiocatore(getBersaglio(), getValoreEffettivo());
    }
}