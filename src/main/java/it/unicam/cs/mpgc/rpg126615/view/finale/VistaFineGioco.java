package it.unicam.cs.mpgc.rpg126615.view.finale;

import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.ScenarioCorridoio;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.view.stile.Componenti;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class VistaFineGioco {

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

    public VistaFineGioco(DimensioniFinestra dimensioni, RiepilogoFinale riepilogoFinale, Runnable suMenu) {
        Label titolo = creaEtichetta("TORRE CONQUISTATA!", DIMENSIONE_TITOLO, Tema.ORO);
        Label sottotitolo = creaEtichetta("Il Boss della Torre è caduto:\nla tua ascesa è compiuta.",
                DIMENSIONE_TESTO, Tema.TESTO);
        Label riepilogo = creaEtichetta(riepilogoFinale.nomeEroe() + " (Liv. " + riepilogoFinale.livello()
                        + ")\nha scalato tutti i " + riepilogoFinale.pianiScalati() + " piani della torre!",
                DIMENSIONE_TESTO, Tema.ORO);

        VBox riquadro = new VBox(12, sottotitolo, riepilogo, creaBottoneMenu(suMenu));
        riquadro.setAlignment(Pos.CENTER);
        riquadro.setPadding(new Insets(PADDING_RIQUADRO));
        riquadro.setMinWidth(LARGHEZZA_RIQUADRO);
        riquadro.setMaxWidth(LARGHEZZA_RIQUADRO);
        riquadro.setStyle(STILE_RIQUADRO);

        VBox contenuto = new VBox(SPAZIO_TRA_ELEMENTI, titolo, riquadro);
        contenuto.setAlignment(Pos.TOP_CENTER);
        contenuto.setPadding(new Insets(PADDING_SCHERMATA));

        SpriteAnimato eroe = new SpriteAnimato(PixelArt.PERSONAGGI, PixelArt.EROE_FERMO, SCALA_EROE);
        eroe.avvia();
        StackPane.setAlignment(eroe, Pos.BOTTOM_CENTER);
        StackPane.setMargin(eroe, new Insets(0, 0, DISTANZA_EROE_DAL_FONDO, 0));

        StackPane radice = new StackPane(
                ScenarioCorridoio.crea(dimensioni.larghezza(), dimensioni.altezza()), contenuto, eroe);
        this.scena = new Scene(radice, dimensioni.larghezza(), dimensioni.altezza());
    }

    public Scene getScena() {
        return this.scena;
    }

    private static Label creaEtichetta(String testo, int dimensione, String colore) {
        return Componenti.etichettaCentrata(testo, dimensione, colore, LARGHEZZA_RIQUADRO - 2 * PADDING_RIQUADRO);
    }

    private static Button creaBottoneMenu(Runnable suMenu) {
        Button bottone = new Button("🏠 TORNA AL MENU PRINCIPALE");
        Componenti.aggiungiEffettoSopra(bottone, stileBottone(Tema.ORO), stileBottone(ORO_SOPRA));
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