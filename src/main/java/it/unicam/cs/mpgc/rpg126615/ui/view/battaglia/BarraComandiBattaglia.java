package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class BarraComandiBattaglia {

    private static final double SPAZIO = 15;
    private static final String STILE_BOTTONE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-font-family: '" + Tema.FONT + "'; "
            + "-fx-font-size: 12px; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 4 10;";

    private final Button bottoneAttacca;
    private final Button bottoneFineTurno;
    private final HBox contenitore;

    public BarraComandiBattaglia(double larghezza, Runnable suAttacca, Runnable suFineTurno) {
        this.bottoneAttacca = creaBottone("Attacca (" + Battaglia.COSTO_PA_ATTACCO_BASE + " PA)", suAttacca);
        this.bottoneFineTurno = creaBottone("Fine turno", suFineTurno);
        this.contenitore = new HBox(SPAZIO, this.bottoneAttacca, this.bottoneFineTurno);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setPrefWidth(larghezza);
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void abilita(boolean abilitati) {
        this.bottoneAttacca.setDisable(!abilitati);
        this.bottoneFineTurno.setDisable(!abilitati);
    }

    private static Button creaBottone(String testo, Runnable azione) {
        Button bottone = new Button(testo);
        bottone.setStyle(STILE_BOTTONE);
        bottone.setFocusTraversable(false);
        bottone.setOnAction(e -> azione.run());
        return bottone;
    }
}