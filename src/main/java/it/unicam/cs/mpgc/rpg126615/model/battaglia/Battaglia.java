package it.unicam.cs.mpgc.rpg126615.model.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

public class Battaglia {

    private final Giocatore giocatore;
    private final Nemico nemico;

    public Battaglia(Giocatore giocatore, Nemico nemico) {
        this.giocatore = giocatore;
        this.nemico = nemico;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public Nemico getNemico() {
        return nemico;
    }

    public boolean isFinita() {
        return !giocatore.isVivo() || !nemico.isVivo();
    }

    public boolean isVinta() {
        return !nemico.isVivo();
    }

    public String eseguiTurno() {
        if (isFinita()) {
            throw new IllegalStateException("La battaglia è già finita");
        }
        StringBuilder racconto = new StringBuilder();

        int dannoAlNemico = giocatore.getAttaccoBase();
        nemico.subisciDanno(dannoAlNemico);
        racconto.append("Colpisci ").append(nemico.getNome())
                .append(" per ").append(dannoAlNemico).append(" danni.\n");

        if (!nemico.isVivo()) {
            giocatore.aggiungiEsperienza(nemico.getEsperienzaData());
            racconto.append(nemico.getNome()).append(" è stato sconfitto! Ottieni ")
                    .append(nemico.getEsperienzaData()).append(" punti esperienza.\n");
            return racconto.toString();
        }

        int dannoAlGiocatore = nemico.getAttaccoBase();
        giocatore.subisciDanno(dannoAlGiocatore);
        racconto.append(nemico.getNome()).append(" ti colpisce per ")
                .append(dannoAlGiocatore).append(" danni.\n");

        if (!giocatore.isVivo()) {
            racconto.append("Sei stato sconfitto.\n");
        }
        return racconto.toString();
    }
}