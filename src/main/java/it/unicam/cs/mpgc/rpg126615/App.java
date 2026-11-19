package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.controller.ControllerBattaglia;
import it.unicam.cs.mpgc.rpg126615.controller.ControllerCorridoio;
import it.unicam.cs.mpgc.rpg126615.controller.ControllerMenu;
import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.ArchivioSalvataggiFile;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.ArrivoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.AzioniCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.IngressoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.finale.RiepilogoFinale;
import it.unicam.cs.mpgc.rpg126615.view.finale.VistaFineGioco;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.nio.file.Path;

public class App extends Application {

    private static final double LARGHEZZA_FINESTRA = 820;
    private static final double ALTEZZA_FINESTRA = 620;
    private static final DimensioniFinestra DIMENSIONI_FINESTRA =
            new DimensioniFinestra(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA);
    private static final String TITOLO_FINESTRA = "Emberfall";
    private static final String CARTELLA_SALVATAGGI = "salvataggi";

    private Stage finestra;
    private Partita partita;
    private double posizioneEroe;
    private final DialoghiSalvataggio dialoghi = new DialoghiSalvataggio(
            new ArchivioSalvataggiFile(Path.of(CARTELLA_SALVATAGGI)));

    @Override
    public void start(Stage primaryStage) {
        this.finestra = primaryStage;
        primaryStage.setTitle(TITOLO_FINESTRA);
        primaryStage.setResizable(false);
        mostraMenu();
        primaryStage.show();
    }

    private void mostraMenu() {
        ControllerMenu controller = new ControllerMenu(DIMENSIONI_FINESTRA, this.dialoghi,
                (nome, piani) -> iniziaNuovaPartita(nome, piani),
                dati -> caricaPartita(dati));
        this.finestra.setScene(controller.getScena());
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
        ControllerCorridoio controller = new ControllerCorridoio(DIMENSIONI_FINESTRA, this.partita, this.dialoghi,
                azioni, new IngressoNelCorridoio(arrivo, this.posizioneEroe));
        this.finestra.setScene(controller.getScena());
    }

    private void mostraBattaglia(double posizione) {
        this.posizioneEroe = posizione;
        ControllerBattaglia controller = new ControllerBattaglia(DIMENSIONI_FINESTRA, this.partita,
                () -> tornaDallaBattaglia());
        this.finestra.setScene(controller.getScena());
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
}