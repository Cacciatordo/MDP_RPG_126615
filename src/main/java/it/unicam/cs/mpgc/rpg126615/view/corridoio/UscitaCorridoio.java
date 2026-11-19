package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UscitaCorridoio {

    private static final int SCALA = 4;
    private static final String PORTA_CHIUSA = "doors_leaf_closed";
    private static final String SCALA_SU = "scala_sale";
    private static final double MARGINE_DESTRO = 10;
    private static final double LARGHEZZA_MINIMA = 90;
    private static final double SPAZIO_SCRITTE = 36;
    private static final double SCARTO_BLOCCATA = 16;
    private static final int DIMENSIONE_SCRITTA = 11;
    private static final int DIMENSIONE_BLOCCATA = 10;
    private static final double SCARTO_SCALA_SALITA = -65;
    private static final double DISTANZA_SCRITTA_SALITA = 24;
    private static final String TESTO_BLOCCATA = "BLOCCATA";
    private static final String TESTO_SALI = "SALI ▲";
    private static final String DESCRIZIONE_BLOCCATA = "Uscita verso il piano superiore: sconfiggi il pericolo per aprirla!";
    private static final String DESCRIZIONE_SALI = "Le scale portano al piano superiore.";
    private static final String STILE_SUGGERIMENTO = "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: 9px;";

    private final Pane livello = new Pane();
    private final Image immaginePorta = PixelArt.carica(PixelArt.AMBIENTE, PORTA_CHIUSA, SCALA);
    private final Image immagineScala = PixelArt.carica(PixelArt.AMBIENTE, SCALA_SU, SCALA);
    private final ImageView immagine = new ImageView(this.immaginePorta);
    private final Label scrittaSali = new Label(TESTO_SALI);
    private final Label scrittaBloccata = new Label(TESTO_BLOCCATA);
    private final Tooltip suggerimento = new Tooltip();
    private final StackPane contenitore = new StackPane(this.immagine, this.scrittaSali, this.scrittaBloccata);

    public UscitaCorridoio(double larghezzaScena, double yBase) {
        double larghezza = Math.max(this.immaginePorta.getWidth(), LARGHEZZA_MINIMA);
        double altezza = this.immaginePorta.getHeight() + SPAZIO_SCRITTE;

        this.livello.setPickOnBounds(false);
        this.immagine.setSmooth(false);
        this.scrittaSali.setFont(Font.font(Tema.FONT, DIMENSIONE_SCRITTA));
        this.scrittaSali.setTextFill(Color.web(Tema.ORO));
        this.scrittaBloccata.setFont(Font.font(Tema.FONT, DIMENSIONE_BLOCCATA));
        this.scrittaBloccata.setTextFill(Color.web(Tema.ERRORE));
        StackPane.setAlignment(this.scrittaBloccata, Pos.TOP_CENTER);
        StackPane.setMargin(this.scrittaBloccata, new Insets(SCARTO_BLOCCATA, 0, 0, 0));

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
        if (partita.isPassaggioLibero()) {
            mostraScala();
        } else {
            mostraPorta();
        }
    }

    private void mostraPorta() {
        this.immagine.setImage(this.immaginePorta);
        this.immagine.setTranslateY(0);
        StackPane.setAlignment(this.immagine, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(this.scrittaSali, Pos.TOP_CENTER);
        this.scrittaSali.setMinWidth(Region.USE_PREF_SIZE);
        this.scrittaSali.setMaxWidth(Region.USE_PREF_SIZE);
        this.scrittaSali.translateXProperty().unbind();
        this.scrittaSali.setTranslateX(0);
        this.scrittaSali.setTranslateY(0);
        this.scrittaBloccata.setVisible(true);
        this.suggerimento.setText(DESCRIZIONE_BLOCCATA);
    }

    private void mostraScala() {
        this.immagine.setImage(this.immagineScala);
        StackPane.setAlignment(this.immagine, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(this.scrittaSali, Pos.TOP_RIGHT);
        this.scrittaSali.setMinWidth(Region.USE_PREF_SIZE);
        this.scrittaSali.setMaxWidth(Region.USE_PREF_SIZE);
        this.scrittaSali.setTranslateX(0);
        this.scrittaSali.setTranslateY(this.immaginePorta.getHeight() + SPAZIO_SCRITTE
                + SCARTO_SCALA_SALITA - DISTANZA_SCRITTA_SALITA);
        this.immagine.setTranslateY(this.immagineScala.getHeight() + SCARTO_SCALA_SALITA);
        this.scrittaBloccata.setVisible(false);
        this.suggerimento.setText(DESCRIZIONE_SALI);
    }
}