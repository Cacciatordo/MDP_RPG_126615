package it.unicam.cs.mpgc.rpg126615.model.torre;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public enum ContenutoCassa {

    ESPERIENZA {
        @Override
        public String applica(Giocatore giocatore, int numeroPiano) {
            int xp = XP_BASE + numeroPiano * XP_PER_PIANO;
            giocatore.aggiungiEsperienza(xp);
            return "Hai trovato una pergamena antica! Ottieni " + xp + " punti esperienza.";
        }
    },

    BONUS_ATTACCO {
        @Override
        public String applica(Giocatore giocatore, int numeroPiano) {
            giocatore.aggiungiBonusAttacco(BONUS_ATTACCO_VALORE);
            return "Hai trovato una pietra d'affilatura! Attacco aumentato permanentemente di "
                    + BONUS_ATTACCO_VALORE + ".";
        }
    },

    BONUS_DIFESA {
        @Override
        public String applica(Giocatore giocatore, int numeroPiano) {
            giocatore.aggiungiDifesaPermanente(BONUS_DIFESA_VALORE);
            return "Hai trovato una corazza rinforzata! Difesa aumentata permanentemente di "
                    + BONUS_DIFESA_VALORE + ".";
        }
    },

    TRAPPOLA {
        @Override
        public String applica(Giocatore giocatore, int numeroPiano) {
            int dannoPrevisto = DANNO_TRAPPOLA_BASE + numeroPiano * DANNO_TRAPPOLA_PER_PIANO;
            int dannoSubito = Math.min(dannoPrevisto, Math.max(0, giocatore.getHp() - 1));
            if (dannoSubito > 0) {
                giocatore.subisciDanno(dannoSubito);
            }
            giocatore.applicaFerita(TURNI_BLOCCO_CURA);
            return "Scatta una trappola! Subisci " + dannoSubito + " danni e le cure sono bloccate per "
                    + TURNI_BLOCCO_CURA + " nuovi piani!";
        }
    };

    public static final int XP_BASE = 50;
    public static final int XP_PER_PIANO = 10;
    public static final int BONUS_ATTACCO_VALORE = 2;
    public static final int BONUS_DIFESA_VALORE = 1;
    public static final int DANNO_TRAPPOLA_BASE = 15;
    public static final int DANNO_TRAPPOLA_PER_PIANO = 2;
    public static final int TURNI_BLOCCO_CURA = 3;

    public abstract String applica(Giocatore giocatore, int numeroPiano);
}