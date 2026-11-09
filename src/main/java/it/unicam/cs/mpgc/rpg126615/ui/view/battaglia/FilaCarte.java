package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FilaCarte {

    private static final double SPAZIO_CARTE = 10;
    private static final double SPAZIO_RIGHE = 6;
    private static final int DIMENSIONE_TESTO = 12;

    private final Giocatore giocatore;
    private final Label etichettaPa = new Label();
    private final HBox carte = new HBox(SPAZIO_CARTE);
    private final VBox contenitore;

    public FilaCarte(double larghezza, Giocatore giocatore) {
        this.giocatore = giocatore;
        this.etichettaPa.setFont(Font.font(Tema.FONT, DIMENSIONE_TESTO));
        this.etichettaPa.setTextFill(Color.web(Tema.ORO));
        this.carte.setAlignment(Pos.CENTER);

        this.contenitore = new VBox(SPAZIO_RIGHE, this.etichettaPa, this.carte);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setPrefWidth(larghezza);
        aggiorna();
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna() {
        this.etichettaPa.setText("PA " + this.giocatore.getPuntiAzione());
        this.carte.getChildren().clear();
        for (Carta carta : this.giocatore.getMano().getCarte()) {
            this.carte.getChildren().add(new VistaCarta(carta).getNodo());
        }
    }
}