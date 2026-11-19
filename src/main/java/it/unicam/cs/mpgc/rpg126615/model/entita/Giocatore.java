package it.unicam.cs.mpgc.rpg126615.model.entita;

import it.unicam.cs.mpgc.rpg126615.model.carta.CreatoreMazzo;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mano;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mazzo;
import it.unicam.cs.mpgc.rpg126615.model.progressione.GestoreLivello;
import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public class Giocatore extends EntitaAstratta implements Livellabile {

    public static final int PA_PER_TURNO = 3;
    public static final int ATTACCO_PER_LIVELLO = 2;
    public static final int HP_PER_LIVELLO = 15;
    public static final int PENALITA_XP_SCONFITTA = 25;

    private final Mano mano = new Mano();
    private final Mazzo mazzo;
    private final GestoreLivello gestoreLivello = new GestoreLivello();
    private final Ferita ferita = new Ferita();

    private int puntiAzione;
    private int bonusAttaccoPermanente;
    private int difesaPermanente;

    public Giocatore(String nome, int hpMax, int attaccoBase, CreatoreMazzo creatoreMazzo) {
        super(nome, hpMax, attaccoBase);
        this.mazzo = new Mazzo(creatoreMazzo.creaMazzoIniziale(this));
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
        int livelliGuadagnati = gestoreLivello.aggiungiEsperienza(xp);
        if (livelliGuadagnati > 0) {
            int incremento = livelliGuadagnati * HP_PER_LIVELLO;
            ripristinaHp(getHp() + incremento, getHpMax() + incremento);
        }
    }

    public void perdiEsperienza(int xp) {
        gestoreLivello.perdiEsperienza(xp);
    }

    public void recuperaDopoSconfitta() {
        perdiEsperienza(PENALITA_XP_SCONFITTA);
        cura(getHpMax());
    }

    public void aggiungiBonusAttacco(int bonus) {
        bonusAttaccoPermanente += bonus;
    }

    public void aggiungiDifesaPermanente(int bonus) {
        difesaPermanente += bonus;
    }

    public void applicaFerita(int numeroPiani) {
        ferita.applica(numeroPiani);
    }

    public void avanzaPiano() {
        ferita.avanzaPiano();
    }

    public boolean isGuarigioneBloccata() {
        return ferita.isAttiva();
    }

    public int getPianiFeritaRimanenti() {
        return ferita.getPianiRimanenti();
    }

    public StatoGiocatore getStato() {
        return new StatoGiocatore(getNome(), getLivello(), getEsperienza(), getHp(), getHpMax(),
                bonusAttaccoPermanente, difesaPermanente, ferita.getPianiRimanenti());
    }

    public void applicaStato(StatoGiocatore stato) {
        gestoreLivello.ripristina(stato.livello(), stato.esperienza());
        ripristinaHp(stato.hp(), stato.hpMax());
        bonusAttaccoPermanente = stato.bonusAttaccoPermanente();
        difesaPermanente = stato.difesaPermanente();
        ferita.ripristina(stato.pianiFeritaRimanenti());
    }

    public int getDifesaNaturale() {
        return Math.max(0, getLivello() - GestoreLivello.LIVELLO_INIZIALE);
    }

    @Override
    public int getAttacco() {
        return super.getAttacco() + getLivello() * ATTACCO_PER_LIVELLO + bonusAttaccoPermanente;
    }

    @Override
    public int getDifesa() {
        return super.getDifesa() + getDifesaNaturale() + difesaPermanente;
    }
}