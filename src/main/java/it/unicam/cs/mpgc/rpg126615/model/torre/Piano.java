package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class Piano {

    private static final int PERCENTUALE_CURA_RIPOSO = 30;

    private final int numero;
    private final TipoPiano tipo;
    private final Nemico nemico;
    private final ContenutoCassa contenuto;
    private boolean completato;

    public Piano(int numero, TipoPiano tipo, Nemico nemico) {
        this(numero, tipo, nemico, null);
    }

    public Piano(int numero, ContenutoCassa contenuto) {
        this(numero, TipoPiano.CASSA, null, contenuto);
    }

    private Piano(int numero, TipoPiano tipo, Nemico nemico, ContenutoCassa contenuto) {
        this.numero = numero;
        this.tipo = tipo;
        this.nemico = nemico;
        this.contenuto = contenuto;
    }

    public int getNumero() {
        return numero;
    }

    public TipoPiano getTipo() {
        return tipo;
    }

    public Nemico getNemico() {
        return nemico;
    }

    public boolean haNemico() {
        return nemico != null;
    }

    public boolean isCompletato() {
        return completato;
    }

    public String entra(Giocatore giocatore) {
        if (completato) {
            return "";
        }
        return switch (tipo) {
            case RIPOSO -> usaFontana(giocatore);
            case CASSA -> apriCassa(giocatore);
            default -> "";
        };
    }

    private String usaFontana(Giocatore giocatore) {
        int curaPrevista = giocatore.getHpMax() * PERCENTUALE_CURA_RIPOSO / 100;
        int cura = Math.min(curaPrevista, giocatore.getHpMax() - giocatore.getHp());
        giocatore.cura(cura);
        completato = true;
        return "Una fontana di lava dal calore benefico ti cura di " + cura + " HP.";
    }

    private String apriCassa(Giocatore giocatore) {
        completato = true;
        return contenuto.applica(giocatore);
    }
}