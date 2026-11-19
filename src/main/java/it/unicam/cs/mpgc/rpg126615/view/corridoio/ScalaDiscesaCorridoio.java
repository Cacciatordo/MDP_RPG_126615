package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ScalaDiscesaCorridoio {

    private static final int SCALA = 4;
    private static final String SCALA_GIU = "scala_scende";
    private static final double X_SCALA = -10;
    private static final double X_SCRITTA = 6;
    private static final double SCARTO_SCALA_SOTTO_PAVIMENTO = 8;
    private static final double SCARTO_SCRITTA_SOPRA_PAVIMENTO = 24;
    private static final int DIMENSIONE_SCRITTA = 10;
    private static final String TESTO_SCENDI = "SCENDI ▼";
    private static final String DESCRIZIONE = "Le scale portano al piano inferiore.";
    private static final String STILE_SUGGERIMENTO = "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: 9px;";

    private final Pane livello = new Pane();
    private final ImageView immagine;
    private final Label scritta = new Label(TESTO_SCENDI);

    public ScalaDiscesaCorridoio(double yBase) {
        Image immagineScala = PixelArt.carica(PixelArt.AMBIENTE, SCALA_GIU, SCALA);
        this.immagine = new ImageView(immagineScala);
        this.immagine.setSmooth(false);
        this.immagine.setScaleX(-1);
        this.immagine.setLayoutX(X_SCALA);
        this.immagine.setLayoutY(yBase + SCARTO_SCALA_SOTTO_PAVIMENTO);

        Tooltip suggerimento = new Tooltip(DESCRIZIONE);
        suggerimento.setStyle(STILE_SUGGERIMENTO);
        Tooltip.install(this.immagine, suggerimento);

        this.scritta.setFont(Font.font(Tema.FONT, DIMENSIONE_SCRITTA));
        this.scritta.setTextFill(Color.web(Tema.TESTO_SECONDARIO));
        this.scritta.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        this.scritta.setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
        this.scritta.setLayoutX(X_SCRITTA);
        this.scritta.setLayoutY(yBase - SCARTO_SCRITTA_SOPRA_PAVIMENTO);
        this.scritta.setMouseTransparent(true);

        this.livello.setPickOnBounds(false);
        this.livello.getChildren().addAll(this.immagine, this.scritta);
    }

    public Pane getLivello() {
        return this.livello;
    }

    public void aggiorna(Partita partita) {
        boolean visibile = partita.getNumeroPianoCorrente() > 1;
        this.immagine.setVisible(visibile);
        this.scritta.setVisible(visibile);
    }
}