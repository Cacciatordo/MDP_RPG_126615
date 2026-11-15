package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public abstract class PianoAstratto implements PianoTorre {

    private final int numero;
    private final TipoPiano tipo;
    private final Nemico nemico;
    private boolean completato;

    protected PianoAstratto(int numero, TipoPiano tipo, Nemico nemico) {
        this.numero = numero;
        this.tipo = tipo;
        this.nemico = nemico;
    }

    protected PianoAstratto(int numero, TipoPiano tipo) {
        this(numero, tipo, null);
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public TipoPiano getTipo() {
        return tipo;
    }

    @Override
    public Nemico getNemico() {
        return nemico;
    }

    @Override
    public boolean haNemico() {
        return nemico != null;
    }

    @Override
    public boolean isCompletato() {
        return completato;
    }

    @Override
    public void ripristinaCompletato() {
        completato = true;
    }

    @Override
    public final String entra(Giocatore giocatore) {
        if (completato) {
            return "";
        }
        return eseguiEffetto(giocatore);
    }

    protected String eseguiEffetto(Giocatore giocatore) {
        return "";
    }

    protected void segnaCompletato() {
        completato = true;
    }
}