package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class CartaPesca extends Carta {

    private final int cartePescate;

    public CartaPesca(String nome, String descrizione, int costoPA, int cartePescate) {
        super(nome, descrizione, costoPA, TipoCarta.PESCA);
        this.cartePescate = cartePescate;
    }

    public int getCartePescate() {
        return cartePescate;
    }

    @Override
    public void applica(Giocatore giocatore, Nemico nemico) {
        giocatore.pescaCarte(cartePescate);
    }
}