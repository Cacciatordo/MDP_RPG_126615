package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SchedaCombattente {

    public static final double LARGHEZZA = 260;

    private static final double LARGHEZZA_BARRA = 230;
    private static final double SPAZIO = 4;
    private static final int DIMENSIONE_NOME = 10;
    private static final int DIMENSIONE_HP = 9;
    private static final int DIMENSIONE_STATISTICHE = 8;

    private final Combattente combattente;
    private final Label etichettaNome = creaEtichetta(DIMENSIONE_NOME, Tema.TESTO);
    private final ProgressBar barraHp = new ProgressBar(1.0);
    private final Label etichettaHp = creaEtichetta(DIMENSIONE_HP, Tema.TESTO);
    private final Label etichettaStatistiche = creaEtichetta(DIMENSIONE_STATISTICHE, Tema.ORO);
    private final VBox contenitore;

    public SchedaCombattente(Combattente combattente, String coloreBarra) {
        this.combattente = combattente;
        this.barraHp.setPrefWidth(LARGHEZZA_BARRA);
        this.barraHp.setStyle("-fx-accent: " + coloreBarra + "; -fx-control-inner-background: " + Tema.SFONDO + "; "
                + "-fx-background-radius: 0; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2;");

        this.contenitore = new VBox(SPAZIO, this.etichettaNome, this.barraHp,
                this.etichettaHp, this.etichettaStatistiche);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setMinWidth(LARGHEZZA);
        this.contenitore.setPrefWidth(LARGHEZZA);
        this.contenitore.setMaxWidth(LARGHEZZA);
        aggiorna();
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna() {
        this.etichettaNome.setText(creaNome());
        this.barraHp.setProgress((double) this.combattente.getHp() / this.combattente.getHpMax());
        this.etichettaHp.setText("HP: " + this.combattente.getHp() + " / " + this.combattente.getHpMax());
        this.etichettaStatistiche.setText(creaStatistiche());
    }

    private String creaNome() {
        if (this.combattente instanceof Livellabile livellabile) {
            return this.combattente.getNome() + " (Liv. " + livellabile.getLivello() + ")";
        }
        return this.combattente.getNome();
    }

    private String creaStatistiche() {
        if (this.combattente instanceof Livellabile) {
            return "⚔ Attacco: " + this.combattente.getAttacco() + "  |  🛡 Scudo: " + this.combattente.getDifesa();
        }
        return "⚔ Potenza: " + this.combattente.getAttacco() + "  |  🛡 Armatura: " + this.combattente.getDifesa();
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setWrapText(true);
        etichetta.setTextAlignment(TextAlignment.CENTER);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setMaxWidth(LARGHEZZA);
        return etichetta;
    }
}