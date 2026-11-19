package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.ScenarioCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Window;

public class VistaCorridoio {

    private static final double Y_PIEDI_EROE = 384;
    private static final double SCARTO_PAVIMENTO_ENTITA = 6;
    private static final double ALTEZZA_SCENARIO = 460;
    private static final double X_INGRESSO_DA_SOTTO = 80;
    private static final double DISTANZA_INGRESSO_DA_SOPRA = 140;
    private static final double MARGINE_USCITA_DESTRA = 120;
    private static final double LIMITE_SCALA_DISCESA = 44;
    private static final double DISTANZA_SFIDA = 90;
    private static final double LARGHEZZA_PULSANTE_SFIDA = 190;
    private static final double SCARTO_PULSANTE_SFIDA = 12;
    private static final int DIMENSIONE_PULSANTE_SFIDA = 9;
    private static final String TESTO_SFIDA_MOSTRO = "⚔ SFIDA MOSTRO";
    private static final String TESTO_SFIDA_BOSS = "⚔ SFIDA BOSS";

    private final double larghezza;
    private final Partita partita;
    private final EventiCorridoio eventi;
    private final UscitaCorridoio uscita;
    private final ScalaDiscesaCorridoio scalaDiscesa;
    private final EntitaCorridoio entitaCentrale;
    private final EroeNelCorridoio eroe;
    private final PannelloConferma pannelloConferma = new PannelloConferma();
    private final BarraSuperioreCorridoio barraSuperiore;
    private final Button pulsanteSfida = new Button();
    private final Scene scena;

    private final ArrivoNelCorridoio arrivo;
    private boolean arrivoMostrato;

