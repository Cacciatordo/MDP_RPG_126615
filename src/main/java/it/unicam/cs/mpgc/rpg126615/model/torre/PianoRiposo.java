package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public class PianoRiposo extends PianoAstratto {

    private static final int PERCENTUALE_CURA = 30;

    public PianoRiposo(int numero) {
        super(numero, TipoPiano.RIPOSO);
    }

    @Override
    protected String eseguiEffetto(Giocatore giocatore) {
        int curaPrevista = giocatore.getHpMax() * PERCENTUALE_CURA / 100;
        int cura = Math.min(curaPrevista, giocatore.getHpMax() - giocatore.getHp());
        giocatore.cura(cura);
        segnaCompletato();
        return "Una fontana di lava dal calore benefico ti cura di " + cura + " HP.";
    }
}