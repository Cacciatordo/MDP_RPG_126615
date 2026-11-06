package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PannelloConferma {

    private static final double LARGHEZZA = 420;
    private static final double SPAZIO = 16;
    private static final double MARGINE = 20;
    private static final int DIMENSIONE_TITOLO = 14;
    private static final int DIMENSIONE_TESTO = 11;
    private static final String SFONDO_OSCURATO = "-fx-background-color: rgba(0, 0, 0, 0.6);";
    private static final String STILE_RIQUADRO = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 3;";
    private static final String STILE_BOTTONE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-font-family: '" + Tema.FONT + "'; "
            + "-fx-font-size: 12px; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 4 10;";

    private final StackPane radice = new StackPane();
    private final Label titolo = creaEtichetta(DIMENSIONE_TITOLO, Tema.ORO);
    private final Label testo = creaEtichetta(DIMENSIONE_TESTO, Tema.TESTO);
    private Runnable azioneConferma;

    public PannelloConferma() {
        this.testo.setWrapText(true);
        this.testo.setTextAlignment(TextAlignment.CENTER);

        Button conferma = creaBottone("Conferma");
        conferma.setOnAction(e -> conferma());
        Button annulla = creaBottone("Annulla");
        annulla.setOnAction(e -> nascondi());
        HBox bottoni = new HBox(SPAZIO, conferma, annulla);
        bottoni.setAlignment(Pos.CENTER);

        VBox riquadro = new VBox(SPAZIO, this.titolo, this.testo, bottoni);
        riquadro.setAlignment(Pos.CENTER);
        riquadro.setPadding(new Insets(MARGINE));
        riquadro.setStyle(STILE_RIQUADRO);
        riquadro.setPrefWidth(LARGHEZZA);
        riquadro.setMaxSize(LARGHEZZA, Region.USE_PREF_SIZE);

        this.radice.getChildren().add(riquadro);
        this.radice.setStyle(SFONDO_OSCURATO);
        this.radice.setVisible(false);
    }

    public Node getNodo() {
        return this.radice;
    }

    public void mostra(String titoloPannello, String testoPannello, Runnable suConferma) {
        this.titolo.setText(titoloPannello);
        this.testo.setText(testoPannello);
        this.azioneConferma = suConferma;
        this.radice.setVisible(true);
    }

    private void conferma() {
        nascondi();
        this.azioneConferma.run();
    }

    private void nascondi() {
        this.radice.setVisible(false);
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }

    private static Button creaBottone(String testo) {
        Button bottone = new Button(testo);
        bottone.setStyle(STILE_BOTTONE);
        bottone.setFocusTraversable(false);
        return bottone;
    }
}