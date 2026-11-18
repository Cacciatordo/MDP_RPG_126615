package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.ScenarioCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import it.unicam.cs.mpgc.rpg126615.ui.view.DimensioniFinestra;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Window;

import java.util.List;

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
    private static final RichiestaConferma RICHIESTA_CASSA = RichiestaConferma.standard("Antica cassa",
            "Vuoi aprirla? Potresti trovare un premio... o una trappola!");
    private static final RichiestaConferma RICHIESTA_FONTANA = new RichiestaConferma("Fontana di lava",
            "Vuoi curarti ora?", "SÌ, curami ora", "NO, ci penserò dopo");
    private static final List<TipoPiano> PIANI_CON_CASSA = List.of(TipoPiano.CASSA);
    private static final List<TipoPiano> PIANI_CON_FONTANA = List.of(TipoPiano.RIPOSO);
    private static final List<TipoPiano> PIANI_CON_NEMICO = List.of(
            TipoPiano.COMBATTIMENTO, TipoPiano.BOSS, TipoPiano.BOSS_FINALE);

    private final Partita partita;
    private final double larghezza;
    private final AzioniCorridoio azioni;
    private final DialoghiSalvataggio dialoghi;
    private final UscitaCorridoio uscita;
    private final ScalaDiscesaCorridoio scalaDiscesa;
    private final EntitaCorridoio entitaCentrale;
    private final EroeNelCorridoio eroe;
    private final InterazioneConPiano interazioneCassa;
    private final InterazioneConPiano interazioneFontana;
    private final InterazioneConPiano interazioneNemico;
    private final PannelloConferma pannelloConferma = new PannelloConferma();
    private final BarraSuperioreCorridoio barraSuperiore;
    private final Button pulsanteSfida = new Button();
    private final Scene scena;

    private final ArrivoNelCorridoio arrivo;
    private boolean arrivoMostrato;
    private boolean interazioneAvviata;
    private boolean richiestaMostrata;

    public VistaCorridoio(DimensioniFinestra dimensioni, Partita partita, DialoghiSalvataggio dialoghi,
                          AzioniCorridoio azioni, IngressoNelCorridoio ingresso) {
        double larghezza = dimensioni.larghezza();
        double altezza = dimensioni.altezza();
        ArrivoNelCorridoio arrivo = ingresso.arrivo();
        double posizioneEroe = ingresso.posizioneEroe();
        this.partita = partita;
        this.dialoghi = dialoghi;
        this.larghezza = larghezza;
        this.azioni = azioni;
        this.arrivo = arrivo;
        this.barraSuperiore = new BarraSuperioreCorridoio(() -> salva(), () -> carica(), azioni.suMenu());
        this.uscita = new UscitaCorridoio(larghezza, Y_PIEDI_EROE);
        this.scalaDiscesa = new ScalaDiscesaCorridoio(Y_PIEDI_EROE);
        this.entitaCentrale = new EntitaCorridoio(larghezza, Y_PIEDI_EROE + SCARTO_PAVIMENTO_ENTITA);
        this.eroe = new EroeNelCorridoio(larghezza, Y_PIEDI_EROE,
                posizioneIniziale(larghezza, arrivo, posizioneEroe), () -> controllaInterazioni());
        double centro = this.entitaCentrale.getCentroX();
        this.interazioneCassa = new InterazioneConPiano(partita, centro, PIANI_CON_CASSA);
        this.interazioneFontana = new InterazioneConPiano(partita, centro, PIANI_CON_FONTANA);
        this.interazioneNemico = new InterazioneConPiano(partita, centro, PIANI_CON_NEMICO);
        if (arrivo == ArrivoNelCorridoio.DA_SOTTO || arrivo == ArrivoNelCorridoio.DA_SOPRA) {
            this.eroe.nascondi();
        }
        preparaPulsanteSfida(centro);
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

    private void controllaInterazioni() {
        if (this.interazioneAvviata || this.pannelloConferma.getNodo().isVisible()) {
            return;
        }
        double centroEroe = this.eroe.getCentroX();
        aggiornaPulsanteSfida(centroEroe);
        boolean vicinoACassa = this.interazioneCassa.isDisponibile(centroEroe);
        boolean vicinoAFontana = this.interazioneFontana.isDisponibile(centroEroe);
        if (!vicinoACassa && !vicinoAFontana) {
            this.richiestaMostrata = false;
        }

        if (this.interazioneNemico.isDisponibile(centroEroe) && !this.partita.isPianoSuperato()) {
            avviaCombattimento();
        } else if (vicinoACassa && !this.richiestaMostrata) {
            chiediConferma(RICHIESTA_CASSA, this.interazioneCassa);
        } else if (vicinoAFontana && !this.richiestaMostrata) {
            chiediConferma(RICHIESTA_FONTANA, this.interazioneFontana);
        } else {
            controllaPassaggioDiPiano();
        }
    }

    private void avviaCombattimento() {
        this.interazioneAvviata = true;
        this.azioni.suCombatti().accept(this.eroe.getPosizione());
    }

    private void chiediConferma(RichiestaConferma richiesta, InterazioneConPiano interazione) {
        this.richiestaMostrata = true;
        this.eroe.fermaMovimento();
        this.pannelloConferma.mostra(richiesta, () -> usa(interazione));
    }

    private void controllaPassaggioDiPiano() {
        if (isUscitaRaggiunta()) {
            this.interazioneAvviata = true;
            this.eroe.animaSalita(this.azioni.suProssimoPiano());
        } else if (isScalaDiscesaRaggiunta()) {
            this.interazioneAvviata = true;
            this.eroe.animaDiscesa(this.azioni.suPianoPrecedente());
        }
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
        this.pulsanteSfida.setOnAction(evento -> riaffronta());
        this.pulsanteSfida.setVisible(false);
        aggiornaPulsanteSfida(this.eroe.getCentroX());
    }

    private void aggiornaPulsanteSfida(double centroEroe) {
        boolean vicino = Math.abs(centroEroe - this.entitaCentrale.getCentroX()) <= DISTANZA_SFIDA;
        this.pulsanteSfida.setVisible(this.partita.getPianoCorrente().haNemico()
                && this.partita.isPianoSuperato() && vicino);
    }

    private void riaffronta() {
        if (this.interazioneAvviata) {
            return;
        }
        this.interazioneAvviata = true;
        this.eroe.fermaMovimento();
        this.partita.riaffrontaAvversario();
        this.azioni.suCombatti().accept(this.eroe.getPosizione());
    }

    private boolean isUscitaRaggiunta() {
        return this.partita.haProssimoPiano()
                && this.partita.isPassaggioLibero()
                && this.eroe.getPosizione() >= this.larghezza - MARGINE_USCITA_DESTRA;
    }

    private boolean isScalaDiscesaRaggiunta() {
        return this.partita.getNumeroPianoCorrente() > 1
                && this.eroe.getPosizione() <= LIMITE_SCALA_DISCESA;
    }

    private void usa(InterazioneConPiano interazione) {
        this.barraSuperiore.mostraMessaggio(interazione.esegui());
        this.barraSuperiore.aggiorna(this.partita);
        this.entitaCentrale.aggiorna(this.partita.getPianoCorrente());
        this.uscita.aggiorna(this.partita);
    }

    private void salva() {
        this.dialoghi.salva(this.partita).ifPresent(messaggio -> this.barraSuperiore.mostraMessaggio(messaggio));
    }

    private void carica() {
        this.dialoghi.scegliDaCaricare().ifPresent(dati -> this.azioni.suPartitaCaricata().accept(dati));
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