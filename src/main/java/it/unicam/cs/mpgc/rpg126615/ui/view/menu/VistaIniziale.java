package it.unicam.cs.mpgc.rpg126615.ui.view.menu;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.ScenarioCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.OptionalInt;

public class VistaIniziale {

    private static final int PIANI_PREDEFINITI = 50;
    private static final String SPRITE_EROE = "knight_m_idle_anim";
    private static final int SCALA_EROE = 3;

    private static final double LARGHEZZA_RIQUADRO = 400;
    private static final double LARGHEZZA_CAMPO = 320;
    private static final double SPAZIO_TRA_RIQUADRI = 12;
    private static final double SPAZIO_TRA_PERSONAGGI = 30;
    private static final double SPAZIO_TRA_ELEMENTI = 14;
    private static final double PADDING_SCHERMATA = 20;
    private static final double PADDING_RIQUADRO = 14;
    private static final int DIMENSIONE_TITOLO = 34;
    private static final int DIMENSIONE_SOTTOTITOLO = 9;
    private static final int DIMENSIONE_TESTO = 9;
    private static final int DIMENSIONE_BOTTONE = 10;

    private static final String ORO_SOPRA = "#ffd700";
    private static final String PANNELLO_SOPRA = "#3d2a1e";
    private static final String TESTO_SPENTO = "#6b6058";

    private static final String TESTO_SOTTOTITOLO = "Scala i piani, affronta i boss e sconfiggi i pericoli della torre";
    private static final String TESTO_DESCRIZIONE = "Sali la torre piano dopo piano: combatti mostri con le "
            + "tue carte, apri casse, riposa alle fontane di lava e sconfiggi i boss. "
            + "In cima ti aspetta il Boss della Torre!";
    private static final String MESSAGGIO_PIANI_NON_VALIDI = "Inserisci un numero intero di piani da "
            + Partita.PIANI_MINIMI + " a " + Partita.PIANI_MASSIMI + ".";
    private static final String STILE_RIQUADRO = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 3; "
            + "-fx-background-radius: 0; -fx-border-radius: 0;";
    private static final String STILE_CAMPO = "-fx-background-color: " + Tema.SFONDO + "; "
            + "-fx-text-fill: " + Tema.TESTO + "; -fx-prompt-text-fill: " + TESTO_SPENTO + "; "
            + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_TESTO + "px; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2; "
            + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 8;";

    private final AzioniMenu azioni;
    private final TextField campoNome = creaCampo("Inserisci nome (default: Eroe)");
    private final TextField campoPiani = creaCampo("Numero di piani (default: " + PIANI_PREDEFINITI + ")");
    private final Label etichettaErrore = creaEtichetta("", Tema.ERRORE);
    private final Scene scena;

    public VistaIniziale(double larghezza, double altezza, AzioniMenu azioni) {
        this.azioni = azioni;

        Button nuovaPartita = creaBottone("⚔ INIZIA NUOVA PARTITA", Tema.ORO, ORO_SOPRA, Tema.SFONDO);
        nuovaPartita.setOnAction(evento -> avviaNuovaPartita());
        Button continua = creaBottone("📂 CONTINUA / CARICA PARTITA", Tema.PANNELLO, PANNELLO_SOPRA, Tema.TESTO);
        continua.setOnAction(evento -> this.azioni.caricaPartita().run());
        this.campoNome.setOnAction(evento -> avviaNuovaPartita());
        this.campoPiani.setOnAction(evento -> avviaNuovaPartita());

        VBox contenuto = new VBox(SPAZIO_TRA_ELEMENTI, creaTitolo(), creaSottotitolo(),
                creaPalcoscenico(nuovaPartita, continua), this.etichettaErrore);
        contenuto.setAlignment(Pos.CENTER);
        contenuto.setPadding(new Insets(PADDING_SCHERMATA));

        StackPane radice = new StackPane(ScenarioCorridoio.crea(larghezza, altezza), contenuto);
        this.scena = new Scene(radice, larghezza, altezza);
    }

    public Scene getScena() {
        return this.scena;
    }

    private void avviaNuovaPartita() {
        OptionalInt piani = leggiNumeroPiani();
        if (piani.isEmpty()) {
            this.etichettaErrore.setText(MESSAGGIO_PIANI_NON_VALIDI);
            return;
        }
        this.etichettaErrore.setText("");
        this.azioni.nuovaPartita().accept(this.campoNome.getText(), piani.getAsInt());
    }

    private OptionalInt leggiNumeroPiani() {
        String testo = this.campoPiani.getText().trim();
        if (testo.isEmpty()) {
            return OptionalInt.of(PIANI_PREDEFINITI);
        }
        try {
            int piani = Integer.parseInt(testo);
            return Partita.isNumeroPianiValido(piani) ? OptionalInt.of(piani) : OptionalInt.empty();
        } catch (NumberFormatException errore) {
            return OptionalInt.empty();
        }
    }

