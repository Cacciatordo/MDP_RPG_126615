package it.unicam.cs.mpgc.rpg126615.model.carta;

public interface ContestoCarta {

    void applicaBonusGiocatore(StatisticaBersaglio statistica, int valore);

    void applicaMalusNemico(StatisticaBersaglio statistica, int valore);

    void richiediPescata(int quantita);

    void richiediCura(int quantita);
}