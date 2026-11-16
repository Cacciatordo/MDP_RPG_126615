package it.unicam.cs.mpgc.rpg126615.ui.view.finale;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.ScenarioCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class VistaFineGioco {

    private static final String SPRITE_EROE = "knight_m_idle_anim";
    private static final int SCALA_EROE = 3;
    private static final double LARGHEZZA_RIQUADRO = 420;
    private static final double PADDING_RIQUADRO = 18;
    private static final double PADDING_SCHERMATA = 30;
    private static final double SPAZIO_TRA_ELEMENTI = 22;
    private static final double DISTANZA_EROE_DAL_FONDO = 150;
    private static final int DIMENSIONE_TITOLO = 28;
    private static final int DIMENSIONE_TESTO = 10;
    private static final int DIMENSIONE_BOTTONE = 10;
    private static final String ORO_SOPRA = "#ffd700";
    private static final String STILE_RIQUADRO = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 3; "
            + "-fx-background-radius: 0; -fx-border-radius: 0;";

    private final Scene scena;

    public VistaFineGioco(double larghezza, double altezza, String nomeEroe, int livello, int pianiScalati,
                          Runnable suMenu) {
        Label titolo = creaEtichetta("TORRE CONQUISTATA!", DIMENSIONE_TITOLO, Tema.ORO);
        Label sottotitolo = creaEtichetta("Il Boss della Torre è caduto:\nla tua ascesa è compiuta.",
                DIMENSIONE_TESTO, Tema.TESTO);
        Label riepilogo = creaEtichetta(nomeEroe + " (Liv. " + livello + ")\nha scalato tutti i "
                + pianiScalati + " piani della torre!", DIMENSIONE_TESTO, Tema.ORO);

        VBox riquadro = new VBox(12, sottotitolo, riepilogo, creaBottoneMenu(suMenu));
        riquadro.setAlignment(Pos.CENTER);
        riquadro.setPadding(new Insets(PADDING_RIQUADRO));
        riquadro.setMinWidth(LARGHEZZA_RIQUADRO);
        riquadro.setMaxWidth(LARGHEZZA_RIQUADRO);
        riquadro.setStyle(STILE_RIQUADRO);

        VBox contenuto = new VBox(SPAZIO_TRA_ELEMENTI, titolo, riquadro);
        contenuto.setAlignment(Pos.TOP_CENTER);
        contenuto.setPadding(new Insets(PADDING_SCHERMATA));

        SpriteAnimato eroe = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_EROE, SCALA_EROE);
        eroe.avvia();
        StackPane.setAlignment(eroe, Pos.BOTTOM_CENTER);
        StackPane.setMargin(eroe, new Insets(0, 0, DISTANZA_EROE_DAL_FONDO, 0));

        StackPane radice = new StackPane(ScenarioCorridoio.crea(larghezza, altezza), contenuto, eroe);
        this.scena = new Scene(radice, larghezza, altezza);
    }

    public Scene getScena() {
        return this.scena;
    }

    private static Label creaEtichetta(String testo, int dimensione, String colore) {
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setWrapText(true);
        etichetta.setTextAlignment(TextAlignment.CENTER);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setMaxWidth(LARGHEZZA_RIQUADRO - 2 * PADDING_RIQUADRO);
        return etichetta;
    }

    private static Button creaBottoneMenu(Runnable suMenu) {
        Button bottone = new Button("🏠 TORNA AL MENU PRINCIPALE");
        String normale = stileBottone(Tema.ORO);
        String sopra = stileBottone(ORO_SOPRA);
        bottone.setStyle(normale);
        bottone.setCursor(Cursor.HAND);
        bottone.setOnMouseEntered(evento -> bottone.setStyle(sopra));
        bottone.setOnMouseExited(evento -> bottone.setStyle(normale));
        bottone.setOnAction(evento -> suMenu.run());
        return bottone;
    }

    private static String stileBottone(String sfondo) {
        return "-fx-background-color: " + sfondo + "; -fx-text-fill: " + Tema.SFONDO + "; "
                + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_BOTTONE + "px; "
                + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
                + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 8 16;";
    }
}