    private static Node creaTitolo() {
        Label titolo = creaEtichetta("EMBERFALL", Tema.ORO);
        titolo.setFont(Font.font(Tema.FONT, DIMENSIONE_TITOLO));
        titolo.setWrapText(false);
        titolo.setMaxWidth(Region.USE_PREF_SIZE);
        return titolo;
    }

    private static Node creaSottotitolo() {
        Label sottotitolo = creaEtichetta(TESTO_SOTTOTITOLO, Tema.TESTO);
        sottotitolo.setFont(Font.font(Tema.FONT, DIMENSIONE_SOTTOTITOLO));
        sottotitolo.setWrapText(false);
        sottotitolo.setMaxWidth(Region.USE_PREF_SIZE);
        return sottotitolo;
    }

    private HBox creaPalcoscenico(Button nuovaPartita, Button continua) {
        VBox colonnaCentrale = new VBox(SPAZIO_TRA_RIQUADRI,
                creaRiquadroNuovaPartita(nuovaPartita), creaRiquadro(continua));
        colonnaCentrale.setAlignment(Pos.CENTER);

        SpriteAnimato eroe = creaSprite(PixelArt.PERSONAGGI, SPRITE_EROE, SCALA_EROE);
        SpriteAnimato boss = creaSprite(PixelArt.PERSONAGGI,
                SpritePersonaggio.DEMONE_GIGANTE.getPrefisso(), SpritePersonaggio.DEMONE_GIGANTE.getScala());
        boss.setScaleX(-1);

        HBox palcoscenico = new HBox(SPAZIO_TRA_PERSONAGGI, eroe, colonnaCentrale, boss);
        palcoscenico.setAlignment(Pos.BOTTOM_CENTER);
        return palcoscenico;
    }

    private VBox creaRiquadroNuovaPartita(Button nuovaPartita) {
        Label descrizione = creaEtichetta(TESTO_DESCRIZIONE, TESTO_SPENTO);
        return creaRiquadro(descrizione,
                creaEtichetta("Nome del tuo Eroe:", Tema.TESTO), this.campoNome,
                creaEtichetta("Quanti piani vuoi sfidare? (da " + Partita.PIANI_MINIMI + " a "
                        + Partita.PIANI_MASSIMI + ")", Tema.TESTO),
                this.campoPiani,
                nuovaPartita);
    }

    private static Label creaEtichetta(String testo, String colore) {
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, DIMENSIONE_TESTO));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setWrapText(true);
        etichetta.setTextAlignment(TextAlignment.CENTER);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setMaxWidth(LARGHEZZA_RIQUADRO - 2 * PADDING_RIQUADRO);
        return etichetta;
    }

    private static TextField creaCampo(String suggerimento) {
        TextField campo = new TextField();
        campo.setPromptText(suggerimento);
        campo.setMaxWidth(LARGHEZZA_CAMPO);
        campo.setStyle(STILE_CAMPO);
        return campo;
    }

    private static VBox creaRiquadro(Node... contenuto) {
        VBox riquadro = new VBox(10, contenuto);
        riquadro.setAlignment(Pos.CENTER);
        riquadro.setPadding(new Insets(PADDING_RIQUADRO));
        riquadro.setMinWidth(LARGHEZZA_RIQUADRO);
        riquadro.setMaxWidth(LARGHEZZA_RIQUADRO);
        riquadro.setStyle(STILE_RIQUADRO);
        return riquadro;
    }

    private static Button creaBottone(String testo, String sfondo, String sfondoSopra, String coloreTesto) {
        Button bottone = new Button(testo);
        String normale = stileBottone(sfondo, coloreTesto, Tema.BORDO);
        String sopra = stileBottone(sfondoSopra, coloreTesto, Tema.ORO);
        bottone.setStyle(normale);
        bottone.setCursor(Cursor.HAND);
        bottone.setOnMouseEntered(evento -> bottone.setStyle(sopra));
        bottone.setOnMouseExited(evento -> bottone.setStyle(normale));
        return bottone;
    }

    private static String stileBottone(String sfondo, String coloreTesto, String bordo) {
        return "-fx-background-color: " + sfondo + "; -fx-text-fill: " + coloreTesto + "; "
                + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_BOTTONE + "px; "
                + "-fx-border-color: " + bordo + "; -fx-border-width: 2; "
                + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 8 16;";
    }

    private static SpriteAnimato creaSprite(String cartella, String prefisso, int scala) {
        SpriteAnimato sprite = new SpriteAnimato(cartella, prefisso, scala);
        sprite.avvia();
        return sprite;
    }
}