package it.unicam.cs.mpgc.rpg126615.model.progressione;

public interface Livellabile {

    int getLivello();

    int getEsperienza();

    int getEsperienzaProssimoLivello();

    void aggiungiEsperienza(int xp);
}