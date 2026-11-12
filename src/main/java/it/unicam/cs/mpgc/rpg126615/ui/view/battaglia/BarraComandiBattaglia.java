package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BarraComandiBattaglia {

    public static final double ALTEZZA = 44;

    private static final double LARGHEZZA_COMANDO = 220;
    private static final double SPAZIO = 20;
    private static final double SPAZIO_PA = 10;
    private static final int DIMENSIONE_TITOLO_PA = 8;
    private static final int DIMENSIONE_PA = 12;
    private static final int DIMENSIONE_BOTTONE = 10;
    private static final String PANNELLO_SOPRA = "#3d2a1e";
    private static final String STILE_SCATOLA_PA = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.ORO + "; -fx-border-width: 2;";

    private final Giocatore giocatore;
    private final Label etichettaPa = creaEtichetta(DIMENSIONE_PA, Tema.ORO);
    private final Button bottoneFineTurno = new Button("⏭ Termina Turno");
    private final HBox contenitore;

    public BarraComandiBattaglia(double larghezza, Giocatore giocatore, Runnable suFineTurno) {
        this.giocatore = giocatore;

        Label titoloPa = creaEtichetta(DIMENSIONE_TITOLO_PA, Tema.TESTO);
        titoloPa.setText("PUNTI AZIONE");
        HBox scatolaPa = new HBox(SPAZIO_PA, titoloPa, this.etichettaPa);
        scatolaPa.setAlignment(Pos.CENTER);
        scatolaPa.setStyle(STILE_SCATOLA_PA);
        blocca(scatolaPa);

        this.bottoneFineTurno.setStyle(stileBottone(Tema.PANNELLO, Tema.BORDO));
        this.bottoneFineTurno.setCursor(Cursor.HAND);
        this.bottoneFineTurno.setFocusTraversable(false);
        this.bottoneFineTurno.setTooltip(new Tooltip("Passa il turno: tocca al nemico."));
        this.bottoneFineTurno.setOnMouseEntered(e -> this.bottoneFineTurno.setStyle(stileBottone(PANNELLO_SOPRA, Tema.ORO)));
        this.bottoneFineTurno.setOnMouseExited(e -> this.bottoneFineTurno.setStyle(stileBottone(Tema.PANNELLO, Tema.BORDO)));
        this.bottoneFineTurno.setOnAction(e -> suFineTurno.run());
        blocca(this.bottoneFineTurno);

        this.contenitore = new HBox(SPAZIO, scatolaPa, this.bottoneFineTurno);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setPrefWidth(larghezza);
        aggiorna();
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna() {
        this.etichettaPa.setText("⚡ " + this.giocatore.getPuntiAzione() + " / " + Giocatore.PA_PER_TURNO);
    }

    public void abilita(boolean abilitati) {
        this.bottoneFineTurno.setDisable(!abilitati);
    }

    private static void blocca(Region regione) {
        regione.setMinSize(LARGHEZZA_COMANDO, ALTEZZA);
        regione.setPrefSize(LARGHEZZA_COMANDO, ALTEZZA);
        regione.setMaxSize(LARGHEZZA_COMANDO, ALTEZZA);
    }

    private static String stileBottone(String sfondo, String bordo) {
        return "-fx-background-color: " + sfondo + "; -fx-text-fill: " + Tema.TESTO + "; "
                + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_BOTTONE + "px; "
                + "-fx-border-color: " + bordo + "; -fx-border-width: 2; "
                + "-fx-background-radius: 0; -fx-border-radius: 0;";
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }
}