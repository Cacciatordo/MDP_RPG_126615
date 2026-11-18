package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BarraSuperioreCorridoio {

    public static final double ALTEZZA = 160;

    private static final String GUIDA = "Usa [A / D] oppure le frecce per camminare nel corridoio";
    private static final String COLORE_HP_BASSI = Tema.ROSSO;
    private static final String COLORE_XP = "#a855f7";
    private static final String COLORE_DIFESA = "#f97316";
    private static final String COLORE_AVVISO = "#4a1d6e";
    private static final String COLORE_AVVISO_CURE_BLOCCATE = "#7f1d1d";
    private static final int LIVELLI_EXTRA_SENZA_CURE = 4;
    private static final String PANNELLO_SOPRA = "#3d2a1e";
    private static final double SOGLIA_HP_ALTI = 0.5;
    private static final double SOGLIA_HP_BASSI = 0.25;
    private static final double LARGHEZZA_BARRA_HP = 80;
    private static final double LARGHEZZA_BARRA_XP = 70;
    private static final double LARGHEZZA_MESSAGGIO = 780;
    private static final double ALTEZZA_MESSAGGIO = 28;
    private static final double ALTEZZA_AVVISO = 26;
    private static final int DIMENSIONE_TITOLO = 16;
    private static final int DIMENSIONE_STATO = 10;
    private static final int DIMENSIONE_MESSAGGIO = 9;
    private static final int DIMENSIONE_BOTTONE = 9;
    private static final int DIMENSIONE_AVVISO = 10;
    private static final String STILE_FERITA = "-fx-background-color: " + COLORE_AVVISO_CURE_BLOCCATE + "; "
            + "-fx-padding: 2 8; -fx-border-color: " + Tema.ROSSO + "; -fx-border-width: 2;";
    private static final String STILE_PANNELLO_STATO = "-fx-background-color: " + Tema.PANNELLO + "; "
            + "-fx-border-color: " + Tema.BORDO + "; -fx-border-width: 3;";

    private final Label etichettaTitolo = creaEtichetta(DIMENSIONE_TITOLO, Tema.ORO);
    private final Label etichettaEroe = creaEtichetta(DIMENSIONE_STATO, Tema.TESTO);
    private final ProgressBar barraHp = creaBarra(LARGHEZZA_BARRA_HP);
    private final Label etichettaHp = creaEtichetta(DIMENSIONE_STATO, Tema.TESTO);
    private final ProgressBar barraXp = creaBarra(LARGHEZZA_BARRA_XP);
    private final Label etichettaXp = creaEtichetta(DIMENSIONE_STATO, Tema.TESTO);
    private final Label etichettaAttacco = creaEtichetta(DIMENSIONE_STATO, Tema.ORO);
    private final Label etichettaDifesa = creaEtichetta(DIMENSIONE_STATO, COLORE_DIFESA);
    private final Label etichettaFerita = creaEtichetta(DIMENSIONE_STATO, Tema.TESTO);
    private final Tooltip suggerimentoFerita = new Tooltip();
    private final Label etichettaMessaggio = creaEtichetta(DIMENSIONE_MESSAGGIO, Tema.TESTO_SECONDARIO);
    private final Label etichettaAvviso = creaEtichetta(DIMENSIONE_AVVISO, Tema.ORO);
    private final VBox contenitore;

    public BarraSuperioreCorridoio(Runnable suSalva, Runnable suCarica, Runnable suMenu) {
        this.barraXp.setStyle(stileBarra(COLORE_XP));
        preparaEtichettaFerita();
        preparaEtichettaMessaggio();
        preparaEtichettaAvviso();
        nonTroncare(this.etichettaHp, this.etichettaXp, this.etichettaAttacco, this.etichettaDifesa,
                this.etichettaFerita);
        this.contenitore = creaContenitore(creaIntestazione(suSalva, suCarica, suMenu), creaPannelloStato());
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna(Partita partita) {
        Giocatore giocatore = partita.getGiocatore();
        int numero = partita.getNumeroPianoCorrente();
        TipoPiano tipo = partita.getPianoCorrente().getTipo();
        this.etichettaTitolo.setText("PIANO " + numero + " - " + tipo.name().replace('_', ' '));

        this.etichettaEroe.setText(giocatore.getNome() + " (Liv. " + giocatore.getLivello() + ")");
        double rapportoHp = Math.max(0, (double) giocatore.getHp() / giocatore.getHpMax());
        this.barraHp.setProgress(rapportoHp);
        this.barraHp.setStyle(stileBarra(coloreHp(rapportoHp)));
        this.etichettaHp.setText(Math.max(0, giocatore.getHp()) + " / " + giocatore.getHpMax());
        this.barraXp.setProgress(progressoEsperienza(giocatore));
        this.etichettaXp.setText(giocatore.getEsperienza() + " / " + giocatore.getEsperienzaProssimoLivello());
        this.etichettaAttacco.setText("⚔ " + giocatore.getAttacco());
        this.etichettaDifesa.setText("🛡 " + giocatore.getDifesa());

        aggiornaFerita(giocatore);

        if (tipo == TipoPiano.BOSS || tipo == TipoPiano.BOSS_FINALE) {
            mostraAvvisoBoss(livelloConsigliato(numero), giocatore.isGuarigioneBloccata());
        } else {
            this.etichettaAvviso.setVisible(false);
        }
    }

    public void mostraMessaggio(String messaggio) {
        this.etichettaMessaggio.setText(messaggio);
    }

    private void preparaEtichettaFerita() {
        this.etichettaFerita.setStyle(STILE_FERITA);
        this.etichettaFerita.setTooltip(this.suggerimentoFerita);
        this.etichettaFerita.setVisible(false);
        this.etichettaFerita.managedProperty().bind(this.etichettaFerita.visibleProperty());
    }

    private void preparaEtichettaMessaggio() {
        this.etichettaMessaggio.setText(GUIDA);
        this.etichettaMessaggio.setWrapText(true);
        this.etichettaMessaggio.setTextAlignment(TextAlignment.CENTER);
        this.etichettaMessaggio.setAlignment(Pos.CENTER);
        this.etichettaMessaggio.setPrefSize(LARGHEZZA_MESSAGGIO, ALTEZZA_MESSAGGIO);
        this.etichettaMessaggio.setMinSize(LARGHEZZA_MESSAGGIO, ALTEZZA_MESSAGGIO);
        this.etichettaMessaggio.setMaxSize(LARGHEZZA_MESSAGGIO, ALTEZZA_MESSAGGIO);
    }

    private void preparaEtichettaAvviso() {
        this.etichettaAvviso.setVisible(false);
        this.etichettaAvviso.setAlignment(Pos.CENTER);
        this.etichettaAvviso.setMinHeight(ALTEZZA_AVVISO);
        this.etichettaAvviso.setPrefHeight(ALTEZZA_AVVISO);
    }

    private HBox creaIntestazione(Runnable suSalva, Runnable suCarica, Runnable suMenu) {
        Button bottoneSalva = creaBottone("💾 Salva", "Salva la partita.", suSalva);
        Button bottoneCarica = creaBottone("📂 Carica", "Carica una partita salvata.", suCarica);
        Button bottoneMenu = creaBottone("🏠 Menu Principale", "Torna al menu iniziale.", suMenu);
        HBox comandi = new HBox(8, bottoneSalva, bottoneCarica, bottoneMenu);
        comandi.setAlignment(Pos.CENTER);
        HBox intestazione = new HBox(24, this.etichettaTitolo, comandi);
        intestazione.setAlignment(Pos.CENTER);
        return intestazione;
    }

    private HBox creaPannelloStato() {
        HBox pannelloStato = new HBox(10, this.etichettaEroe,
                creaBlocco("HP", Tema.VERDE, this.barraHp, this.etichettaHp),
                creaBlocco("XP", COLORE_XP, this.barraXp, this.etichettaXp),
                this.etichettaAttacco, this.etichettaDifesa, this.etichettaFerita);
        pannelloStato.setAlignment(Pos.CENTER);
        pannelloStato.setPadding(new Insets(6, 14, 6, 14));
        pannelloStato.setStyle(STILE_PANNELLO_STATO);
        pannelloStato.setMaxWidth(HBox.USE_PREF_SIZE);
        return pannelloStato;
    }

    private VBox creaContenitore(Node intestazione, Node pannelloStato) {
        VBox riquadro = new VBox(6, intestazione, pannelloStato, this.etichettaMessaggio, this.etichettaAvviso);
        riquadro.setAlignment(Pos.TOP_CENTER);
        riquadro.setPadding(new Insets(8, 10, 0, 10));
        riquadro.setMinHeight(ALTEZZA);
        riquadro.setPrefHeight(ALTEZZA);
        riquadro.setMaxHeight(ALTEZZA);
        riquadro.setStyle("-fx-background-color: " + Tema.SFONDO + ";");
        return riquadro;
    }

    private void aggiornaFerita(Giocatore giocatore) {
        this.etichettaFerita.setVisible(giocatore.isGuarigioneBloccata());
        if (giocatore.isGuarigioneBloccata()) {
            int piani = giocatore.getPianiFeritaRimanenti();
            this.etichettaFerita.setText("🩸 " + piani);
            this.suggerimentoFerita.setText("Ferita: le cure restano bloccate per altri " + piani + " piani.");
        }
    }

    private void mostraAvvisoBoss(int livello, boolean cureBloccate) {
        if (cureBloccate) {
            this.etichettaAvviso.setText("⚠ ATTENZIONE PIANO BOSS! 🩸 CURE BLOCCATE! Livello consigliato: Liv. "
                    + (livello + LIVELLI_EXTRA_SENZA_CURE) + " (Pericolo Estremo)");
        } else {
            this.etichettaAvviso.setText("⚠ ATTENZIONE: PIANO BOSS! Livello consigliato: Liv. " + livello);
        }
        String colore = cureBloccate ? COLORE_AVVISO_CURE_BLOCCATE : COLORE_AVVISO;
        this.etichettaAvviso.setStyle("-fx-background-color: " + colore + "; -fx-border-color: "
                + Tema.ROSSO + "; -fx-border-width: 2; -fx-padding: 3 12;");
        this.etichettaAvviso.setVisible(true);
    }

    private static int livelloConsigliato(int numeroPiano) {
        double stima = 0.55 * numeroPiano + 0.3 * Math.sqrt(numeroPiano) + 0.2;
        return Math.max(1, (int) Math.round(stima));
    }

    private static String coloreHp(double rapporto) {
        if (rapporto > SOGLIA_HP_ALTI) {
            return Tema.VERDE;
        }
        return rapporto > SOGLIA_HP_BASSI ? Tema.ORO : COLORE_HP_BASSI;
    }

    private static double progressoEsperienza(Giocatore giocatore) {
        int prossimo = giocatore.getEsperienzaProssimoLivello();
        if (prossimo <= 0) {
            return 1;
        }
        return Math.min(1, (double) giocatore.getEsperienza() / prossimo);
    }

    private static void nonTroncare(Label... etichette) {
        for (Label etichetta : etichette) {
            etichetta.setMinWidth(Region.USE_PREF_SIZE);
        }
    }

    private static HBox creaBlocco(String nome, String colore, ProgressBar barra, Label valore) {
        Label titolo = creaEtichetta(DIMENSIONE_STATO, colore);
        titolo.setText(nome);
        HBox blocco = new HBox(6, titolo, barra, valore);
        blocco.setAlignment(Pos.CENTER);
        return blocco;
    }

    private static ProgressBar creaBarra(double larghezza) {
        ProgressBar barra = new ProgressBar(1.0);
        barra.setPrefWidth(larghezza);
        barra.setStyle(stileBarra(Tema.VERDE));
        return barra;
    }

    private static String stileBarra(String colore) {
        return "-fx-accent: " + colore + "; -fx-control-inner-background: " + Tema.SFONDO + "; "
                + "-fx-background-radius: 0; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2;";
    }

    private static Button creaBottone(String testo, String suggerimento, Runnable azione) {
        Button bottone = new Button(testo);
        bottone.setFont(Font.font(Tema.FONT, DIMENSIONE_BOTTONE));
        bottone.setStyle(stileBottone(Tema.PANNELLO, Tema.BORDO));
        bottone.setCursor(Cursor.HAND);
        bottone.setFocusTraversable(false);
        bottone.setTooltip(new Tooltip(suggerimento));
        bottone.setOnMouseEntered(evento -> bottone.setStyle(stileBottone(PANNELLO_SOPRA, Tema.ORO)));
        bottone.setOnMouseExited(evento -> bottone.setStyle(stileBottone(Tema.PANNELLO, Tema.BORDO)));
        bottone.setOnAction(evento -> azione.run());
        return bottone;
    }

    private static String stileBottone(String sfondo, String bordo) {
        return "-fx-background-color: " + sfondo + "; -fx-text-fill: " + Tema.TESTO + "; "
                + "-fx-border-color: " + bordo + "; -fx-border-width: 2; -fx-background-radius: 0; "
                + "-fx-border-radius: 0; -fx-padding: 4 10;";
    }

    private static Label creaEtichetta(int dimensione, String colore) {
        Label etichetta = new Label();
        etichetta.setFont(Font.font(Tema.FONT, dimensione));
        etichetta.setTextFill(Color.web(colore));
        return etichetta;
    }
}