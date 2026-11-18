package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public class PianoRiposo extends PianoAstratto {

    private static final int PERCENTUALE_CURA = 30;

    public PianoRiposo(int numero) {
        super(numero, TipoPiano.RIPOSO);
    }

    @Override
    protected String eseguiEffetto(Giocatore giocatore) {
        segnaCompletato();
        if (giocatore.isGuarigioneBloccata()) {
            return "La ferita della trappola impedisce alla fontana di curarti: ancora "
                    + giocatore.getPianiFeritaRimanenti() + " piani.";
        }
        int curaPrevista = giocatore.getHpMax() * PERCENTUALE_CURA / 100;
        int cura = Math.min(curaPrevista, giocatore.getHpMax() - giocatore.getHp());
        if (cura > 0) {
            giocatore.cura(cura);
        }
        return "Una fontana di lava dal calore benefico ti cura di " + cura + " HP.";
    }
}