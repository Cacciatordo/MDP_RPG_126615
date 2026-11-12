package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.ui.view.battaglia.VistaBattaglia;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.VistaCorridoio;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {

    private static final double LARGHEZZA_FINESTRA = 820;
    private static final double ALTEZZA_FINESTRA = 620;
    private static final String TITOLO_FINESTRA = "Emberfall";
    private static final String NOME_EROE = "Eroe";
    private static final int PIANI_DI_PROVA = 20;

    private Stage finestra;
    private Partita partita;

    @Override
    public void start(Stage primaryStage) {
        this.finestra = primaryStage;
        this.partita = Partita.nuova(NOME_EROE, PIANI_DI_PROVA, new Random());
        primaryStage.setTitle(TITOLO_FINESTRA);
        primaryStage.setResizable(false);
        mostraCorridoio();
        primaryStage.show();
    }

    private void mostraCorridoio() {
        VistaCorridoio vista = new VistaCorridoio(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA, this.partita,
                this::entraNelPiano, this::mostraBattaglia, this::vaiAlProssimoPiano);
        this.finestra.setScene(vista.getScena());
    }

    private void mostraBattaglia() {
        VistaBattaglia vista = new VistaBattaglia(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA, this.partita,
                () -> mostraCorridoio());
        this.finestra.setScene(vista.getScena());
    }

    private void entraNelPiano() {
        this.partita.entraNelPianoCorrente();
        mostraCorridoio();
    }

    private void vaiAlProssimoPiano() {
        this.partita.avanzaAlProssimoPiano();
        mostraCorridoio();
    }
}