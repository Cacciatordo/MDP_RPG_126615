package it.unicam.cs.mpgc.rpg126615.model.progressione;

public class GestoreLivello {

    public static final int LIVELLO_INIZIALE = 1;

    private static final int XP_BASE_PER_LIVELLO = 100;

    private int livello = LIVELLO_INIZIALE;
    private int esperienza;

    public int getLivello() {
        return livello;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public int getEsperienzaProssimoLivello() {
        return livello * XP_BASE_PER_LIVELLO;
    }

    public int aggiungiEsperienza(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("L'esperienza non può essere negativa");
        }
        int livelloPrima = livello;
        esperienza += xp;
        while (esperienza >= getEsperienzaProssimoLivello()) {
            esperienza -= getEsperienzaProssimoLivello();
            livello++;
        }
        return livello - livelloPrima;
    }
}