package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BarraStatoCorridoio {

    public static final String STILE = "-fx-background-color: rgba(10, 6, 8, 0.75);";

    private static final String STILE_BARRA_HP = "-fx-accent: " + Tema.VERDE + ";";
    private static final String TESTO_GUIDA = "Usa [A / D] oppure le frecce sinistra e destra per camminare";
    private static final int DIMENSIONE_TITOLO = 20;
    private static final int DIMENSIONE_NOME = 14;
    private static final int DIMENSIONE_TESTO = 13;
    private static final double LARGHEZZA_BARRA_HP = 200;
    private static final double LARGHEZZA_MASSIMA_MESSAGGIO = 780;
    private static final double SPAZIO_STATO = 12;
    private static final double SPAZIO_RIGHE = 6;
    private static final double MARGINE = 10;

    private final Label etichettaPiano = creaEtichetta(DIMENSIONE_TITOLO, Tema.ORO);
    private final Label etichettaEroe = creaEtichetta(DIMENSIONE_NOME, Tema.TESTO);
    private final ProgressBar barraHp = new ProgressBar(1.0);
    private final Label etichettaHp = creaEtichetta(DIMENSIONE_TESTO, Tema.TESTO);
    private final Label etichettaMessaggio = creaEtichetta(DIMENSIONE_TESTO, Tema.ORO);
    private final VBox contenitore;

    public BarraStatoCorridoio() {
        Label guida = creaEtichetta(DIMENSIONE_TESTO, Tema.TESTO_SECONDARIO);
        guida.setText(TESTO_GUIDA);
        this.barraHp.setPrefWidth(LARGHEZZA_BARRA_HP);
        this.barraHp.setStyle(STILE_BARRA_HP);

        this.etichettaMessaggio.setWrapText(true);
        this.etichettaMessaggio.setMaxWidth(LARGHEZZA_MASSIMA_MESSAGGIO);
        this.etichettaMessaggio.setTextAlignment(TextAlignment.CENTER);
        this.etichettaMessaggio.setVisible(false);
        this.etichettaMessaggio.setManaged(false);

        HBox stato = new HBox(SPAZIO_STATO, this.etichettaEroe, this.barraHp, this.etichettaHp);
        stato.setAlignment(Pos.CENTER);

        this.contenitore = new VBox(SPAZIO_RIGHE, this.etichettaPiano, stato, guida, this.etichettaMessaggio);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setPadding(new Insets(MARGINE));
        this.contenitore.setStyle(STILE);
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna(Partita partita) {
        Giocatore giocatore = partita.getGiocatore();
        this.etichettaPiano.setText("Piano " + partita.getNumeroPianoCorrente() + " di " + partita.getTotalePiani());
        this.etichettaEroe.setText(giocatore.getNome() + "  Liv " + giocatore.getLivello());
        this.barraHp.setProgress(Math.max(0, (double) giocatore.getHp() / giocatore.getHpMax()));
        this.etichettaHp.setText("HP " + giocatore.getHp() + "/" + giocatore.getHpMax());
    }

    public void mostraMessaggio(String messaggio) {
        this.etichettaMessaggio.setText(messaggio);
        this.etichettaMessaggio.setVisible(true);
        this.etichettaMessaggio.setManaged(true);
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }
}