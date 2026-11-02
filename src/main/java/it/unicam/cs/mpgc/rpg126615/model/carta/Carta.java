package it.unicam.cs.mpgc.rpg126615.model.carta;

public interface Carta {

    String getNome();

    String getDescrizione();

    int getCostoPA();

    TipoCarta getTipo();

    void applica(ContestoCarta contesto);

    default boolean isGiocabile(Mano mano) {
        return true;
    }
}