    public VistaCorridoio(DimensioniFinestra dimensioni, Partita partita, EventiCorridoio eventi,
                          IngressoNelCorridoio ingresso) {
        double larghezza = dimensioni.larghezza();
        double altezza = dimensioni.altezza();
        ArrivoNelCorridoio arrivo = ingresso.arrivo();
        double posizioneEroe = ingresso.posizioneEroe();
        this.larghezza = larghezza;
        this.partita = partita;
        this.eventi = eventi;
        this.arrivo = arrivo;
        this.barraSuperiore = new BarraSuperioreCorridoio(
                eventi.salvataggioRichiesto(),
                eventi.caricamentoRichiesto(),
                eventi.menuRichiesto());
        this.uscita = new UscitaCorridoio(larghezza, Y_PIEDI_EROE);
        this.scalaDiscesa = new ScalaDiscesaCorridoio(Y_PIEDI_EROE);
        this.entitaCentrale = new EntitaCorridoio(larghezza, Y_PIEDI_EROE + SCARTO_PAVIMENTO_ENTITA);
        this.eroe = new EroeNelCorridoio(larghezza, Y_PIEDI_EROE,
                posizioneIniziale(larghezza, arrivo, posizioneEroe), eventi.eroeSpostato());
        if (arrivo == ArrivoNelCorridoio.DA_SOTTO || arrivo == ArrivoNelCorridoio.DA_SOPRA) {
            this.eroe.nascondi();
        }
        preparaPulsanteSfida(this.entitaCentrale.getCentroX());
        this.uscita.aggiorna(partita);
        this.scalaDiscesa.aggiorna(partita);
        this.entitaCentrale.mostra(partita.getPianoCorrente());
        this.barraSuperiore.aggiorna(partita);

        StackPane radice = new StackPane(creaScenario(larghezza, altezza),
                creaInterfaccia(),
                this.pannelloConferma.getNodo());
        this.scena = new Scene(radice, larghezza, altezza);
        this.scena.setOnKeyPressed(evento -> {
            if (!this.pannelloConferma.getNodo().isVisible()) {
                this.eroe.tastoPremuto(evento.getCode());
            }
        });
        this.scena.setOnKeyReleased(evento -> this.eroe.tastoRilasciato(evento.getCode()));
        this.scena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));
    }

    private static double posizioneIniziale(double larghezza, ArrivoNelCorridoio arrivo, double posizioneEroe) {
        return switch (arrivo) {
            case DA_SOPRA -> larghezza - DISTANZA_INGRESSO_DA_SOPRA;
            case DOPO_SCONTRO -> posizioneEroe;
            default -> X_INGRESSO_DA_SOTTO;
        };
    }

    public Scene getScena() {
        return this.scena;
    }

    private Pane creaScenario(double larghezza, double altezza) {
        Pane zona = new Pane();
        zona.setLayoutY(BarraSuperioreCorridoio.ALTEZZA);
        zona.setPrefSize(larghezza, ALTEZZA_SCENARIO);
        zona.setMaxSize(larghezza, ALTEZZA_SCENARIO);
        zona.setClip(new Rectangle(larghezza, ALTEZZA_SCENARIO));
        zona.getChildren().add(ScenarioCorridoio.crea(larghezza, ALTEZZA_SCENARIO));
        zona.getChildren().add(this.uscita.getLivello());
        zona.getChildren().add(this.scalaDiscesa.getLivello());
        zona.getChildren().add(this.entitaCentrale.getLivello());
        zona.getChildren().add(this.pulsanteSfida);
        zona.getChildren().add(this.eroe.getNodo());

        Pane scenario = new Pane(zona);
        scenario.setPrefSize(larghezza, altezza);
        scenario.setMaxSize(larghezza, altezza);
        scenario.setClip(new Rectangle(larghezza, altezza));
        scenario.setStyle("-fx-background-color: " + Tema.SFONDO + ";");
        return scenario;
    }

    private BorderPane creaInterfaccia() {
        BorderPane interfaccia = new BorderPane();
        interfaccia.setPickOnBounds(false);
        interfaccia.setTop(this.barraSuperiore.getNodo());
        return interfaccia;
    }

    public double getPosizioneEroe() {
        return this.eroe.getPosizione();
    }

    public double getCentroEroe() {
        return this.eroe.getCentroX();
    }

    public double getCentroEntita() {
        return this.entitaCentrale.getCentroX();
    }

    public boolean isEroeAllUscita() {
        return this.eroe.getPosizione() >= this.larghezza - MARGINE_USCITA_DESTRA;
    }

    public boolean isEroeAllaScala() {
        return this.eroe.getPosizione() <= LIMITE_SCALA_DISCESA;
    }

    public boolean isConfermaVisibile() {
        return this.pannelloConferma.getNodo().isVisible();
    }

    public void fermaEroe() {
        this.eroe.fermaMovimento();
    }

    public void animaSalita(Runnable alTermine) {
        this.eroe.animaSalita(alTermine);
    }

    public void animaDiscesa(Runnable alTermine) {
        this.eroe.animaDiscesa(alTermine);
    }

    public void mostraConferma(RichiestaConferma richiesta, Runnable suConferma) {
        this.pannelloConferma.mostra(richiesta, suConferma);
    }

    public void mostraMessaggio(String messaggio) {
        this.barraSuperiore.mostraMessaggio(messaggio);
    }

    public void aggiornaDopoUso(String messaggio) {
        this.barraSuperiore.mostraMessaggio(messaggio);
        this.barraSuperiore.aggiorna(this.partita);
        this.entitaCentrale.aggiorna(this.partita.getPianoCorrente());
        this.uscita.aggiorna(this.partita);
    }

    public void aggiornaPulsanteSfida() {
        aggiornaPulsanteSfida(this.eroe.getCentroX());
    }

    private void preparaPulsanteSfida(double centroAvversario) {
        boolean boss = this.partita.getPianoCorrente().getTipo() != TipoPiano.COMBATTIMENTO;
        this.pulsanteSfida.setText(boss ? TESTO_SFIDA_BOSS : TESTO_SFIDA_MOSTRO);
        this.pulsanteSfida.setFont(Font.font(Tema.FONT, DIMENSIONE_PULSANTE_SFIDA));
        this.pulsanteSfida.setStyle("-fx-background-color: " + Tema.PANNELLO + "; -fx-text-fill: " + Tema.ORO + "; "
                + "-fx-border-color: " + Tema.ORO + "; -fx-border-width: 2; "
                + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 5 10;");
        this.pulsanteSfida.setCursor(Cursor.HAND);
        this.pulsanteSfida.setFocusTraversable(false);
        this.pulsanteSfida.setMinWidth(LARGHEZZA_PULSANTE_SFIDA);
        this.pulsanteSfida.setMaxWidth(LARGHEZZA_PULSANTE_SFIDA);
        this.pulsanteSfida.setLayoutX(Math.round(centroAvversario - LARGHEZZA_PULSANTE_SFIDA / 2));
        this.pulsanteSfida.setLayoutY(Y_PIEDI_EROE + SCARTO_PAVIMENTO_ENTITA + SCARTO_PULSANTE_SFIDA);
        this.pulsanteSfida.setOnAction(evento -> this.eventi.sfidaRichiesta().run());
        this.pulsanteSfida.setVisible(false);
        aggiornaPulsanteSfida(this.eroe.getCentroX());
    }

    private void aggiornaPulsanteSfida(double centroEroe) {
        boolean vicino = Math.abs(centroEroe - this.entitaCentrale.getCentroX()) <= DISTANZA_SFIDA;
        this.pulsanteSfida.setVisible(this.partita.getPianoCorrente().haNemico()
                && this.partita.isPianoSuperato() && vicino);
    }

    private void animaSoloSeMostrata(Window finestra) {
        if (finestra == null) {
            this.eroe.ferma();
            this.entitaCentrale.ferma();
        } else {
            this.eroe.avvia();
            this.entitaCentrale.avvia();
            mostraArrivo();
        }
    }

    private void mostraArrivo() {
        if (this.arrivoMostrato || (this.arrivo != ArrivoNelCorridoio.DA_SOTTO
                && this.arrivo != ArrivoNelCorridoio.DA_SOPRA)) {
            return;
        }
        this.arrivoMostrato = true;
        if (this.arrivo == ArrivoNelCorridoio.DA_SOTTO) {
            this.eroe.animaArrivoDaSotto(() -> { });
        } else {
            this.eroe.animaArrivoDaSopra(() -> { });
        }
    }
}