package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class EffettoColpo {

    private static final Duration DURATA_AVANZATA = Duration.millis(140);
    private static final Duration DURATA_RITORNO = Duration.millis(220);
    private static final Duration DURATA_TESTO = Duration.millis(900);
    private static final double DISTANZA_AFFONDO = 80;
    private static final double AMPIEZZA_SCOSSA = 7;
    private static final double OPACITA_LAMPEGGIO = 0.45;
    private static final double SALITA_TESTO = 50;
    private static final double LARGHEZZA_TESTO = 180;
    private static final int DIMENSIONE_TESTO = 18;
    private static final String CHIAVE_ANIMAZIONE = "animazioneColpo";

    private final Pane livello;

    public EffettoColpo(Pane livello) {
        this.livello = livello;
    }

    public void colpisci(Node attaccante, int verso, Node bersaglio, EsitoColpo esito) {
        double spostamento = verso * DISTANZA_AFFONDO;
        TranslateTransition avanzata = new TranslateTransition(DURATA_AVANZATA, attaccante);
        avanzata.setFromX(0);
        avanzata.setToX(spostamento);
        avanzata.setOnFinished(e -> reagisciAlColpo(bersaglio, esito));

        TranslateTransition ritorno = new TranslateTransition(DURATA_RITORNO, attaccante);
        ritorno.setFromX(spostamento);
        ritorno.setToX(0);

        avviaSostituendoLaPrecedente(attaccante, new SequentialTransition(avanzata, ritorno));
    }

    private void reagisciAlColpo(Node bersaglio, EsitoColpo esito) {
        avviaSostituendoLaPrecedente(bersaglio, creaScossa(bersaglio));
        mostraTestoFluttuante(bersaglio, esito.testo(), esito.colore());
    }

    private Animation creaScossa(Node bersaglio) {
        return new Timeline(
                fotogramma(0, bersaglio, 0, 1),
                fotogramma(50, bersaglio, -AMPIEZZA_SCOSSA, OPACITA_LAMPEGGIO),
                fotogramma(100, bersaglio, AMPIEZZA_SCOSSA, 1),
                fotogramma(150, bersaglio, -AMPIEZZA_SCOSSA, OPACITA_LAMPEGGIO),
                fotogramma(200, bersaglio, AMPIEZZA_SCOSSA / 2, 1),
                fotogramma(250, bersaglio, 0, 1));
    }

    private static KeyFrame fotogramma(double millisecondi, Node nodo, double spostamentoX, double opacita) {
        return new KeyFrame(Duration.millis(millisecondi),
                new KeyValue(nodo.translateXProperty(), spostamentoX),
                new KeyValue(nodo.opacityProperty(), opacita));
    }

    private void mostraTestoFluttuante(Node bersaglio, String testo, String colore) {
        Bounds area = bersaglio.getBoundsInParent();
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, DIMENSIONE_TESTO));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setPrefWidth(LARGHEZZA_TESTO);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setMouseTransparent(true);
        etichetta.setLayoutX(Math.round(area.getCenterX() - LARGHEZZA_TESTO / 2));
        etichetta.setLayoutY(Math.round(area.getMinY()));
        this.livello.getChildren().add(etichetta);

        TranslateTransition sale = new TranslateTransition(DURATA_TESTO, etichetta);
        sale.setByY(-SALITA_TESTO);
        FadeTransition svanisce = new FadeTransition(DURATA_TESTO, etichetta);
        svanisce.setFromValue(1);
        svanisce.setToValue(0);

        ParallelTransition movimento = new ParallelTransition(sale, svanisce);
        movimento.setOnFinished(e -> this.livello.getChildren().remove(etichetta));
        movimento.play();
    }

    private static void avviaSostituendoLaPrecedente(Node nodo, Animation nuova) {
        Object precedente = nodo.getProperties().put(CHIAVE_ANIMAZIONE, nuova);
        if (precedente instanceof Animation animazione) {
            animazione.stop();
        }
        nuova.play();
    }
}