package it.unicam.cs.mpgc.rpg126615.model.entita;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mano;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mazzo;

public class Giocatore extends Personaggio {

    private static final int XP_BASE_PER_LIVELLO = 100;
    private static final int PA_PER_TURNO = 3;

    private final Mano mano = new Mano();
    private final Mazzo mazzo = new Mazzo();

    private int livello;
    private int esperienza;
    private int puntiAzione;
    private int bonusAttaccoPermanente;
    private int difesaPermanente;

    public Giocatore(String nome, int hpMax, int attaccoBase) {
        super(nome, hpMax, attaccoBase);
        this.livello = 1;
        this.esperienza = 0;
        this.puntiAzione = PA_PER_TURNO;
    }

    public Mano getMano() {
        return mano;
    }

    public Mazzo getMazzo() {
        return mazzo;
    }

    public int pescaCarte(int quantita) {
        int pescate = 0;
        while (pescate < quantita && !mano.isPiena()) {
            Carta carta = mazzo.pesca();
            if (carta == null) {
                break;
            }
            mano.aggiungiCarta(carta);
            pescate++;
        }
        return pescate;
    }

    public int getPuntiAzione() {
        return puntiAzione;
    }

    public void consumaPA(int costo) {
        if (costo > puntiAzione) {
            throw new IllegalArgumentException("Punti azione insufficienti: servono " + costo
                    + ", ne hai " + puntiAzione);
        }
        puntiAzione -= costo;
    }

    public void resetPA() {
        puntiAzione = PA_PER_TURNO;
    }

    public int getLivello() {
        return livello;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public int getEsperienzaProssimoLivello() {
        return livello * XP_BASE_PER_LIVELLO;
    }

    public void aggiungiEsperienza(int xp) {
        esperienza += xp;
        while (esperienza >= getEsperienzaProssimoLivello()) {
            esperienza -= getEsperienzaProssimoLivello();
            livello++;
        }
    }

    public void aggiungiBonusAttacco(int bonus) {
        bonusAttaccoPermanente += bonus;
    }

    public void aggiungiDifesaPermanente(int bonus) {
        difesaPermanente += bonus;
    }

    @Override
    public int getAttacco() {
        return super.getAttacco() + bonusAttaccoPermanente;
    }

    @Override
    public int getDifesa() {
        return super.getDifesa() + difesaPermanente;
    }
}