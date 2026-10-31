package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class CartaDebuff extends Carta {

    private final int valore;

    public CartaDebuff(String nome, String descrizione, int costoPA, TipoCarta tipo, int valore) {
        super(nome, descrizione, costoPA, tipo);
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    @Override
    public void applica(Giocatore giocatore, Nemico nemico) {
        if (getTipo() == TipoCarta.INDEBOLIMENTO_ATTACCO) {
            nemico.modificaAttacco(-valore);
        } else if (getTipo() == TipoCarta.INDEBOLIMENTO_DIFESA) {
            nemico.modificaDifesa(-valore);
        }
    }
}