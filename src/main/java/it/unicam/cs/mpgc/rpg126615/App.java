package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;
import it.unicam.cs.mpgc.rpg126615.model.torre.GeneratorePiani;
import it.unicam.cs.mpgc.rpg126615.model.torre.Piano;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.model.torre.Torre;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class App extends Application {

    private static final int NUMERO_PIANI = 20;

    private static final double LARGHEZZA = 764;
    private static final double ALTEZZA = 360;
    private static final double Y_PAVIMENTO = 300;
    private static final double VELOCITA = 220;

    private static final double LARGHEZZA_EROE = 36;
    private static final double ALTEZZA_EROE = 45;
    private static final double LARGHEZZA_NEMICO = 40;
    private static final double ALTEZZA_NEMICO = 50;
    private static final double LARGHEZZA_PORTA = 44;
    private static final double ALTEZZA_PORTA = 70;

    private Giocatore giocatore;
    private Torre torre;
    private Piano piano;
    private Battaglia battaglia;
    private int numeroPiano = 1;

    private Stage finestra;
    private Scene scenaCorridoio;
    private Scene scenaCombattimento;

    private final Rectangle eroe = new Rectangle(LARGHEZZA_EROE, ALTEZZA_EROE, Color.DODGERBLUE);
    private final Rectangle nemico = new Rectangle(LARGHEZZA_NEMICO, ALTEZZA_NEMICO, Color.CRIMSON);
    private final Rectangle porta = new Rectangle(LARGHEZZA_PORTA, ALTEZZA_PORTA, Color.DARKGRAY);

    private final Label etichettaPiano = creaEtichetta(15, 10);
    private final Label etichettaEroeCorridoio = creaEtichetta(15, 32);
    private final Label etichettaNemicoCorridoio = creaEtichetta(15, 54);
    private final Label etichettaMessaggio = creaEtichetta(15, 90);

    private final Label etichettaEroeCombattimento = new Label();
    private final Label etichettaNemicoCombattimento = new Label();
    private final TextArea log = new TextArea();
    private final FlowPane pulsantiCarte = new FlowPane(10, 10);
    private final Button pulsanteAttacca = new Button("Attacca (1 PA)");
    private final Button pulsanteTerminaTurno = new Button("Termina turno");
    private final Button pulsanteTorna = new Button("Torna al corridoio");

    private final Set<KeyCode> tasti = EnumSet.noneOf(KeyCode.class);
    private final AnimationTimer timer = creaTimer();

    @Override
    public void start(Stage stage) {
        finestra = stage;
        giocatore = new Giocatore("Eroe", 100, 12);
        torre = new Torre(new GeneratorePiani(new Random()).generaPiani(NUMERO_PIANI));
        scenaCorridoio = creaScenaCorridoio();
        scenaCombattimento = creaScenaCombattimento();
        iniziaPiano();

        stage.setTitle("RPG - prova");
        stage.setScene(scenaCorridoio);
        stage.setResizable(false);
        stage.show();
        timer.start();
    }

    private Scene creaScenaCorridoio() {
        Pane radice = new Pane();
        radice.setStyle("-fx-background-color: #1e1e28;");

        Rectangle pavimento = new Rectangle(0, Y_PAVIMENTO, LARGHEZZA, ALTEZZA - Y_PAVIMENTO);
        pavimento.setFill(Color.web("#3c3c4a"));

        nemico.setX((LARGHEZZA - LARGHEZZA_NEMICO) / 2);
        nemico.setY(Y_PAVIMENTO - ALTEZZA_NEMICO);
        porta.setX(LARGHEZZA - LARGHEZZA_PORTA - 10);
        porta.setY(Y_PAVIMENTO - ALTEZZA_PORTA);
        eroe.setY(Y_PAVIMENTO - ALTEZZA_EROE);

        Label suggerimento = creaEtichetta(15, 330);
        suggerimento.setText("A/D o frecce: cammina - raggiungi l'oggetto al centro per usarlo");

        etichettaMessaggio.setWrapText(true);
        etichettaMessaggio.setMaxWidth(LARGHEZZA - 30);

        radice.getChildren().addAll(pavimento, porta, nemico, eroe, etichettaPiano,
                etichettaEroeCorridoio, etichettaNemicoCorridoio, etichettaMessaggio, suggerimento);

        Scene scena = new Scene(radice, LARGHEZZA, ALTEZZA);
        scena.setOnKeyPressed(e -> tasti.add(e.getCode()));
        scena.setOnKeyReleased(e -> tasti.remove(e.getCode()));
        return scena;
    }

    private Scene creaScenaCombattimento() {
        log.setEditable(false);
        log.setPrefHeight(100);
        pulsanteAttacca.setOnAction(e -> attacca());
        pulsanteTerminaTurno.setOnAction(e -> terminaTurno());
        pulsanteTorna.setOnAction(e -> tornaAlCorridoio());

        HBox pulsanti = new HBox(10, pulsanteAttacca, pulsanteTerminaTurno, pulsanteTorna);
        VBox radice = new VBox(10, etichettaEroeCombattimento, etichettaNemicoCombattimento,
                log, pulsantiCarte, pulsanti);
        radice.setPadding(new Insets(15));
        return new Scene(radice, LARGHEZZA, ALTEZZA);
    }

    private void aggiornaPulsantiCarte() {
        pulsantiCarte.getChildren().clear();
        List<Carta> carte = giocatore.getMano().getCarte();
        for (int indice = 0; indice < carte.size(); indice++) {
            Carta carta = carte.get(indice);
            Button pulsante = new Button(carta.getNome() + " (" + carta.getCostoPA() + " PA)");
            pulsante.setTooltip(new Tooltip(carta.getDescrizione()));
            pulsante.setDisable(battaglia.isFinita() || giocatore.getPuntiAzione() < carta.getCostoPA());
            int posizione = indice;
            pulsante.setOnAction(e -> giocaCarta(posizione));
            pulsantiCarte.getChildren().add(pulsante);
        }
    }

    private void iniziaPiano() {
        piano = torre.getPiano(numeroPiano);
        if (piano.haNemico()) {
            battaglia = new Battaglia(giocatore, piano.getNemico());
            etichettaMessaggio.setText("Piano " + numeroPiano + ": ti sbarra la strada "
                    + piano.getNemico().getNome());
        } else {
            battaglia = null;
            etichettaMessaggio.setText("Piano " + numeroPiano + ": " + testoInvito());
        }
        eroe.setX(20);
        aggiornaCorridoio();
    }

    private boolean isPianoSuperato() {
        return battaglia != null ? battaglia.isVinta() : piano.isCompletato();
    }

    private void aggiornaCorridoio() {
        etichettaPiano.setText("Piano " + numeroPiano + " di " + torre.getNumeroPiani()
                + " - " + piano.getTipo());
        etichettaEroeCorridoio.setText(testoEroe());
        etichettaNemicoCorridoio.setText(testoNemico());
        nemico.setFill(coloreOggettoDelPiano());
        nemico.setVisible(battaglia == null || battaglia.getNemico().isVivo());
        porta.setFill(isPianoSuperato() ? Color.LIMEGREEN : Color.DARKGRAY);
    }

    private void muoviEroe(double secondi) {
        double spostamento = 0;
        if (tasti.contains(KeyCode.A) || tasti.contains(KeyCode.LEFT)) {
            spostamento -= VELOCITA * secondi;
        }
        if (tasti.contains(KeyCode.D) || tasti.contains(KeyCode.RIGHT)) {
            spostamento += VELOCITA * secondi;
        }
        double nuovaX = Math.max(0, Math.min(LARGHEZZA - LARGHEZZA_EROE, eroe.getX() + spostamento));
        eroe.setX(nuovaX);
    }

    private void controllaNemico() {
        if (battaglia != null && !battaglia.isFinita()
                && eroe.getBoundsInParent().intersects(nemico.getBoundsInParent())) {
            iniziaCombattimento();
        }
    }

    private void controllaOggettoDelPiano() {
        if (battaglia == null && !piano.isCompletato()
                && eroe.getBoundsInParent().intersects(nemico.getBoundsInParent())) {
            etichettaMessaggio.setText("Piano " + numeroPiano + ": " + piano.entra(giocatore));
            aggiornaCorridoio();
        }
    }

    private void controllaPorta() {
        if (isPianoSuperato() && eroe.getBoundsInParent().intersects(porta.getBoundsInParent())) {
            if (numeroPiano >= torre.getNumeroPiani()) {
                etichettaMessaggio.setText("Hai conquistato la torre!");
                timer.stop();
                return;
            }
            numeroPiano++;
            iniziaPiano();
        }
    }

    private void iniziaCombattimento() {
        timer.stop();
        tasti.clear();
        log.clear();
        log.appendText("--- " + battaglia.getNemico().getNome() + " ---\n");
        aggiornaCombattimento();
        finestra.setScene(scenaCombattimento);
    }

    private void attacca() {
        log.appendText(battaglia.attaccaConEroe());
        aggiornaCombattimento();
    }

    private void terminaTurno() {
        log.appendText(battaglia.terminaTurnoGiocatore());
        aggiornaCombattimento();
    }

    private void giocaCarta(int indice) {
        Nemico nemicoDelPiano = battaglia.getNemico();
        int attaccoEroe = giocatore.getAttacco();
        int difesaEroe = giocatore.getDifesa();
        int attaccoNemico = nemicoDelPiano.getAttacco();
        int difesaNemico = nemicoDelPiano.getDifesa();

        log.appendText(battaglia.giocaCartaDallaMano(indice));

        log.appendText(descriviVariazione("Attacco dell'eroe", attaccoEroe, giocatore.getAttacco())
                + descriviVariazione("Difesa dell'eroe", difesaEroe, giocatore.getDifesa())
                + descriviVariazione("Attacco del nemico", attaccoNemico, nemicoDelPiano.getAttacco())
                + descriviVariazione("Difesa del nemico", difesaNemico, nemicoDelPiano.getDifesa()));
        aggiornaCombattimento();
    }

    private static String descriviVariazione(String statistica, int prima, int dopo) {
        if (prima == dopo) {
            return "";
        }
        return "  " + statistica + ": " + prima + " -> " + dopo + "\n";
    }

    private void aggiornaCombattimento() {
        etichettaEroeCombattimento.setText(testoEroe());
        etichettaNemicoCombattimento.setText(testoNemico());
        aggiornaPulsantiCarte();
        pulsanteAttacca.setDisable(battaglia.isFinita()
                || giocatore.getPuntiAzione() < Battaglia.COSTO_PA_ATTACCO_BASE);
        pulsanteTerminaTurno.setDisable(battaglia.isFinita());
        pulsanteTorna.setDisable(!battaglia.isVinta());
    }

    private void tornaAlCorridoio() {
        etichettaMessaggio.setText("Nemico sconfitto! Raggiungi la porta per salire.");
        aggiornaCorridoio();
        finestra.setScene(scenaCorridoio);
        timer.start();
    }

    private String testoEroe() {
        return giocatore.getNome() + " - HP " + giocatore.getHp() + "/" + giocatore.getHpMax()
                + " - PA " + giocatore.getPuntiAzione()
                + " - Attacco " + giocatore.getAttacco() + " - Difesa " + giocatore.getDifesa()
                + " - Livello " + giocatore.getLivello()
                + " (XP " + giocatore.getEsperienza() + "/" + giocatore.getEsperienzaProssimoLivello() + ")";
    }

    private String testoNemico() {
        if (battaglia == null) {
            return piano.getTipo() == TipoPiano.CASSA ? "Cassa" : "Fontana di lava";
        }
        Nemico nemicoDelPiano = battaglia.getNemico();
        return nemicoDelPiano.getNome() + " - HP " + nemicoDelPiano.getHp() + "/" + nemicoDelPiano.getHpMax()
                + " - Attacco " + nemicoDelPiano.getAttacco() + " - Difesa " + nemicoDelPiano.getDifesa();
    }

    private String testoInvito() {
        return piano.getTipo() == TipoPiano.CASSA
                ? "raggiungi la cassa per aprirla."
                : "raggiungi la fontana di lava per curarti.";
    }

    private Color coloreOggettoDelPiano() {
        if (battaglia != null) {
            return Color.CRIMSON;
        }
        return piano.getTipo() == TipoPiano.CASSA ? Color.SADDLEBROWN : Color.ORANGE;
    }

    private static Label creaEtichetta(double x, double y) {
        Label etichetta = new Label();
        etichetta.setTextFill(Color.WHITE);
        etichetta.setLayoutX(x);
        etichetta.setLayoutY(y);
        return etichetta;
    }

    private AnimationTimer creaTimer() {
        return new AnimationTimer() {
            private long ultimoIstante;

            @Override
            public void handle(long adesso) {
                if (ultimoIstante != 0) {
                    muoviEroe((adesso - ultimoIstante) / 1_000_000_000.0);
                    controllaNemico();
                    controllaOggettoDelPiano();
                    controllaPorta();
                }
                ultimoIstante = adesso;
            }

            @Override
            public void stop() {
                super.stop();
                ultimoIstante = 0;
            }
        };
    }
}