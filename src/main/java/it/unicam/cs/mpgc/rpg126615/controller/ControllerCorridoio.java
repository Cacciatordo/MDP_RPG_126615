package it.unicam.cs.mpgc.rpg126615.controller;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.AzioniCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.EventiCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.IngressoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.RichiestaConferma;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.VistaCorridoio;
import javafx.scene.Scene;

import java.util.List;

public class ControllerCorridoio {

    private static final double MARGINE_USCITA_DESTRA = 120;
    private static final double LIMITE_SCALA_DISCESA = 44;
    private static final RichiestaConferma RICHIESTA_CASSA = RichiestaConferma.standard("Antica cassa",
            "Vuoi aprirla? Potresti trovare un premio... o una trappola!");
    private static final RichiestaConferma RICHIESTA_FONTANA = new RichiestaConferma("Fontana di lava",
            "Vuoi curarti ora?", "SÌ, curami ora", "NO, ci penserò dopo");
    private static final List<TipoPiano> PIANI_CON_CASSA = List.of(TipoPiano.CASSA);
    private static final List<TipoPiano> PIANI_CON_FONTANA = List.of(TipoPiano.RIPOSO);
    private static final List<TipoPiano> PIANI_CON_NEMICO = List.of(
            TipoPiano.COMBATTIMENTO, TipoPiano.BOSS, TipoPiano.BOSS_FINALE);

    private final Partita partita;
    private final DialoghiSalvataggio dialoghi;
    private final AzioniCorridoio azioni;
    private final double larghezza;
    private final VistaCorridoio vista;
    private final InterazioneConPiano interazioneCassa;
    private final InterazioneConPiano interazioneFontana;
    private final InterazioneConPiano interazioneNemico;

    private boolean interazioneAvviata;
    private boolean richiestaMostrata;

    public ControllerCorridoio(DimensioniFinestra dimensioni, Partita partita, DialoghiSalvataggio dialoghi,
                               AzioniCorridoio azioni, IngressoNelCorridoio ingresso) {
        this.partita = partita;
        this.dialoghi = dialoghi;
        this.azioni = azioni;
        this.larghezza = dimensioni.larghezza();
        this.vista = new VistaCorridoio(dimensioni, partita, new EventiCorridoio(
                () -> controllaInterazioni(),
                () -> riaffronta(),
                () -> salva(),
                () -> carica(),
                azioni.suMenu()), ingresso);
        double centro = this.vista.getCentroEntita();
        this.interazioneCassa = new InterazioneConPiano(partita, centro, PIANI_CON_CASSA);
        this.interazioneFontana = new InterazioneConPiano(partita, centro, PIANI_CON_FONTANA);
        this.interazioneNemico = new InterazioneConPiano(partita, centro, PIANI_CON_NEMICO);
    }

    public Scene getScena() {
        return this.vista.getScena();
    }

    private void controllaInterazioni() {
        if (this.interazioneAvviata || this.vista.isConfermaVisibile()) {
            return;
        }
        double centroEroe = this.vista.getCentroEroe();
        this.vista.aggiornaPulsanteSfida();
        boolean vicinoACassa = this.interazioneCassa.isDisponibile(centroEroe);
        boolean vicinoAFontana = this.interazioneFontana.isDisponibile(centroEroe);
        if (!vicinoACassa && !vicinoAFontana) {
            this.richiestaMostrata = false;
        }

        if (this.interazioneNemico.isDisponibile(centroEroe) && !this.partita.isPianoSuperato()) {
            avviaCombattimento();
        } else if (vicinoACassa && !this.richiestaMostrata) {
            chiediConferma(RICHIESTA_CASSA, this.interazioneCassa);
        } else if (vicinoAFontana && !this.richiestaMostrata) {
            chiediConferma(RICHIESTA_FONTANA, this.interazioneFontana);
        } else {
            controllaPassaggioDiPiano();
        }
    }

    private void avviaCombattimento() {
        this.interazioneAvviata = true;
        this.azioni.suCombatti().accept(this.vista.getPosizioneEroe());
    }

    private void chiediConferma(RichiestaConferma richiesta, InterazioneConPiano interazione) {
        this.richiestaMostrata = true;
        this.vista.fermaEroe();
        this.vista.mostraConferma(richiesta, () -> usa(interazione));
    }

    private void controllaPassaggioDiPiano() {
        if (isUscitaRaggiunta()) {
            this.interazioneAvviata = true;
            this.vista.animaSalita(this.azioni.suProssimoPiano());
        } else if (isScalaDiscesaRaggiunta()) {
            this.interazioneAvviata = true;
            this.vista.animaDiscesa(this.azioni.suPianoPrecedente());
        }
    }

    private void riaffronta() {
        if (this.interazioneAvviata) {
            return;
        }
        this.interazioneAvviata = true;
        this.vista.fermaEroe();
        this.partita.riaffrontaAvversario();
        this.azioni.suCombatti().accept(this.vista.getPosizioneEroe());
    }

    private boolean isUscitaRaggiunta() {
        return this.partita.haProssimoPiano()
                && this.partita.isPassaggioLibero()
                && this.vista.getPosizioneEroe() >= this.larghezza - MARGINE_USCITA_DESTRA;
    }

    private boolean isScalaDiscesaRaggiunta() {
        return this.partita.getNumeroPianoCorrente() > 1
                && this.vista.getPosizioneEroe() <= LIMITE_SCALA_DISCESA;
    }

    private void usa(InterazioneConPiano interazione) {
        this.vista.aggiornaDopoUso(interazione.esegui());
    }

    private void salva() {
        this.dialoghi.salva(this.partita).ifPresent(messaggio -> this.vista.mostraMessaggio(messaggio));
    }

    private void carica() {
        this.dialoghi.scegliDaCaricare().ifPresent(dati -> this.azioni.suPartitaCaricata().accept(dati));
    }
}