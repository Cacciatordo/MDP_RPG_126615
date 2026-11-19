package it.unicam.cs.mpgc.rpg126615.model.entita;

import it.unicam.cs.mpgc.rpg126615.model.carta.StatisticheModificabili;

public interface Combattente extends StatisticheModificabili {

    String getNome();

    int getHp();

    int getHpMax();

    int getAttaccoBase();

    int getAttacco();

    int getDifesa();

    void azzeraModificatori();

    int attacca(Combattente bersaglio);

    void subisciDanno(int danno);

    void cura(int quantita);

    boolean isVivo();

    default boolean isBoss() {
        return false;
    }

    default boolean isFinale() {
        return false;
    }
}