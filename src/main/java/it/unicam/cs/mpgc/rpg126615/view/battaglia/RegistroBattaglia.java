package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

public class RegistroBattaglia {

    public static final double ALTEZZA = 112;

    private static final int DIMENSIONE_TESTO = 9;
    private static final String STILE = "-fx-control-inner-background: " + Tema.SFONDO + "; "
            + "-fx-text-fill: " + Tema.ORO + "; "
            + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_TESTO + "px; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 3; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-highlight-fill: #7a1220;";

    private final TextArea area = new TextArea();

    public RegistroBattaglia(double larghezza) {
        this.area.setEditable(false);
        this.area.setWrapText(true);
        this.area.setFocusTraversable(false);
        this.area.setPrefSize(larghezza, ALTEZZA);
        this.area.setMinSize(larghezza, ALTEZZA);
        this.area.setMaxSize(larghezza, ALTEZZA);
        this.area.setStyle(STILE);
    }

    public Node getNodo() {
        return this.area;
    }

    public void aggiungi(String testo) {
        this.area.appendText(testo);
        this.area.setScrollTop(Double.MAX_VALUE);
    }
}