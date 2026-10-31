package it.unicam.cs.mpgc.rpg126615.model.carta;

public class CartaDebuff extends Carta {

    private final int valore;

    public CartaDebuff(String nome, String descrizione, int costoPA, TipoCarta tipo, int valore) {
        super(nome, descrizione, costoPA, tipo);
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }
}