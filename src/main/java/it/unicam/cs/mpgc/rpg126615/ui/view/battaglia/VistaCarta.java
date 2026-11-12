package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

public class VistaCarta {

    public static final double LARGHEZZA = 108;
    public static final double ALTEZZA = 152;
    public static final double SOLLEVAMENTO = 8;

    private static final double MARGINE = 3;
    private static final double LARGHEZZA_CONTENUTO = LARGHEZZA - 2 * (MARGINE + 3);
    private static final double ALTEZZA_NOME = 28;
    private static final double ALTEZZA_ILLUSTRAZIONE = 46;
    private static final double ALTEZZA_DESCRIZIONE = 52;
    private static final double RAGGIO_COSTO = 11;
    private static final double OPACITA_SFONDO_ILLUSTRAZIONE = 0.2;
    private static final double OPACITA_NON_GIOCABILE = 0.45;
    private static final int DIMENSIONE_NOME_GRANDE = 8;
    private static final int DIMENSIONE_NOME_PICCOLA = 7;
    private static final int DIMENSIONE_DESCRIZIONE = 7;
    private static final int DIMENSIONE_ICONA = 30;
    private static final int DIMENSIONE_COSTO = 10;

    private final String stileNormale;
    private final VBox corpo;
    private final StackPane contenitore;

    public VistaCarta(Carta carta, boolean giocabile) {
        this(StileCarta.coloreDi(carta.getTipo()), StileCarta.iconaDi(carta.getTipo()), carta.getCostoPA(),
                carta.getNome(), carta.getDescrizione(), giocabile);
    }

    public VistaCarta(String colore, String icona, int costoPa, String nome, String descrizione,
                      boolean giocabile) {
        this.stileNormale = stileCornice(colore);
        this.corpo = new VBox(4, creaNome(nome), creaIllustrazione(costoPa, icona, colore),
                creaDescrizione(descrizione));
        this.corpo.setAlignment(Pos.TOP_CENTER);
        this.corpo.setPadding(new Insets(MARGINE));
        blocca(this.corpo, LARGHEZZA, ALTEZZA);
        this.corpo.setStyle(this.stileNormale);

        this.contenitore = new StackPane(this.corpo);
        this.contenitore.setAlignment(Pos.BOTTOM_CENTER);
        blocca(this.contenitore, LARGHEZZA, ALTEZZA + SOLLEVAMENTO);
        Tooltip suggerimento = new Tooltip(descrizione);
        suggerimento.setShowDelay(Duration.millis(50));
        Tooltip.install(this.contenitore, suggerimento);
        preparaGiocabilita(giocabile);
    }

    public Node getNodo() {
        return this.contenitore;
    }

    private void preparaGiocabilita(boolean giocabile) {
        if (!giocabile) {
            this.contenitore.setOpacity(OPACITA_NON_GIOCABILE);
            return;
        }
        this.contenitore.setOnMouseEntered(evento -> {
            this.corpo.setTranslateY(-SOLLEVAMENTO);
            this.corpo.setStyle(stileCornice(Tema.TESTO));
        });
        this.contenitore.setOnMouseExited(evento -> {
            this.corpo.setTranslateY(0);
            this.corpo.setStyle(this.stileNormale);
        });
    }

    private static Label creaNome(String testo) {
        Label nome = creaEtichetta(testo.replace("-", "-\n"), dimensioneNome(testo), Tema.TESTO);
        nome.setAlignment(Pos.CENTER);
        nome.setMinHeight(ALTEZZA_NOME);
        nome.setPrefHeight(ALTEZZA_NOME);
        return nome;
    }

    private static int dimensioneNome(String nome) {
        int piuLunga = 0;
        for (String parola : nome.split("[ -]")) {
            piuLunga = Math.max(piuLunga, parola.length());
        }
        return piuLunga * DIMENSIONE_NOME_GRANDE <= LARGHEZZA_CONTENUTO
                ? DIMENSIONE_NOME_GRANDE : DIMENSIONE_NOME_PICCOLA;
    }

    private static StackPane creaIllustrazione(int costoPa, String icona, String colore) {
        StackPane illustrazione = new StackPane();
        blocca(illustrazione, LARGHEZZA_CONTENUTO, ALTEZZA_ILLUSTRAZIONE);
        illustrazione.setBackground(new Background(new BackgroundFill(
                Color.web(colore, OPACITA_SFONDO_ILLUSTRAZIONE), CornerRadii.EMPTY, Insets.EMPTY)));

        Label etichettaIcona = creaEtichetta(icona, DIMENSIONE_ICONA, colore);
        etichettaIcona.setWrapText(false);
        etichettaIcona.setAlignment(Pos.CENTER);
        etichettaIcona.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        StackPane costo = creaCosto(costoPa, colore);
        StackPane.setAlignment(costo, Pos.TOP_LEFT);
        StackPane.setMargin(costo, new Insets(3));

        illustrazione.getChildren().addAll(etichettaIcona, costo);
        return illustrazione;
    }

    private static StackPane creaCosto(int costoPa, String colore) {
        Circle cerchio = new Circle(RAGGIO_COSTO, Color.web(colore));
        cerchio.setStroke(Color.web(Tema.SFONDO));
        cerchio.setStrokeWidth(2);
        Text valore = new Text(String.valueOf(costoPa));
        valore.setFont(Font.font(Tema.FONT, DIMENSIONE_COSTO));
        valore.setFill(Color.web(Tema.SFONDO));
        valore.setBoundsType(TextBoundsType.VISUAL);

        StackPane costo = new StackPane(cerchio, valore);
        costo.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return costo;
    }

    private static Label creaDescrizione(String testo) {
        Label descrizione = creaEtichetta(testo, DIMENSIONE_DESCRIZIONE, Tema.TESTO_SECONDARIO);
        descrizione.setAlignment(Pos.TOP_CENTER);
        descrizione.setTextOverrun(OverrunStyle.ELLIPSIS);
        descrizione.setMinHeight(ALTEZZA_DESCRIZIONE);
        descrizione.setPrefHeight(ALTEZZA_DESCRIZIONE);
        descrizione.setMaxHeight(ALTEZZA_DESCRIZIONE);
        return descrizione;
    }

    private static Label creaEtichetta(String testo, int dimensione, String colore) {
        Label etichetta = new Label(testo);
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        etichetta.setWrapText(true);
        etichetta.setTextAlignment(TextAlignment.CENTER);
        etichetta.setMaxWidth(LARGHEZZA_CONTENUTO);
        return etichetta;
    }

    private static void blocca(Region regione, double larghezza, double altezza) {
        regione.setMinSize(larghezza, altezza);
        regione.setPrefSize(larghezza, altezza);
        regione.setMaxSize(larghezza, altezza);
    }

    private static String stileCornice(String colore) {
        return "-fx-background-color: " + Tema.PANNELLO + "; -fx-border-color: " + colore + "; "
                + "-fx-border-width: 3; -fx-background-radius: 0; -fx-border-radius: 0;";
    }
}