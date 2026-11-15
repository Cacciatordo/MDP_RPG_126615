package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.persistenza.ArchivioSalvataggiFile;
import it.unicam.cs.mpgc.rpg126615.ui.view.battaglia.VistaBattaglia;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.ArrivoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.AzioniCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.VistaCorridoio;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Random;

public class App extends Application {

    private static final double LARGHEZZA_FINESTRA = 820;
    private static final double ALTEZZA_FINESTRA = 620;
    private static final String TITOLO_FINESTRA = "Emberfall";
    private static final String NOME_EROE = "Eroe";
    private static final int PIANI_DI_PROVA = 20;
    private static final long SEME_TORRE = 126615;
    private static final Path CARTELLA_SALVATAGGI = Path.of("salvataggi");

    private Stage finestra;
    private Partita partita;
    private double posizioneEroe;
    private final DialoghiSalvataggio dialoghi = new DialoghiSalvataggio(
            new ArchivioSalvataggiFile(CARTELLA_SALVATAGGI));

    @Override
    public void start(Stage primaryStage) {
        this.finestra = primaryStage;
        this.partita = Partita.nuova(NOME_EROE, PIANI_DI_PROVA, new Random(SEME_TORRE));
        primaryStage.setTitle(TITOLO_FINESTRA);
        primaryStage.setResizable(false);
        mostraCorridoio();
        primaryStage.show();
    }

    private void mostraCorridoio() {
        mostraCorridoio(ArrivoNelCorridoio.NESSUNO);
    }

    private void mostraCorridoio(ArrivoNelCorridoio arrivo) {
        AzioniCorridoio azioni = new AzioniCorridoio(
                posizione -> mostraBattaglia(posizione),
                () -> vaiAlProssimoPiano(),
                () -> vaiAlPianoPrecedente(),
                () -> { },
                () -> mostraCorridoio());
        VistaCorridoio vista = new VistaCorridoio(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA, this.partita,
                this.dialoghi, azioni, arrivo, this.posizioneEroe);
        this.finestra.setScene(vista.getScena());
    }

    private void mostraBattaglia(double posizione) {
        this.posizioneEroe = posizione;
        VistaBattaglia vista = new VistaBattaglia(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA, this.partita,
                () -> tornaDallaBattaglia());
        this.finestra.setScene(vista.getScena());
    }

    private void tornaDallaBattaglia() {
        if (this.partita.isPianoSuperato()) {
            mostraCorridoio(ArrivoNelCorridoio.DOPO_SCONTRO);
        } else {
            mostraCorridoio();
        }
    }

    private void vaiAlProssimoPiano() {
        this.partita.avanzaAlProssimoPiano();
        mostraCorridoio(ArrivoNelCorridoio.DA_SOTTO);
    }

    private void vaiAlPianoPrecedente() {
        this.partita.tornaAlPianoPrecedente();
        mostraCorridoio(ArrivoNelCorridoio.DA_SOPRA);
    }
}