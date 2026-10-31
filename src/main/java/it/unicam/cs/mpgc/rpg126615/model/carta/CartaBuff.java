package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class CartaBuff extends Carta {

    private final int valore;

    public CartaBuff(String nome, String descrizione, int costoPA, TipoCarta tipo, int valore) {
        super(nome, descrizione, costoPA, tipo);
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    @Override
    public void applica(Giocatore giocatore, Nemico nemico) {
        if (getTipo() == TipoCarta.POTENZIAMENTO_ATTACCO) {
            giocatore.modificaAttacco(valore);
        } else if (getTipo() == TipoCarta.POTENZIAMENTO_DIFESA) {
            giocatore.modificaDifesa(valore);
        }
    }
}