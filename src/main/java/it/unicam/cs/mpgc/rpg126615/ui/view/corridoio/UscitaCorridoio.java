package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UscitaCorridoio {

    private static final int SCALA = 4;
    private static final String PORTA_CHIUSA = "doors_leaf_closed";
    private static final String SCALA_SU = "scala_sale";
    private static final double MARGINE_DESTRO = 10;
    private static final double SPAZIO_SCRITTA = 24;
    private static final int DIMENSIONE_SCRITTA = 9;
    private static final String TESTO_BLOCCATA = "BLOCCATA";
    private static final String TESTO_SALI = "SALI";
    private static final String DESCRIZIONE_BLOCCATA = "Uscita verso il piano superiore: sconfiggi il pericolo per aprirla!";
    private static final String DESCRIZIONE_SALI = "Le scale portano al piano superiore.";
    private static final String STILE_SUGGERIMENTO = "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: 9px;";

    private final Pane livello = new Pane();
    private final Image immaginePorta = PixelArt.carica(PixelArt.AMBIENTE, PORTA_CHIUSA, SCALA);
    private final Image immagineScala = PixelArt.carica(PixelArt.AMBIENTE, SCALA_SU, SCALA);
    private final ImageView immagine = new ImageView(this.immaginePorta);
    private final Label scritta = new Label();
    private final Tooltip suggerimento = new Tooltip();
    private final StackPane contenitore = new StackPane(this.immagine, this.scritta);

    public UscitaCorridoio(double larghezzaScena, double yBase) {
        double larghezza = this.immaginePorta.getWidth();
        double altezza = this.immaginePorta.getHeight() + SPAZIO_SCRITTA;

        this.livello.setPickOnBounds(false);
        this.immagine.setSmooth(false);
        this.scritta.setFont(Font.font(Tema.FONT, DIMENSIONE_SCRITTA));
        StackPane.setAlignment(this.immagine, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(this.scritta, Pos.TOP_CENTER);

        this.contenitore.setMinSize(larghezza, altezza);
        this.contenitore.setPrefSize(larghezza, altezza);
        this.contenitore.setMaxSize(larghezza, altezza);
        this.contenitore.setLayoutX(Math.round(larghezzaScena - MARGINE_DESTRO - larghezza));
        this.contenitore.setLayoutY(yBase - altezza);

        this.suggerimento.setStyle(STILE_SUGGERIMENTO);
        Tooltip.install(this.contenitore, this.suggerimento);
        this.livello.getChildren().add(this.contenitore);
    }

    public Pane getLivello() {
        return this.livello;
    }

    public void aggiorna(Partita partita) {
        this.contenitore.setVisible(partita.haProssimoPiano());
        if (partita.isPianoSuperato()) {
            mostraScala();
        } else {
            mostraPorta();
        }
    }

    private void mostraPorta() {
        this.immagine.setImage(this.immaginePorta);
        this.scritta.setText(TESTO_BLOCCATA);
        this.scritta.setTextFill(Color.web(Tema.TESTO_SECONDARIO));
        this.scritta.setTranslateY(0);
        this.suggerimento.setText(DESCRIZIONE_BLOCCATA);
    }

    private void mostraScala() {
        this.immagine.setImage(this.immagineScala);
        this.scritta.setText(TESTO_SALI);
        this.scritta.setTextFill(Color.web(Tema.ORO));
        this.scritta.setTranslateY(this.immaginePorta.getHeight() - this.immagineScala.getHeight());
        this.suggerimento.setText(DESCRIZIONE_SALI);
    }
}