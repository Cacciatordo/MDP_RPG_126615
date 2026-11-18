package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.persistenza.ArchivioSalvataggiFile;
import it.unicam.cs.mpgc.rpg126615.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.ui.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.ui.view.battaglia.VistaBattaglia;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.ArrivoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.AzioniCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.IngressoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.corridoio.VistaCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.view.finale.RiepilogoFinale;
import it.unicam.cs.mpgc.rpg126615.ui.view.finale.VistaFineGioco;
import it.unicam.cs.mpgc.rpg126615.ui.view.menu.AzioniMenu;
import it.unicam.cs.mpgc.rpg126615.ui.view.menu.VistaIniziale;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.Optional;

public class App extends Application {

    private static final double LARGHEZZA_FINESTRA = 820;
    private static final double ALTEZZA_FINESTRA = 620;
    private static final DimensioniFinestra DIMENSIONI_FINESTRA =
            new DimensioniFinestra(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA);
    private static final String TITOLO_FINESTRA = "Emberfall";
    private static final String CARTELLA_SALVATAGGI = "salvataggi";
    private static final String FILE_RADICE_PROGETTO = "build.gradle";

    private Stage finestra;
    private Partita partita;
    private double posizioneEroe;
    private final DialoghiSalvataggio dialoghi = new DialoghiSalvataggio(
            new ArchivioSalvataggiFile(radiceProgetto().resolve(CARTELLA_SALVATAGGI)));

    @Override
    public void start(Stage primaryStage) {
        this.finestra = primaryStage;
        primaryStage.setTitle(TITOLO_FINESTRA);
        primaryStage.setResizable(false);
        mostraMenu();
        primaryStage.show();
    }

    private void mostraMenu() {
        AzioniMenu azioni = new AzioniMenu(
                (nome, piani) -> iniziaNuovaPartita(nome, piani),
                () -> this.dialoghi.scegliDaCaricare().ifPresent(dati -> caricaPartita(dati)));
        VistaIniziale vista = new VistaIniziale(DIMENSIONI_FINESTRA, azioni);
        this.finestra.setScene(vista.getScena());
    }

    private void iniziaNuovaPartita(String nomeEroe, int piani) {
        this.partita = Partita.nuova(nomeEroe, piani);
        mostraCorridoio();
    }

    private void caricaPartita(DatiSalvataggio dati) {
        try {
            Partita caricata = Partita.nuova("", dati.piani().size(), dati.semeTorre());
            caricata.ripristina(dati);
            this.partita = caricata;
        } catch (IllegalArgumentException errore) {
            new Alert(Alert.AlertType.ERROR, "Impossibile caricare il salvataggio: " + errore.getMessage())
                    .showAndWait();
            return;
        }
        mostraCorridoio();
    }

    private void tornaAlMenu() {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION,
                "I progressi non salvati andranno persi.", ButtonType.OK, ButtonType.CANCEL);
        conferma.setTitle("Menu Principale");
        conferma.setHeaderText("Tornare al menu iniziale?");
        conferma.showAndWait()
                .filter(risposta -> risposta == ButtonType.OK)
                .ifPresent(risposta -> mostraMenu());
    }

    private void mostraCorridoio() {
        mostraCorridoio(ArrivoNelCorridoio.NESSUNO);
    }

    private void mostraCorridoio(ArrivoNelCorridoio arrivo) {
        AzioniCorridoio azioni = new AzioniCorridoio(
                posizione -> mostraBattaglia(posizione),
                () -> vaiAlProssimoPiano(),
                () -> vaiAlPianoPrecedente(),
                () -> tornaAlMenu(),
                dati -> caricaPartita(dati));
        VistaCorridoio vista = new VistaCorridoio(DIMENSIONI_FINESTRA, this.partita, this.dialoghi, azioni,
                new IngressoNelCorridoio(arrivo, this.posizioneEroe));
        this.finestra.setScene(vista.getScena());
    }

    private void mostraBattaglia(double posizione) {
        this.posizioneEroe = posizione;
        VistaBattaglia vista = new VistaBattaglia(DIMENSIONI_FINESTRA, this.partita,
                () -> tornaDallaBattaglia());
        this.finestra.setScene(vista.getScena());
    }

    private void tornaDallaBattaglia() {
        if (this.partita.isGiocatoreSconfitto()) {
            this.partita.ritiratiDopoSconfitta();
            mostraCorridoio();
        } else if (this.partita.isPianoSuperato() && !this.partita.haProssimoPiano()) {
            mostraFineGioco();
        } else if (this.partita.isPianoSuperato()) {
            mostraCorridoio(ArrivoNelCorridoio.DOPO_SCONTRO);
        } else {
            mostraCorridoio();
        }
    }

    private void mostraFineGioco() {
        RiepilogoFinale riepilogo = new RiepilogoFinale(this.partita.getGiocatore().getNome(),
                this.partita.getGiocatore().getLivello(), this.partita.getTotalePiani());
        VistaFineGioco vista = new VistaFineGioco(DIMENSIONI_FINESTRA, riepilogo, () -> mostraMenu());
        this.finestra.setScene(vista.getScena());
    }

    private void vaiAlProssimoPiano() {
        this.partita.avanzaAlProssimoPiano();
        mostraCorridoio(ArrivoNelCorridoio.DA_SOTTO);
    }

    private void vaiAlPianoPrecedente() {
        this.partita.tornaAlPianoPrecedente();
        mostraCorridoio(ArrivoNelCorridoio.DA_SOPRA);
    }

    private static Path radiceProgetto() {
        return posizioneClassi()
                .flatMap(App::risaliFinoAllaRadice)
                .orElseGet(() -> Path.of("").toAbsolutePath());
    }

    private static Optional<Path> posizioneClassi() {
        try {
            CodeSource origine = App.class.getProtectionDomain().getCodeSource();
            return origine == null
                    ? Optional.empty()
                    : Optional.of(Path.of(origine.getLocation().toURI()));
        } catch (URISyntaxException | SecurityException e) {
            return Optional.empty();
        }
    }

    private static Optional<Path> risaliFinoAllaRadice(Path partenza) {
        for (Path cartella = partenza; cartella != null; cartella = cartella.getParent()) {
            if (Files.exists(cartella.resolve(FILE_RADICE_PROGETTO))) {
                return Optional.of(cartella);
            }
        }
        return Optional.empty();
    }
}