package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SchedaCombattente {

    public static final double LARGHEZZA = 180;

    private static final double MARGINE = 8;
    private static final double SPAZIO = 4;
    private static final double LARGHEZZA_BARRA = LARGHEZZA - 2 * MARGINE;
    private static final int DIMENSIONE_NOME = 12;
    private static final int DIMENSIONE_TESTO = 11;
    private static final String STILE = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2;";

    private final Combattente combattente;
    private final Label etichettaNome = creaEtichetta(DIMENSIONE_NOME, Tema.ORO);
    private final ProgressBar barraHp = new ProgressBar(1.0);
    private final Label etichettaHp = creaEtichetta(DIMENSIONE_TESTO, Tema.TESTO);
    private final Label etichettaStatistiche = creaEtichetta(DIMENSIONE_TESTO, Tema.TESTO_SECONDARIO);
    private final VBox contenitore;

    public SchedaCombattente(Combattente combattente, String coloreBarra) {
        this.combattente = combattente;
        this.etichettaNome.setWrapText(true);
        this.etichettaNome.setTextAlignment(TextAlignment.CENTER);
        this.barraHp.setPrefWidth(LARGHEZZA_BARRA);
        this.barraHp.setStyle("-fx-accent: " + coloreBarra + ";");

        this.contenitore = new VBox(SPAZIO, this.etichettaNome, this.barraHp,
                this.etichettaHp, this.etichettaStatistiche);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setPadding(new Insets(MARGINE));
        this.contenitore.setPrefWidth(LARGHEZZA);
        this.contenitore.setMaxWidth(LARGHEZZA);
        this.contenitore.setStyle(STILE);
        aggiorna();
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna() {
        this.etichettaNome.setText(this.combattente.getNome());
        this.barraHp.setProgress((double) this.combattente.getHp() / this.combattente.getHpMax());
        this.etichettaHp.setText("HP " + this.combattente.getHp() + "/" + this.combattente.getHpMax());
        this.etichettaStatistiche.setText("ATT " + this.combattente.getAttacco()
                + "  DIF " + this.combattente.getDifesa());
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }
}