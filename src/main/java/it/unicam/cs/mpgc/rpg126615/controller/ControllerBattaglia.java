package it.unicam.cs.mpgc.rpg126615.controller;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.battaglia.AzioniBattaglia;
import it.unicam.cs.mpgc.rpg126615.view.battaglia.VistaBattaglia;
import javafx.scene.Scene;

public class ControllerBattaglia {

    private final Partita partita;
    private final Battaglia battaglia;
    private final Runnable suUscita;
    private final VistaBattaglia vista;

    public ControllerBattaglia(DimensioniFinestra dimensioni, Partita partita, Runnable suUscita) {
        this.partita = partita;
        this.suUscita = suUscita;
        this.battaglia = partita.getBattaglia().orElseThrow();
        this.vista = new VistaBattaglia(dimensioni, this.battaglia, partita.getNumeroPianoCorrente(),
                new AzioniBattaglia(
                        this::attacca,
                        this::terminaTurno,
                        this::giocaCarta,
                        this::esci));
    }

    public Scene getScena() {
        return this.vista.getScena();
    }

    private void attacca() {
        int hpNemicoPrima = this.battaglia.getNemico().getHp();
        aggiornaVista(this.battaglia.attaccaConEroe());
        int danno = hpNemicoPrima - this.battaglia.getNemico().getHp();
        if (danno > 0) {
            this.vista.mostraAttaccoEroe(danno);
        }
    }

    private void terminaTurno() {
        int hpEroePrima = this.battaglia.getGiocatore().getHp();
        aggiornaVista(this.battaglia.terminaTurnoGiocatore());
        this.vista.mostraAttaccoNemico(hpEroePrima - this.battaglia.getGiocatore().getHp());
    }

    private void giocaCarta(int indice) {
        if (!this.battaglia.isFinita()) {
            aggiornaVista(this.battaglia.giocaCartaDallaMano(indice));
        }
    }

    private void esci() {
        if (this.battaglia.isFinita() && !this.battaglia.isVinta()) {
            this.partita.ritiratiDopoSconfitta();
        }
        this.suUscita.run();
    }

    private void aggiornaVista(String esito) {
        this.vista.mostraEsito(esito);
        if (!this.battaglia.isFinita()) {
            return;
        }
        if (this.battaglia.isVinta()) {
            this.vista.mostraVittoria(this.partita.haProssimoPiano());
        } else {
            this.vista.mostraSconfitta();
        }
    }
}