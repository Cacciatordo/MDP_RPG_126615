package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import it.unicam.cs.mpgc.rpg126615.view.AspettoAvversario;
import it.unicam.cs.mpgc.rpg126615.view.DimensioniFinestra;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Window;

public class VistaBattaglia {

    private static final double MARGINE = 12;
    private static final double MARGINE_CARTE = 10;
    private static final double CIMA_TITOLO = 12;
    private static final double ALTEZZA_TITOLO = 44;
    private static final double CIMA_PALCO = CIMA_TITOLO + ALTEZZA_TITOLO;
    private static final double CIMA_REGISTRO = CIMA_PALCO + PalcoBattaglia.ALTEZZA + 4;
    private static final double CIMA_COMANDI = CIMA_REGISTRO + RegistroBattaglia.ALTEZZA + 2;
    private static final double CIMA_CARTE = CIMA_COMANDI + BarraComandiBattaglia.ALTEZZA;
    private static final int DIMENSIONE_TITOLO = 16;
    private static final String TESTO_INIZIO = "Battaglia contro %s. Gioca le carte, attacca o termina il turno.\n";

    private static final int DIMENSIONE_BOTTONE_ESITO = 10;
    private static final String VERDE_CHIARO = "#4ade80";
    private static final String ORO_CHIARO = "#ffd700";
    private static final String ROSSO_SCURO = "#7a1220";

    private final AzioniBattaglia azioni;
    private final Battaglia battaglia;
    private final PalcoBattaglia palco;
    private final FilaCarte filaCarte;
    private final BarraComandiBattaglia comandi;
    private final RegistroBattaglia registro;
    private final Scene scena;

    public VistaBattaglia(DimensioniFinestra dimensioni, Battaglia battaglia, int numeroPiano,
                          AzioniBattaglia azioni) {
        double larghezza = dimensioni.larghezza();
        this.azioni = azioni;
        this.battaglia = battaglia;
        Combattente nemico = battaglia.getNemico();
        this.palco = new PalcoBattaglia(larghezza - 2 * MARGINE, battaglia.getGiocatore(), nemico,
                aspettoDelNemico(nemico, numeroPiano));
        this.filaCarte = new FilaCarte(larghezza - 2 * MARGINE_CARTE, battaglia,
                indice -> azioni.giocaCarta().accept(indice), () -> azioni.attacca().run());
        this.comandi = new BarraComandiBattaglia(larghezza, battaglia.getGiocatore(), () -> azioni.terminaTurno().run());
        this.registro = new RegistroBattaglia(larghezza - 2 * MARGINE);
        this.scena = creaScena(dimensioni, creaInterfaccia(larghezza, nemico.getNome()));

        this.registro.aggiungi(TESTO_INIZIO.formatted(nemico.getNome()));
        this.comandi.abilita(!this.battaglia.isFinita());
    }

    public Scene getScena() {
        return this.scena;
    }

    private static SpritePersonaggio aspettoDelNemico(Combattente nemico, int numeroPiano) {
        return AspettoAvversario.di(nemico).personaggioDelPiano(numeroPiano);
    }

    private Pane creaInterfaccia(double larghezza, String nomeNemico) {
        Pane interfaccia = new Pane();
        interfaccia.setPickOnBounds(false);
        aggiungi(interfaccia, creaTitolo(larghezza, nomeNemico), 0, CIMA_TITOLO);
        aggiungi(interfaccia, this.palco.getLivello(), MARGINE, CIMA_PALCO);
        aggiungi(interfaccia, this.registro.getNodo(), MARGINE, CIMA_REGISTRO);
        aggiungi(interfaccia, this.comandi.getNodo(), 0, CIMA_COMANDI);
        aggiungi(interfaccia, this.filaCarte.getNodo(), MARGINE_CARTE, CIMA_CARTE);
        return interfaccia;
    }

