package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.controller.ControllerBattaglia;
import it.unicam.cs.mpgc.rpg126615.controller.ControllerCorridoio;
import it.unicam.cs.mpgc.rpg126615.controller.ControllerMenu;
import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.ArchivioSalvataggiFile;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.DialoghiApplicazione;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.ArrivoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.AzioniCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.DialoghiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.view.corridoio.IngressoNelCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.finale.RiepilogoFinale;
import it.unicam.cs.mpgc.rpg126615.view.finale.VistaFineGioco;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;

public class App extends Application {

    private static final double LARGHEZZA_FINESTRA = 820;
    private static final double ALTEZZA_FINESTRA = 620;
    private static final DimensioniFinestra DIMENSIONI_FINESTRA =
            new DimensioniFinestra(LARGHEZZA_FINESTRA, ALTEZZA_FINESTRA);
    private static final String TITOLO_FINESTRA = "Emberfall";
    private static final String CARTELLA_SALVATAGGI = "salvataggi";
    private static final double NESSUNA_POSIZIONE = 0;

    private Stage finestra;
    private Partita partita;
    private final DialoghiSalvataggio dialoghi = new DialoghiSalvataggio(
            new ArchivioSalvataggiFile(Path.of(CARTELLA_SALVATAGGI)));
    private final DialoghiApplicazione dialoghiApplicazione = new DialoghiApplicazione();

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
                this::iniziaNuovaPartita,
                this::caricaPartita);
        this.finestra.setScene(controller.getScena());
    }

    private void iniziaNuovaPartita(String nomeEroe, int piani) {
        this.partita = Partita.nuova(nomeEroe, piani);
        mostraCorridoio();
    }

    private void caricaPartita(DatiSalvataggio dati) {
        try {
            this.partita = Partita.daSalvataggio(dati);
        } catch (IllegalArgumentException errore) {
            this.dialoghiApplicazione.mostraErroreCaricamento(errore.getMessage());
            return;
        }
        mostraCorridoio();
    }

    private void tornaAlMenu() {
        if (this.dialoghiApplicazione.confermaRitornoAlMenu()) {
            mostraMenu();
        }
    }

    private void mostraCorridoio() {
        mostraCorridoio(ArrivoNelCorridoio.NESSUNO);
    }

    private void mostraCorridoio(ArrivoNelCorridoio arrivo) {
        mostraCorridoio(new IngressoNelCorridoio(arrivo, NESSUNA_POSIZIONE));
    }

    private void mostraCorridoio(IngressoNelCorridoio ingresso) {
        AzioniCorridoio azioni = new AzioniCorridoio(
                this::mostraBattaglia,
                this::vaiAlProssimoPiano,
                this::vaiAlPianoPrecedente,
                this::tornaAlMenu,
                this::caricaPartita);
        ControllerCorridoio controller = new ControllerCorridoio(DIMENSIONI_FINESTRA, this.partita, this.dialoghi,
                azioni, ingresso);
        this.finestra.setScene(controller.getScena());
    }

    private void mostraBattaglia(double posizioneEroe) {
        ControllerBattaglia controller = new ControllerBattaglia(DIMENSIONI_FINESTRA, this.partita,
                () -> tornaDallaBattaglia(posizioneEroe));
        this.finestra.setScene(controller.getScena());
    }

    private void tornaDallaBattaglia(double posizioneEroe) {
        if (this.partita.isTorreCompletata()) {
            mostraFineGioco();
        } else if (this.partita.isPianoSuperato()) {
            mostraCorridoio(new IngressoNelCorridoio(ArrivoNelCorridoio.DOPO_SCONTRO, posizioneEroe));
        } else {
            mostraCorridoio();
        }
    }

    private void mostraFineGioco() {
        RiepilogoFinale riepilogo = new RiepilogoFinale(this.partita.getGiocatore().getNome(),
                this.partita.getGiocatore().getLivello(), this.partita.getTotalePiani());
        VistaFineGioco vista = new VistaFineGioco(DIMENSIONI_FINESTRA, riepilogo, this::mostraMenu);
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