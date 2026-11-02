package it.unicam.cs.mpgc.rpg126615.model.entita;

import it.unicam.cs.mpgc.rpg126615.model.carta.FabbricaCarte;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mano;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mazzo;
import it.unicam.cs.mpgc.rpg126615.model.progressione.GestoreLivello;
import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class Giocatore extends EntitaAstratta implements Livellabile {

    private static final int PA_PER_TURNO = 3;

    private final Mano mano = new Mano();
    private final Mazzo mazzo;
    private final GestoreLivello gestoreLivello = new GestoreLivello();

    private int puntiAzione;
    private int bonusAttaccoPermanente;
    private int difesaPermanente;

    public Giocatore(String nome, int hpMax, int attaccoBase) {
        super(nome, hpMax, attaccoBase);
        this.mazzo = new Mazzo(new FabbricaCarte().creaMazzoIniziale(this));
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
        while (pescate < quantita && mano.pescaDa(mazzo)) {
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

    @Override
    public int getLivello() {
        return gestoreLivello.getLivello();
    }

    @Override
    public int getEsperienza() {
        return gestoreLivello.getEsperienza();
    }

    @Override
    public int getEsperienzaProssimoLivello() {
        return gestoreLivello.getEsperienzaProssimoLivello();
    }

    @Override
    public void aggiungiEsperienza(int xp) {
        gestoreLivello.aggiungiEsperienza(xp);
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