    private Scene creaScena(DimensioniFinestra dimensioni, Pane interfaccia) {
        int lineaPavimento = (int) (CIMA_PALCO + PalcoBattaglia.LINEA_PAVIMENTO);
        int fondoTappeto = (int) (CIMA_PALCO + PalcoBattaglia.ALTEZZA);
        StackPane radice = new StackPane(
                ScenarioBattaglia.crea(dimensioni.larghezza(), dimensioni.altezza(), lineaPavimento, fondoTappeto),
                interfaccia);
        radice.setStyle("-fx-background-color: " + Tema.SFONDO + ";");
        Scene nuovaScena = new Scene(radice, dimensioni.larghezza(), dimensioni.altezza());
        nuovaScena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));
        return nuovaScena;
    }

    private Label creaTitolo(double larghezza, String nomeNemico) {
        Label titolo = new Label("LOTTA CONTRO " + nomeNemico.toUpperCase() + "!");
        titolo.setFont(Font.font(Tema.FONT, DIMENSIONE_TITOLO));
        titolo.setTextFill(Color.web(Tema.ORO));
        titolo.setAlignment(Pos.CENTER);
        titolo.setPrefSize(larghezza, ALTEZZA_TITOLO);
        titolo.setMinSize(larghezza, ALTEZZA_TITOLO);
        titolo.setMaxSize(larghezza, ALTEZZA_TITOLO);
        return titolo;
    }

    public void mostraEsito(String esito) {
        this.registro.aggiungi(esito);
        this.palco.aggiorna();
        this.filaCarte.aggiorna();
        this.comandi.aggiorna();
        this.comandi.abilita(!this.battaglia.isFinita());
    }

    public void mostraAttaccoEroe(int danno) {
        this.palco.mostraAttaccoEroe(danno);
    }

    public void mostraAttaccoNemico(int danno) {
        this.palco.mostraAttaccoNemico(danno);
    }

    public void mostraVittoria(boolean haProssimoPiano) {
        if (haProssimoPiano) {
            this.registro.aggiungi("Hai sconfitto il nemico! Clicca sul pulsante qui sotto per continuare.\n");
            Button bottone = creaBottoneEsito("★ VITTORIA! TORNA AL CORRIDOIO ►", Tema.VERDE, VERDE_CHIARO, Tema.SFONDO);
            bottone.setOnAction(e -> this.azioni.esci().run());
            this.filaCarte.mostraEsito(bottone);
        } else {
            this.registro.aggiungi("Il boss finale è caduto! Clicca sul pulsante qui sotto per concludere la tua ascesa.\n");
            Button bottone = creaBottoneEsito("👑 HAI CONQUISTATO LA TORRE! ►", Tema.ORO, ORO_CHIARO, Tema.SFONDO);
            bottone.setOnAction(e -> this.azioni.esci().run());
            this.filaCarte.mostraEsito(bottone);
        }
    }

    public void mostraSconfitta() {
        this.registro.aggiungi("Sei caduto in battaglia! Clicca sul pulsante qui sotto per ritirarti.\n");
        Button bottone = creaBottoneEsito("☠ RITIRATI ALLA FONTANA (-" + Partita.PENALITA_XP_SCONFITTA + " XP)",
                ROSSO_SCURO, Tema.ROSSO, Tema.TESTO);
        bottone.setOnAction(e -> this.azioni.esci().run());
        this.filaCarte.mostraEsito(bottone);
    }

    private static Button creaBottoneEsito(String testo, String sfondo, String sfondoSopra, String coloreTesto) {
        Button bottone = new Button(testo);
        bottone.setStyle(stileBottoneEsito(sfondo, coloreTesto, Tema.BORDO));
        bottone.setCursor(Cursor.HAND);
        bottone.setFocusTraversable(false);
        bottone.setOnMouseEntered(e -> bottone.setStyle(stileBottoneEsito(sfondoSopra, coloreTesto, Tema.ORO)));
        bottone.setOnMouseExited(e -> bottone.setStyle(stileBottoneEsito(sfondo, coloreTesto, Tema.BORDO)));
        return bottone;
    }

    private static String stileBottoneEsito(String sfondo, String coloreTesto, String bordo) {
        return "-fx-background-color: " + sfondo + "; -fx-text-fill: " + coloreTesto + "; "
                + "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: " + DIMENSIONE_BOTTONE_ESITO + "px; "
                + "-fx-border-color: " + bordo + "; -fx-border-width: 2; "
                + "-fx-background-radius: 0; -fx-border-radius: 0; -fx-padding: 8 22;";
    }

    private static void aggiungi(Pane interfaccia, Node nodo, double x, double y) {
        nodo.setLayoutX(x);
        nodo.setLayoutY(y);
        interfaccia.getChildren().add(nodo);
    }

    private void animaSoloSeMostrata(Window finestra) {
        if (finestra == null) {
            this.palco.ferma();
        } else {
            this.palco.avvia();
        }
    }
}