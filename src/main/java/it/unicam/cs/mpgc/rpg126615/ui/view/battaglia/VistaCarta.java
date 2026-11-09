package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class VistaCarta {

    public static final double LARGHEZZA = 112;
    public static final double ALTEZZA = 130;

    private static final double MARGINE = 6;
    private static final double SPAZIO = 4;
    private static final int DIMENSIONE_NOME = 9;
    private static final int DIMENSIONE_TESTO = 8;

    private final VBox contenitore;

    public VistaCarta(Carta carta) {
        String colore = StileCarta.coloreDi(carta.getTipo());
        Label costo = creaEtichetta("PA " + carta.getCostoPA(), DIMENSIONE_NOME, colore);
        Label nome = creaEtichetta(carta.getNome(), DIMENSIONE_NOME, Tema.TESTO);
        Label descrizione = creaEtichetta(carta.getDescrizione(), DIMENSIONE_TESTO, Tema.TESTO_SECONDARIO);

        this.contenitore = new VBox(SPAZIO, costo, nome, descrizione);
        this.contenitore.setPadding(new Insets(MARGINE));
        this.contenitore.setPrefSize(LARGHEZZA, ALTEZZA);
        this.contenitore.setMaxSize(LARGHEZZA, ALTEZZA);
        this.contenitore.setStyle("-fx-background-color: " + Tema.PANNELLO + "; "
                + "-fx-border-color: " + colore + "; -fx-border-width: 3;");
    }

    public Node getNodo() {
        return this.contenitore;
    }

    private static Label creaEtichetta(String testo, int dimensione, String colore) {
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setWrapText(true);
        etichetta.setMaxWidth(LARGHEZZA - 2 * MARGINE);
        return etichetta;
    }
}