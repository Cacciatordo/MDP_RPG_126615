package it.unicam.cs.mpgc.rpg126615.model.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.ContestoCarta;
import it.unicam.cs.mpgc.rpg126615.model.carta.StatisticaBersaglio;
import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;

public class ContestoBattaglia implements ContestoCarta {

    private final Giocatore giocatore;
    private final Combattente nemico;

    public ContestoBattaglia(Giocatore giocatore, Combattente nemico) {
        if (giocatore == null || nemico == null) {
            throw new IllegalArgumentException("Giocatore e nemico non possono essere nulli");
        }
        this.giocatore = giocatore;
        this.nemico = nemico;
    }

    @Override
    public void applicaBonusGiocatore(StatisticaBersaglio statistica, int valore) {
        statistica.modifica(giocatore, valore);
    }

    @Override
    public void applicaMalusNemico(StatisticaBersaglio statistica, int valore) {
        statistica.modifica(nemico, -valore);
    }

    @Override
    public void richiediPescata(int quantita) {
        giocatore.pescaCarte(quantita);
    }

    @Override
    public void richiediCura(int quantita) {
        if (giocatore.isGuarigioneBloccata()) {
            return;
        }
        giocatore.cura(quantita);
    }
}