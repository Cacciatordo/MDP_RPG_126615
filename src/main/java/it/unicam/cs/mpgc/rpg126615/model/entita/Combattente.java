package it.unicam.cs.mpgc.rpg126615.model.entita;

public interface Combattente {

    String getNome();

    int getHp();

    int getHpMax();

    int getAttaccoBase();

    int getAttacco();

    int getDifesa();

    void modificaAttacco(int variazione);

    void modificaDifesa(int variazione);

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