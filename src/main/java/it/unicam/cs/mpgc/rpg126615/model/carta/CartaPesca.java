package it.unicam.cs.mpgc.rpg126615.model.carta;

public class CartaPesca extends Carta {

    private final int cartePescate;

    public CartaPesca(String nome, String descrizione, int costoPA, int cartePescate) {
        super(nome, descrizione, costoPA, TipoCarta.PESCA);
        this.cartePescate = cartePescate;
    }

    public int getCartePescate() {
        return cartePescate;
    }
}