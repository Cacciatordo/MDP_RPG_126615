package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class VistaPila {

    public static final double LARGHEZZA = VistaCarta.LARGHEZZA;
    public static final double ALTEZZA = 76;

    private static final double SCARTO_STRATO = 3;
    private static final int CARTE_PER_STRATO = 3;
    private static final double LARGHEZZA_FRONTE = LARGHEZZA - 2 * SCARTO_STRATO;
    private static final double ALTEZZA_FRONTE = ALTEZZA - 2 * SCARTO_STRATO;
    private static final double OPACITA_VUOTA = 0.45;
    private static final double INGRANDIMENTO_SCATTO = 1.3;
    private static final Duration DURATA_SCATTO = Duration.millis(140);
    private static final int DIMENSIONE_TITOLO = 8;
    private static final int DIMENSIONE_NUMERO = 16;
    private static final int DIMENSIONE_DETTAGLIO = 8;

    private final Label numero = creaEtichetta(DIMENSIONE_NUMERO, Tema.TESTO);
    private final Label dettaglio = creaEtichetta(DIMENSIONE_DETTAGLIO, Tema.TESTO_SECONDARIO);
    private final VBox fronte = new VBox(2);
    private final Region primoStrato = creaStrato(SCARTO_STRATO);
    private final Region secondoStrato = creaStrato(2 * SCARTO_STRATO);
    private final Tooltip suggerimento = new Tooltip();
    private final ScaleTransition scatto = creaScatto();
    private final StackPane contenitore;
    private int numeroPrecedente = -1;

    public VistaPila(String titolo) {
        Label etichettaTitolo = creaEtichetta(DIMENSIONE_TITOLO, Tema.BORDO);
        etichettaTitolo.setText(titolo);
        this.dettaglio.setTextOverrun(OverrunStyle.ELLIPSIS);
        this.dettaglio.setAlignment(Pos.CENTER);
        this.dettaglio.setMaxWidth(LARGHEZZA_FRONTE - 8);

        this.fronte.getChildren().addAll(etichettaTitolo, this.numero, this.dettaglio);
        this.fronte.setAlignment(Pos.CENTER);
        blocca(this.fronte, LARGHEZZA_FRONTE, ALTEZZA_FRONTE);

        this.contenitore = new StackPane(this.secondoStrato, this.primoStrato, this.fronte);
        this.contenitore.setAlignment(Pos.BOTTOM_LEFT);
        blocca(this.contenitore, LARGHEZZA, ALTEZZA);
        this.suggerimento.setShowDelay(Duration.millis(50));
        Tooltip.install(this.contenitore, this.suggerimento);
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna(int carte, String descrizione, String coloreBordo, String testoSuggerimento) {
        this.numero.setText(String.valueOf(carte));
        this.dettaglio.setText(descrizione);
        this.fronte.setStyle(stileFronte(coloreBordo));
        this.primoStrato.setVisible(carte > 1);
        this.secondoStrato.setVisible(carte > CARTE_PER_STRATO);
        this.contenitore.setOpacity(carte == 0 ? OPACITA_VUOTA : 1);
        this.suggerimento.setText(testoSuggerimento);
        if (this.numeroPrecedente >= 0 && carte != this.numeroPrecedente) {
            this.scatto.playFromStart();
        }
        this.numeroPrecedente = carte;
    }

    private ScaleTransition creaScatto() {
        ScaleTransition transizione = new ScaleTransition(DURATA_SCATTO, this.numero);
        transizione.setToX(INGRANDIMENTO_SCATTO);
        transizione.setToY(INGRANDIMENTO_SCATTO);
        transizione.setAutoReverse(true);
        transizione.setCycleCount(2);
        return transizione;
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }

    private static Region creaStrato(double scarto) {
        Region strato = new Region();
        blocca(strato, LARGHEZZA_FRONTE, ALTEZZA_FRONTE);
        strato.setStyle("-fx-background-color: " + Tema.PANNELLO + "; -fx-border-color: " + Tema.BORDO
                + "; -fx-border-width: 2;");
        strato.setTranslateX(scarto);
        strato.setTranslateY(-scarto);
        return strato;
    }

    private static void blocca(Region regione, double larghezza, double altezza) {
        regione.setMinSize(larghezza, altezza);
        regione.setPrefSize(larghezza, altezza);
        regione.setMaxSize(larghezza, altezza);
    }

    private static String stileFronte(String colore) {
        return "-fx-background-color: " + Tema.PANNELLO + "; -fx-border-color: " + colore + "; "
                + "-fx-border-width: 3;";
    }
}