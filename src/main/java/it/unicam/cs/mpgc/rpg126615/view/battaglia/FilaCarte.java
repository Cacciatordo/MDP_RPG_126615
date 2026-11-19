package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mazzo;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FilaCarte {

    public static final double ALTEZZA = VistaCarta.ALTEZZA + VistaCarta.SOLLEVAMENTO;

    private static final double SPAZIO_CARTE = 5;
    private static final double SPAZIO_GRUPPO_ATTACCO = 10;
    private static final double SPESSORE_SEPARATORE = 2;
    private static final double ALTEZZA_SEPARATORE = 100;
    private static final double LARGHEZZA_FASCIA = SPESSORE_SEPARATORE + SPAZIO_GRUPPO_ATTACCO
            + VistaCarta.LARGHEZZA;
    private static final double SPAZIO_PILE = ALTEZZA - 2 * VistaPila.ALTEZZA;
    private static final String TESTO_ATTACCO = "Colpisci il nemico con il tuo attacco.";
    private static final String TESTO_ATTACCO_USATO = "Hai già attaccato in questo turno.";

    private final Battaglia battaglia;
    private final Giocatore giocatore;
    private final Consumer<Integer> suCartaScelta;
    private final Runnable suAttacco;
    private final VistaPila pilaPesca = new VistaPila("MAZZO");
    private final VistaPila pilaScarti = new VistaPila("SCARTI");
    private final HBox carte = new HBox(SPAZIO_CARTE);
    private final HBox postoAttacco = new HBox();
    private final BorderPane riga = new BorderPane();

    public FilaCarte(double larghezza, Battaglia battaglia, Consumer<Integer> suCartaScelta,
                     Runnable suAttacco) {
        this.battaglia = battaglia;
        this.giocatore = battaglia.getGiocatore();
        this.suCartaScelta = suCartaScelta;
        this.suAttacco = suAttacco;
        this.carte.setAlignment(Pos.BOTTOM_CENTER);
        this.carte.setMinHeight(ALTEZZA);
        this.postoAttacco.setAlignment(Pos.BOTTOM_CENTER);
        this.postoAttacco.setMinSize(VistaCarta.LARGHEZZA, ALTEZZA);

        this.riga.setLeft(creaColonnaPile());
        this.riga.setCenter(this.carte);
        this.riga.setRight(creaGruppoAttacco());
        this.riga.setMinWidth(larghezza);
        this.riga.setPrefWidth(larghezza);
        this.riga.setMaxWidth(larghezza);
        aggiorna();
    }

    public Node getNodo() {
        return this.riga;
    }

    public void aggiorna() {
        this.carte.getChildren().clear();
        List<Carta> mano = this.giocatore.getMano().getCarte();
        for (int indice = 0; indice < mano.size(); indice++) {
            Carta carta = mano.get(indice);
            Node nodo = new VistaCarta(carta, this.battaglia.isCartaGiocabile(carta)).getNodo();
            int indiceCarta = indice;
            nodo.setCursor(Cursor.HAND);
            nodo.setOnMouseClicked(evento -> this.suCartaScelta.accept(indiceCarta));
            this.carte.getChildren().add(nodo);
        }
        this.postoAttacco.getChildren().setAll(creaCartaAttacco());
        aggiornaPile();
    }

    public void mostraEsito(Node bottone) {
        this.carte.getChildren().setAll(bottone);
        this.postoAttacco.getChildren().clear();
    }

    private void aggiornaPile() {
        Mazzo mazzo = this.giocatore.getMazzo();
        int daPescare = mazzo.getDimensionePesca();
        this.pilaPesca.aggiorna(daPescare, "da pescare", Tema.BORDO,
                "Mazzo: " + daPescare + " carte da pescare. Quando finiscono, gli scarti vengono rimescolati.");

        List<Carta> scarti = mazzo.getPilaScarti();
        if (scarti.isEmpty()) {
            this.pilaScarti.aggiorna(0, "nessuno", Tema.BORDO, "Nessuna carta scartata.");
        } else {
            Carta ultima = scarti.get(scarti.size() - 1);
            this.pilaScarti.aggiorna(scarti.size(), ultima.getNome(), StileCarta.coloreDi(ultima.getTipo()),
                    "Scarti: " + descriviScarti(scarti));
        }
    }

    private static String descriviScarti(List<Carta> scarti) {
        Map<String, Integer> perNome = new LinkedHashMap<>();
        for (Carta carta : scarti) {
            perNome.merge(carta.getNome(), 1, Integer::sum);
        }
        StringBuilder descrizione = new StringBuilder();
        for (Map.Entry<String, Integer> voce : perNome.entrySet()) {
            if (descrizione.length() > 0) {
                descrizione.append(", ");
            }
            descrizione.append(voce.getKey());
            if (voce.getValue() > 1) {
                descrizione.append(" x").append(voce.getValue());
            }
        }
        return descrizione.toString();
    }

    private VBox creaColonnaPile() {
        VBox colonna = new VBox(SPAZIO_PILE, this.pilaScarti.getNodo(), this.pilaPesca.getNodo());
        colonna.setAlignment(Pos.BOTTOM_LEFT);
        colonna.setMinWidth(LARGHEZZA_FASCIA);
        colonna.setPrefWidth(LARGHEZZA_FASCIA);
        colonna.setMaxWidth(LARGHEZZA_FASCIA);
        return colonna;
    }

    private HBox creaGruppoAttacco() {
        HBox gruppo = new HBox(SPAZIO_GRUPPO_ATTACCO, creaSeparatore(), this.postoAttacco);
        gruppo.setAlignment(Pos.BOTTOM_CENTER);
        return gruppo;
    }

    private static Region creaSeparatore() {
        Region separatore = new Region();
        separatore.setMinWidth(SPESSORE_SEPARATORE);
        separatore.setPrefWidth(SPESSORE_SEPARATORE);
        separatore.setMaxWidth(SPESSORE_SEPARATORE);
        separatore.setPrefHeight(ALTEZZA_SEPARATORE);
        separatore.setMaxHeight(ALTEZZA_SEPARATORE);
        separatore.setStyle("-fx-background-color: " + Tema.BORDO + ";");
        return separatore;
    }

    private Node creaCartaAttacco() {
        boolean usato = this.battaglia.isAttaccoEseguitoNelTurno();
        boolean giocabile = this.battaglia.isAttaccoBaseGiocabile();
        String descrizione = usato ? TESTO_ATTACCO_USATO : TESTO_ATTACCO;
        Node nodo = new VistaCarta(StileCarta.COLORE_ATTACCO_BASE, StileCarta.ICONA_ATTACCO_BASE,
                Battaglia.COSTO_PA_ATTACCO_BASE, "Attacco Base", descrizione, giocabile).getNodo();
        nodo.setCursor(Cursor.HAND);
        nodo.setOnMouseClicked(evento -> this.suAttacco.run());
        return nodo;
    }
}