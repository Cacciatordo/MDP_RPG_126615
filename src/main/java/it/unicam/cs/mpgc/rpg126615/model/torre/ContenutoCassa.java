package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public enum ContenutoCassa {

    ESPERIENZA {
        @Override
        public String applica(Giocatore giocatore) {
            giocatore.aggiungiEsperienza(ESPERIENZA_GUADAGNATA);
            return "Nella cassa trovi un tomo antico: guadagni " + ESPERIENZA_GUADAGNATA + " XP.";
        }
    },

    BONUS_ATTACCO {
        @Override
        public String applica(Giocatore giocatore) {
            giocatore.aggiungiBonusAttacco(BONUS_ATTACCO_PERMANENTE);
            return "Nella cassa trovi una lama affilata: l'attacco sale di " + BONUS_ATTACCO_PERMANENTE
                    + " in modo permanente.";
        }
    },

    BONUS_DIFESA {
        @Override
        public String applica(Giocatore giocatore) {
            giocatore.aggiungiDifesaPermanente(BONUS_DIFESA_PERMANENTE);
            return "Nella cassa trovi uno scudo robusto: la difesa sale di " + BONUS_DIFESA_PERMANENTE
                    + " in modo permanente.";
        }
    },

    TRAPPOLA {
        @Override
        public String applica(Giocatore giocatore) {
            int danno = Math.min(DANNO_TRAPPOLA, giocatore.getHp() - 1);
            giocatore.subisciDanno(danno);
            return "La cassa era una trappola: subisci " + danno + " danni.";
        }
    };

    private static final int ESPERIENZA_GUADAGNATA = 50;
    private static final int BONUS_ATTACCO_PERMANENTE = 3;
    private static final int BONUS_DIFESA_PERMANENTE = 2;
    private static final int DANNO_TRAPPOLA = 10;

    public abstract String applica(Giocatore giocatore);
}