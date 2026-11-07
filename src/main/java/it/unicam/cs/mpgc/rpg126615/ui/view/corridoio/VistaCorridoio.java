package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

import java.util.List;

public class VistaCorridoio {

    private static final double Y_PIEDI_EROE = 440;
    private static final double SPAZIO_BOTTONI = 15;
    private static final double MARGINE_BARRA = 10;
    private static final String TITOLO_FONTANA = "Fontana di lava";
    private static final String TESTO_FONTANA = "Vuoi riposare qui? Recupererai i tuoi HP.";
    private static final List<TipoPiano> PIANI_CON_CASSA = List.of(TipoPiano.CASSA);
    private static final List<TipoPiano> PIANI_CON_FONTANA = List.of(TipoPiano.RIPOSO);
    private static final List<TipoPiano> PIANI_CON_NEMICO = List.of(
            TipoPiano.COMBATTIMENTO, TipoPiano.BOSS, TipoPiano.BOSS_FINALE);
    private static final String STILE_BOTTONE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-font-family: '" + Tema.FONT + "'; "
            + "-fx-font-size: 12px; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 4 10;";

    private final Partita partita;
    private final Runnable suEntra;
    private final UscitaCorridoio uscita;
    private final EntitaCorridoio entitaCentrale;
    private final EroeNelCorridoio eroe;
    private final InterazioneConPiano interazioneCassa;
    private final InterazioneConPiano interazioneFontana;
    private final InterazioneConPiano interazioneNemico;
    private final Button bottoneCassa = creaBottone("Apri cassa", () -> apriCassa());
    private final Button bottoneFontana = creaBottone("Usa la fontana", () -> chiediConfermaFontana());
    private final Button bottoneCombatti = creaBottone("Combatti", () -> combatti());
    private final PannelloConferma pannelloConferma = new PannelloConferma();
    private final BarraStatoCorridoio barraStato = new BarraStatoCorridoio();
    private final Scene scena;

    private boolean battagliaAvviata;

    public VistaCorridoio(double larghezza, double altezza, Partita partita, Runnable suEntra,
                          Runnable suProssimoPiano) {
        this.partita = partita;
        this.suEntra = suEntra;
        this.uscita = new UscitaCorridoio(larghezza, Y_PIEDI_EROE);
        this.entitaCentrale = new EntitaCorridoio(larghezza, Y_PIEDI_EROE);
        this.eroe = new EroeNelCorridoio(larghezza, Y_PIEDI_EROE, () -> aggiornaBottoni());
        double centro = this.entitaCentrale.getCentroX();
        this.interazioneCassa = new InterazioneConPiano(partita, centro, PIANI_CON_CASSA);
        this.interazioneFontana = new InterazioneConPiano(partita, centro, PIANI_CON_FONTANA);
        this.interazioneNemico = new InterazioneConPiano(partita, centro, PIANI_CON_NEMICO);
        this.uscita.aggiorna(partita);
        this.entitaCentrale.mostra(partita.getPianoCorrente());
        this.barraStato.aggiorna(partita);
        aggiornaBottoni();

        StackPane radice = new StackPane(creaScenario(larghezza, altezza),
                creaInterfaccia(suProssimoPiano),
                this.pannelloConferma.getNodo());
        this.scena = new Scene(radice, larghezza, altezza);
        this.scena.setOnKeyPressed(evento -> this.eroe.tastoPremuto(evento.getCode()));
        this.scena.setOnKeyReleased(evento -> this.eroe.tastoRilasciato(evento.getCode()));
        this.scena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));
    }

    public Scene getScena() {
        return this.scena;
    }

    private Pane creaScenario(double larghezza, double altezza) {
        Pane scenario = new Pane();
        scenario.setPrefSize(larghezza, altezza);
        scenario.setMaxSize(larghezza, altezza);
        scenario.setClip(new Rectangle(larghezza, altezza));
        scenario.getChildren().add(ScenarioCorridoio.crea(larghezza, altezza));
        scenario.getChildren().add(this.uscita.getLivello());
        scenario.getChildren().add(this.entitaCentrale.getLivello());
        scenario.getChildren().add(this.eroe.getNodo());
        return scenario;
    }

    private BorderPane creaInterfaccia(Runnable suProssimoPiano) {
        HBox bottoni = new HBox(SPAZIO_BOTTONI,
                this.bottoneCassa,
                this.bottoneFontana,
                this.bottoneCombatti,
                creaBottone("Entra nel piano", this.suEntra),
                creaBottone("Piano successivo", suProssimoPiano));
        bottoni.setAlignment(Pos.CENTER);
        bottoni.setPadding(new Insets(MARGINE_BARRA));
        bottoni.setStyle(BarraStatoCorridoio.STILE);

        BorderPane interfaccia = new BorderPane();
        interfaccia.setPickOnBounds(false);
        interfaccia.setTop(this.barraStato.getNodo());
        interfaccia.setBottom(bottoni);
        return interfaccia;
    }

    private static Button creaBottone(String testo, Runnable azione) {
        Button bottone = new Button(testo);
        bottone.setStyle(STILE_BOTTONE);
        bottone.setFocusTraversable(false);
        bottone.setOnAction(e -> azione.run());
        return bottone;
    }

    private void apriCassa() {
        usa(this.interazioneCassa);
    }

    private void chiediConfermaFontana() {
        this.pannelloConferma.mostra(TITOLO_FONTANA, TESTO_FONTANA, () -> usa(this.interazioneFontana));
    }

    private void combatti() {
        this.battagliaAvviata = true;
        aggiornaBottoni();
        this.suEntra.run();
    }

    private void usa(InterazioneConPiano interazione) {
        this.barraStato.mostraMessaggio(interazione.esegui());
        this.barraStato.aggiorna(this.partita);
        this.entitaCentrale.aggiorna(this.partita.getPianoCorrente());
        this.uscita.aggiorna(this.partita);
        aggiornaBottoni();
    }

    private void aggiornaBottoni() {
        double centroEroe = this.eroe.getCentroX();
        mostraSe(this.bottoneCassa, this.interazioneCassa.isDisponibile(centroEroe));
        mostraSe(this.bottoneFontana, this.interazioneFontana.isDisponibile(centroEroe));
        mostraSe(this.bottoneCombatti,
                !this.battagliaAvviata && this.interazioneNemico.isDisponibile(centroEroe));
    }

    private static void mostraSe(Button bottone, boolean condizione) {
        bottone.setVisible(condizione);
        bottone.setManaged(condizione);
    }

    private void animaSoloSeMostrata(Window finestra) {
        if (finestra == null) {
            this.eroe.ferma();
            this.entitaCentrale.ferma();
        } else {
            this.eroe.avvia();
            this.entitaCentrale.avvia();
        }
    }
}