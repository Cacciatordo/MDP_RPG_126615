package it.unicam.cs.mpgc.rpg126615.model.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.carta.ContestoCarta;
import it.unicam.cs.mpgc.rpg126615.model.carta.TipoCarta;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;

import java.util.List;

public class Battaglia {

    public static final int COSTO_PA_ATTACCO_BASE = 1;
    public static final int CARTE_INIZIALI_BATTAGLIA = 3;
    public static final int CARTE_PESCATE_INIZIO_TURNO = 1;

    private final Giocatore giocatore;
    private final Nemico nemico;
    private final ContestoCarta contestoCarte;
    private boolean attaccoEseguitoNelTurno;

    public Battaglia(Giocatore giocatore, Nemico nemico) {
        this.giocatore = giocatore;
        this.nemico = nemico;
        this.contestoCarte = new ContestoBattaglia(giocatore, nemico);
        this.giocatore.azzeraModificatori();
        this.nemico.azzeraModificatori();
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

    public boolean isAttaccoEseguitoNelTurno() {
        return attaccoEseguitoNelTurno;
    }

    public String giocaCartaDallaMano(int indiceCarta) {
        controllaBattagliaInCorso();
        List<Carta> carte = giocatore.getMano().getCarte();
        if (indiceCarta < 0 || indiceCarta >= carte.size()) {
            return aCapo(CronacaBattaglia.cartaNonValida());
        }
        Carta carta = carte.get(indiceCarta);
        if (!carta.isGiocabile(giocatore.getMano())) {
            return aCapo(CronacaBattaglia.manoPiena(carta));
        }
        if (giocatore.getPuntiAzione() < carta.getCostoPA()) {
            return aCapo(CronacaBattaglia.cartaSenzaPuntiAzione(carta));
        }
        return aCapo(giocaCarta(carta));
    }

    public String attaccaConEroe() {
        controllaBattagliaInCorso();
        if (attaccoEseguitoNelTurno) {
            return aCapo(CronacaBattaglia.attaccoGiaEseguito());
        }
        if (giocatore.getPuntiAzione() < COSTO_PA_ATTACCO_BASE) {
            return aCapo(CronacaBattaglia.attaccoSenzaPuntiAzione());
        }
        giocatore.consumaPA(COSTO_PA_ATTACCO_BASE);
        attaccoEseguitoNelTurno = true;
        int attacco = giocatore.getAttacco();
        int armaturaNemico = nemico.getDifesa();
        int danno = giocatore.attacca(nemico);
        String racconto = CronacaBattaglia.attaccoDelGiocatore(new EsitoAttacco(attacco, armaturaNemico, danno));
        if (!nemico.isVivo()) {
            racconto += "\n" + concludiConVittoria();
        }
        return aCapo(racconto);
    }

    public String terminaTurnoGiocatore() {
        controllaBattagliaInCorso();
        StatisticheInGioco primaDelNuovoRound = statisticheInGioco();
        String racconto = nemicoAttacca();
        if (!giocatore.isVivo()) {
            return aCapo(racconto + "\n" + concludiConSconfitta());
        }
        iniziaNuovoRound();
        return aCapo(racconto + raccontaEffettiSvaniti(primaDelNuovoRound));
    }

    private String giocaCarta(Carta carta) {
        StatisticheInGioco prima = statisticheInGioco();
        int hpPrima = giocatore.getHp();
        boolean curaAnnullata = carta.getTipo() == TipoCarta.CURA && giocatore.isGuarigioneBloccata();
        giocatore.consumaPA(carta.getCostoPA());
        giocatore.getMano().rimuoviCarta(carta);
        giocatore.getMazzo().scarta(carta);
        int carteInManoPrima = giocatore.getMano().getDimensione();
        carta.applica(contestoCarte);
        String racconto = descriviEffetto(carta, prima, hpPrima, curaAnnullata);
        int cartePescate = giocatore.getMano().getDimensione() - carteInManoPrima;
        if (cartePescate > 0) {
            racconto += "\n" + CronacaBattaglia.cartePescate(cartePescate);
        }
        return racconto;
    }

    private String descriviEffetto(Carta carta, StatisticheInGioco prima, int hpPrima, boolean curaAnnullata) {
        if (curaAnnullata) {
            return CronacaBattaglia.curaAnnullata(carta, giocatore.getPianiFeritaRimanenti());
        }
        if (carta.getTipo() == TipoCarta.CURA) {
            return CronacaBattaglia.curaRiuscita(carta, giocatore.getHp() - hpPrima);
        }
        return CronacaBattaglia.cartaUsata(carta, prima, statisticheInGioco());
    }

    private String nemicoAttacca() {
        int attaccoNemico = nemico.getAttacco();
        int scudoGiocatore = giocatore.getDifesa();
        int danno = nemico.attacca(giocatore);
        return CronacaBattaglia.attaccoDelNemico(nemico.getNome(),
                new EsitoAttacco(attaccoNemico, scudoGiocatore, danno));
    }

    private void iniziaNuovoRound() {
        giocatore.azzeraModificatori();
        nemico.azzeraModificatori();
        giocatore.resetPA();
        attaccoEseguitoNelTurno = false;
        giocatore.pescaCarte(CARTE_PESCATE_INIZIO_TURNO);
    }

    private String raccontaEffettiSvaniti(StatisticheInGioco primaDelNuovoRound) {
        StatisticheInGioco dopo = statisticheInGioco();
        if (dopo.equals(primaDelNuovoRound)) {
            return "";
        }
        return "\n" + CronacaBattaglia.effettiSvaniti(primaDelNuovoRound, dopo);
    }

    private String concludiConVittoria() {
        int esperienza = nemico.getEsperienzaData();
        giocatore.aggiungiEsperienza(esperienza);
        giocatore.azzeraModificatori();
        return CronacaBattaglia.vittoria(esperienza);
    }

    private String concludiConSconfitta() {
        giocatore.azzeraModificatori();
        return CronacaBattaglia.sconfitta();
    }

    private StatisticheInGioco statisticheInGioco() {
        return new StatisticheInGioco(giocatore.getAttacco(), giocatore.getDifesa(),
                nemico.getAttacco(), nemico.getDifesa());
    }

    private void controllaBattagliaInCorso() {
        if (isFinita()) {
            throw new IllegalStateException("La battaglia è già finita");
        }
    }

    private static String aCapo(String testo) {
        return testo + "\n";
    }
}