package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class Carta {

    private final String nome;
    private final String descrizione;
    private final int costoPA;
    private final TipoCarta tipo;

    public Carta(String nome, String descrizione, int costoPA, TipoCarta tipo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.costoPA = costoPA;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getCostoPA() {
        return costoPA;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public void applica(Giocatore giocatore, Nemico nemico) {
    }
}