package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Giocatore extends Personaggio {

    private static final int XP_BASE_PER_LIVELLO = 100;

    private int livello;
    private int esperienza;

    public Giocatore(String nome, int hpMax, int attaccoBase) {
        super(nome, hpMax, attaccoBase);
        this.livello = 1;
        this.esperienza = 0;
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
}