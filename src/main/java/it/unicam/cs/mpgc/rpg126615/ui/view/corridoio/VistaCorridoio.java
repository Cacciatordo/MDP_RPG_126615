package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
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

public class VistaCorridoio {

    private static final double Y_PIEDI_EROE = 440;
    private static final double SPAZIO_BOTTONI = 15;
    private static final double MARGINE_BARRA = 10;
    private static final String STILE_BOTTONE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-font-family: '" + Tema.FONT + "'; "
            + "-fx-font-size: 12px; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 4 10;";

    private final EntitaCorridoio entitaCentrale;
    private final EroeNelCorridoio eroe;
    private final BarraStatoCorridoio barraStato = new BarraStatoCorridoio();
    private final Scene scena;

    public VistaCorridoio(double larghezza, double altezza, Partita partita, Runnable suEntra,
                          Runnable suProssimoPiano) {
        this.entitaCentrale = new EntitaCorridoio(larghezza, Y_PIEDI_EROE);
        this.eroe = new EroeNelCorridoio(larghezza, Y_PIEDI_EROE);
        this.entitaCentrale.mostra(partita.getPianoCorrente());
        this.barraStato.aggiorna(partita);

        StackPane radice = new StackPane(creaScenario(larghezza, altezza),
                creaInterfaccia(suEntra, suProssimoPiano));
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
        scenario.getChildren().add(this.entitaCentrale.getLivello());
        scenario.getChildren().add(this.eroe.getNodo());
        return scenario;
    }

    private BorderPane creaInterfaccia(Runnable suEntra, Runnable suProssimoPiano) {
        HBox bottoni = new HBox(SPAZIO_BOTTONI,
                creaBottone("Entra nel piano", suEntra),
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