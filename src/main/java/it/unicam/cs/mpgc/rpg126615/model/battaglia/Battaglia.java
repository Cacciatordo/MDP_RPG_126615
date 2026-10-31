package it.unicam.cs.mpgc.rpg126615.model.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.List;

public class Battaglia {

    public static final int COSTO_PA_ATTACCO_BASE = 1;
    public static final int CARTE_INIZIALI_BATTAGLIA = 3;
    public static final int CARTE_PESCATE_INIZIO_TURNO = 1;

    private final Giocatore giocatore;
    private final Nemico nemico;

    public Battaglia(Giocatore giocatore, Nemico nemico) {
        this.giocatore = giocatore;
        this.nemico = nemico;
        this.giocatore.azzeraModificatori();
        this.giocatore.getMazzo().ripristinaMazzoCompleto(this.giocatore.getMano());
        this.giocatore.pescaCarte(CARTE_INIZIALI_BATTAGLIA);
        this.giocatore.resetPA();
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

    public String giocaCartaDallaMano(int indiceCarta) {
        controllaBattagliaInCorso();
        List<Carta> carte = giocatore.getMano().getCarte();
        if (indiceCarta < 0 || indiceCarta >= carte.size()) {
            return "Carta non valida.\n";
        }
        Carta carta = carte.get(indiceCarta);
        if (giocatore.getPuntiAzione() < carta.getCostoPA()) {
            return "Punti azione insufficienti per giocare " + carta.getNome() + ".\n";
        }
        giocatore.consumaPA(carta.getCostoPA());
        giocatore.getMano().rimuoviCarta(carta);
        giocatore.getMazzo().scarta(carta);
        carta.applica(giocatore, nemico);
        return "Giochi la carta " + carta.getNome() + ".\n";
    }

    public String attaccaConEroe() {
        controllaBattagliaInCorso();
        if (giocatore.getPuntiAzione() < COSTO_PA_ATTACCO_BASE) {
            return "Punti azione insufficienti per attaccare.\n";
        }
        giocatore.consumaPA(COSTO_PA_ATTACCO_BASE);
        StringBuilder racconto = new StringBuilder();
        int danno = giocatore.attacca(nemico);
        racconto.append("Colpisci ").append(nemico.getNome())
                .append(" per ").append(danno).append(" danni.\n");
        if (!nemico.isVivo()) {
            giocatore.aggiungiEsperienza(nemico.getEsperienzaData());
            racconto.append(nemico.getNome()).append(" è stato sconfitto! Ottieni ")
                    .append(nemico.getEsperienzaData()).append(" punti esperienza.\n");
        }
        return racconto.toString();
    }

    public String terminaTurnoGiocatore() {
        controllaBattagliaInCorso();
        StringBuilder racconto = new StringBuilder();
        int danno = nemico.attacca(giocatore);
        racconto.append(nemico.getNome()).append(" ti colpisce per ")
                .append(danno).append(" danni.\n");
        if (!giocatore.isVivo()) {
            racconto.append("Sei stato sconfitto.\n");
            return racconto.toString();
        }
        giocatore.resetPA();
        int cartePescate = giocatore.pescaCarte(CARTE_PESCATE_INIZIO_TURNO);
        racconto.append("Inizia un nuovo turno.");
        if (cartePescate > 0) {
            racconto.append(" Peschi una carta.");
        }
        racconto.append("\n");
        return racconto.toString();
    }

    private void controllaBattagliaInCorso() {
        if (isFinita()) {
            throw new IllegalStateException("La battaglia è già finita");
        }
    }
}