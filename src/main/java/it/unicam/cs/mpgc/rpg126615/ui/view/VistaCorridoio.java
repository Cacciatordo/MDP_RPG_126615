package it.unicam.cs.mpgc.rpg126615.ui.view;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Window;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class VistaCorridoio {

    private static final int SCALA_AMBIENTE = 4;
    private static final int LATO_MATTONELLA = 16;
    private static final int LATO = LATO_MATTONELLA * SCALA_AMBIENTE;
    private static final int RIGHE_MURO = 6;
    private static final int SEME = 7;
    private static final String MURO = "wall_mid";
    private static final List<String> PAVIMENTO = List.of("floor_1", "floor_2", "floor_3");

    private static final int SCALA_EROE = 3;
    private static final String EROE_FERMO = "knight_m_idle_anim";
    private static final String EROE_CORSA = "knight_m_run_anim";
    private static final double Y_PIEDI_EROE = 440;
    private static final double MARGINE_CAMMINO = 20;
    private static final double POSIZIONE_INIZIALE_EROE = 0.2;
    private static final double VELOCITA_EROE = 220;
    private static final double NANOSECONDI_PER_SECONDO = 1_000_000_000.0;

    private static final String STILE_BARRA = "-fx-background-color: rgba(10, 6, 8, 0.75);";
    private static final String STILE_BOTTONE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-font-family: '" + Tema.FONT + "'; "
            + "-fx-font-size: 12px; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 4 10;";
    private static final String STILE_BARRA_HP = "-fx-accent: " + Tema.VERDE + ";";

    private final double larghezza;
    private final Label etichettaPiano = creaEtichetta(20, Tema.ORO);
    private final Label etichettaEroe = creaEtichetta(14, Tema.TESTO);
    private final ProgressBar barraHp = new ProgressBar(1.0);
    private final Label etichettaHp = creaEtichetta(13, Tema.TESTO);
    private final Label etichettaGuida = creaEtichetta(13, Tema.TESTO_SECONDARIO);
    private final SpriteAnimato spriteFermo = new SpriteAnimato(PixelArt.PERSONAGGI, EROE_FERMO, SCALA_EROE);
    private final SpriteAnimato spriteCorsa = new SpriteAnimato(PixelArt.PERSONAGGI, EROE_CORSA, SCALA_EROE);
    private final StackPane postazioneEroe = new StackPane(this.spriteFermo, this.spriteCorsa);
    private final Set<KeyCode> tastiPremuti = EnumSet.noneOf(KeyCode.class);
    private final AnimationTimer movimento = creaMovimento();
    private final Scene scena;

    private double posizioneEroe;

    public VistaCorridoio(double larghezza, double altezza, Partita partita, Runnable suEntra,
                          Runnable suProssimoPiano) {
        this.larghezza = larghezza;
        this.posizioneEroe = larghezza * POSIZIONE_INIZIALE_EROE;
        StackPane radice = new StackPane(creaScenario(larghezza, altezza),
                creaInterfaccia(suEntra, suProssimoPiano));
        this.scena = new Scene(radice, larghezza, altezza);
        this.scena.setOnKeyPressed(evento -> this.tastiPremuti.add(evento.getCode()));
        this.scena.setOnKeyReleased(evento -> this.tastiPremuti.remove(evento.getCode()));
        this.scena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));
        mostraPartita(partita);
    }

    public Scene getScena() {
        return this.scena;
    }

    private Pane creaScenario(double larghezzaScena, double altezzaScena) {
        Pane scenario = new Pane();
        scenario.setPrefSize(larghezzaScena, altezzaScena);
        scenario.setMaxSize(larghezzaScena, altezzaScena);
        scenario.setClip(new Rectangle(larghezzaScena, altezzaScena));
        aggiungiSfondo(scenario, larghezzaScena, altezzaScena);

        this.spriteCorsa.setVisible(false);
        this.postazioneEroe.setAlignment(Pos.BOTTOM_CENTER);
        this.postazioneEroe.setLayoutY(Y_PIEDI_EROE - this.spriteFermo.getImage().getHeight());
        this.postazioneEroe.setLayoutX(Math.round(this.posizioneEroe));
        scenario.getChildren().add(this.postazioneEroe);
        return scenario;
    }

    private void aggiungiSfondo(Pane scenario, double larghezzaScena, double altezzaScena) {
        Image muro = PixelArt.carica(PixelArt.AMBIENTE, MURO, SCALA_AMBIENTE);
        List<Image> pavimenti = PAVIMENTO.stream()
                .map(nome -> PixelArt.carica(PixelArt.AMBIENTE, nome, SCALA_AMBIENTE))
                .toList();
        Random caso = new Random(SEME);
        int colonne = (int) Math.ceil(larghezzaScena / LATO);
        int righe = (int) Math.ceil(altezzaScena / LATO);
        for (int riga = 0; riga < righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                Image mattonella = riga < RIGHE_MURO ? muro : pavimenti.get(caso.nextInt(pavimenti.size()));
                ImageView vista = new ImageView(mattonella);
                vista.setSmooth(false);
                vista.setLayoutX(colonna * LATO);
                vista.setLayoutY(riga * LATO);
                scenario.getChildren().add(vista);
            }
        }
    }

    private BorderPane creaInterfaccia(Runnable suEntra, Runnable suProssimoPiano) {
        this.etichettaGuida.setText("Usa [A / D] oppure le frecce sinistra e destra per camminare");
        this.barraHp.setPrefWidth(200);
        this.barraHp.setStyle(STILE_BARRA_HP);
        HBox stato = new HBox(12, this.etichettaEroe, this.barraHp, this.etichettaHp);
        stato.setAlignment(Pos.CENTER);
        VBox superiore = new VBox(6, this.etichettaPiano, stato, this.etichettaGuida);
        superiore.setAlignment(Pos.CENTER);
        superiore.setPadding(new Insets(10));
        superiore.setStyle(STILE_BARRA);

        Button entra = creaBottone("Entra nel piano", suEntra);
        Button prossimo = creaBottone("Piano successivo", suProssimoPiano);
        HBox inferiore = new HBox(15, entra, prossimo);
        inferiore.setAlignment(Pos.CENTER);
        inferiore.setPadding(new Insets(10));
        inferiore.setStyle(STILE_BARRA);

        BorderPane interfaccia = new BorderPane();
        interfaccia.setPickOnBounds(false);
        interfaccia.setTop(superiore);
        interfaccia.setBottom(inferiore);
        return interfaccia;
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }

    private static Button creaBottone(String testo, Runnable azione) {
        Button bottone = new Button(testo);
        bottone.setStyle(STILE_BOTTONE);
        bottone.setFocusTraversable(false);
        bottone.setOnAction(e -> azione.run());
        return bottone;
    }

    private void mostraPartita(Partita partita) {
        Giocatore giocatore = partita.getGiocatore();
        this.etichettaPiano.setText("Piano " + partita.getNumeroPianoCorrente() + " di " + partita.getTotalePiani());
        this.etichettaEroe.setText(giocatore.getNome() + "  Liv " + giocatore.getLivello());
        this.barraHp.setProgress(Math.max(0, (double) giocatore.getHp() / giocatore.getHpMax()));
        this.etichettaHp.setText("HP " + giocatore.getHp() + "/" + giocatore.getHpMax());
    }

    private AnimationTimer creaMovimento() {
        return new AnimationTimer() {

            private long istantePrecedente;

            @Override
            public void handle(long istante) {
                if (this.istantePrecedente != 0) {
                    muoviEroe((istante - this.istantePrecedente) / NANOSECONDI_PER_SECONDO);
                }
                this.istantePrecedente = istante;
            }

            @Override
            public void stop() {
                super.stop();
                this.istantePrecedente = 0;
            }
        };
    }

    private void muoviEroe(double secondi) {
        boolean sinistra = this.tastiPremuti.contains(KeyCode.A) || this.tastiPremuti.contains(KeyCode.LEFT);
        boolean destra = this.tastiPremuti.contains(KeyCode.D) || this.tastiPremuti.contains(KeyCode.RIGHT);
        int direzione = (destra ? 1 : 0) - (sinistra ? 1 : 0);
        this.spriteFermo.setVisible(direzione == 0);
        this.spriteCorsa.setVisible(direzione != 0);
        if (direzione == 0) {
            return;
        }
        double minimo = MARGINE_CAMMINO;
        double massimo = this.larghezza - MARGINE_CAMMINO - this.spriteFermo.getImage().getWidth();
        this.posizioneEroe = Math.max(minimo, Math.min(massimo, this.posizioneEroe + direzione * VELOCITA_EROE * secondi));
        this.postazioneEroe.setLayoutX(Math.round(this.posizioneEroe));
        this.postazioneEroe.setScaleX(direzione);
    }

    private void animaSoloSeMostrata(Window finestra) {
        if (finestra == null) {
            this.movimento.stop();
            this.spriteFermo.ferma();
            this.spriteCorsa.ferma();
            this.tastiPremuti.clear();
        } else {
            this.spriteFermo.avvia();
            this.spriteCorsa.avvia();
            this.movimento.start();
        }
    }
}