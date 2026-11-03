package it.unicam.cs.mpgc.rpg126615;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.persistenza.ArchivioSalvataggi;
import it.unicam.cs.mpgc.rpg126615.persistenza.ArchivioSalvataggiFile;
import it.unicam.cs.mpgc.rpg126615.persistenza.ErrorePersistenzaException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class App extends Application {

    private static final String NOME_EROE = "Eroe";
    private static final int NUMERO_PIANI = 20;
    private static final long SEME_TORRE = 126615L;
    private static final Path CARTELLA_SALVATAGGI = Path.of("salvataggi");
    private static final String NOME_PREDEFINITO = "salvataggio1";

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

    private Partita partita;

    private final ArchivioSalvataggi archivio = new ArchivioSalvataggiFile(CARTELLA_SALVATAGGI);

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

    private final Button pulsanteSalva = new Button("Salva");
    private final Button pulsanteCarica = new Button("Carica");

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
        partita = Partita.nuova(NOME_EROE, NUMERO_PIANI, new Random(SEME_TORRE));
        scenaCorridoio = creaScenaCorridoio();
        scenaCombattimento = creaScenaCombattimento();
        mostraPiano();

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

        pulsanteSalva.setFocusTraversable(false);
        pulsanteCarica.setFocusTraversable(false);
        pulsanteSalva.setOnAction(e -> salvaPartita());
        pulsanteCarica.setOnAction(e -> caricaPartita());
        HBox pulsantiSalvataggio = new HBox(10, pulsanteSalva, pulsanteCarica);
        pulsantiSalvataggio.setLayoutX(15);
        pulsantiSalvataggio.setLayoutY(130);

        radice.getChildren().addAll(pavimento, porta, nemico, eroe, etichettaPiano,
                etichettaEroeCorridoio, etichettaNemicoCorridoio, etichettaMessaggio,
                pulsantiSalvataggio, suggerimento);

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

    private Battaglia battagliaInCorso() {
        return partita.getBattaglia().orElseThrow();
    }

    private void aggiornaPulsantiCarte() {
        Giocatore giocatore = partita.getGiocatore();
        Battaglia battaglia = battagliaInCorso();
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

    private void mostraPiano() {
        int numeroPiano = partita.getNumeroPianoCorrente();
        Optional<Battaglia> battaglia = partita.getBattaglia();
        if (battaglia.isPresent()) {
            etichettaMessaggio.setText("Piano " + numeroPiano + ": ti sbarra la strada "
                    + battaglia.get().getNemico().getNome());
        } else {
            etichettaMessaggio.setText("Piano " + numeroPiano + ": " + testoInvito());
        }
        eroe.setX(20);
        aggiornaCorridoio();
    }

    private void salvaPartita() {
        tasti.clear();
        TextInputDialog dialogo = new TextInputDialog(NOME_PREDEFINITO);
        dialogo.setTitle("Salva");
        dialogo.setHeaderText("Nome del salvataggio (lettere, numeri, - e _)");
        dialogo.setContentText("Nome:");
        Optional<String> risposta = dialogo.showAndWait();
        if (risposta.isEmpty()) {
            return;
        }
        String nome = risposta.get().trim();
        try {
            if (archivio.esiste(nome) && !confermaSovrascrittura(nome)) {
                return;
            }
            archivio.salva(nome, partita.creaDatiSalvataggio());
            etichettaMessaggio.setText("Partita salvata come '" + nome + "': " + descriviProgresso());
        } catch (IllegalArgumentException e) {
            etichettaMessaggio.setText("Nome non valido: " + e.getMessage());
        } catch (ErrorePersistenzaException e) {
            etichettaMessaggio.setText("Salvataggio non riuscito: " + e.getMessage());
        }
    }

    private boolean confermaSovrascrittura(String nome) {
        Alert avviso = new Alert(Alert.AlertType.CONFIRMATION,
                "Il salvataggio '" + nome + "' esiste già. Vuoi sovrascriverlo?",
                ButtonType.YES, ButtonType.NO);
        return avviso.showAndWait().filter(risposta -> risposta == ButtonType.YES).isPresent();
    }

    private void caricaPartita() {
        tasti.clear();
        try {
            List<String> nomi = archivio.elencaNomi();
            if (nomi.isEmpty()) {
                etichettaMessaggio.setText("Nessun salvataggio trovato.");
                return;
            }
            ChoiceDialog<String> dialogo = new ChoiceDialog<>(nomi.get(0), nomi);
            dialogo.setTitle("Carica");
            dialogo.setHeaderText("Scegli il salvataggio da caricare");
            dialogo.setContentText("Salvataggio:");
            Optional<String> scelto = dialogo.showAndWait();
            if (scelto.isEmpty()) {
                return;
            }
            partita.ripristina(archivio.carica(scelto.get()));
            mostraPiano();
            etichettaMessaggio.setText("Partita '" + scelto.get() + "' caricata: " + descriviProgresso());
        } catch (IllegalArgumentException e) {
            etichettaMessaggio.setText("Caricamento non riuscito: " + e.getMessage());
        } catch (ErrorePersistenzaException e) {
            etichettaMessaggio.setText("Caricamento non riuscito: " + e.getMessage());
        }
    }

    private String descriviProgresso() {
        Giocatore giocatore = partita.getGiocatore();
        return "piano " + partita.getNumeroPianoCorrente() + ", livello " + giocatore.getLivello()
                + ", HP " + giocatore.getHp() + ".";
    }

    private void aggiornaCorridoio() {
        etichettaPiano.setText("Piano " + partita.getNumeroPianoCorrente() + " di " + partita.getTotalePiani()
                + " - " + partita.getPianoCorrente().getTipo());
        etichettaEroeCorridoio.setText(testoEroe());
        etichettaNemicoCorridoio.setText(testoNemico());
        nemico.setFill(coloreOggettoDelPiano());
        nemico.setVisible(partita.getBattaglia().map(battaglia -> battaglia.getNemico().isVivo()).orElse(true));
        porta.setFill(partita.isPianoSuperato() ? Color.LIMEGREEN : Color.DARKGRAY);
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
        Optional<Battaglia> battaglia = partita.getBattaglia();
        if (battaglia.isPresent() && !battaglia.get().isFinita()
                && eroe.getBoundsInParent().intersects(nemico.getBoundsInParent())) {
            iniziaCombattimento();
        }
    }

    private void controllaOggettoDelPiano() {
        if (partita.getBattaglia().isEmpty() && !partita.getPianoCorrente().isCompletato()
                && eroe.getBoundsInParent().intersects(nemico.getBoundsInParent())) {
            etichettaMessaggio.setText("Piano " + partita.getNumeroPianoCorrente() + ": "
                    + partita.entraNelPianoCorrente());
            aggiornaCorridoio();
        }
    }

    private void controllaPorta() {
        if (partita.isPianoSuperato() && eroe.getBoundsInParent().intersects(porta.getBoundsInParent())) {
            if (!partita.avanzaAlProssimoPiano()) {
                etichettaMessaggio.setText("Hai conquistato la torre!");
                timer.stop();
                return;
            }
            mostraPiano();
        }
    }

    private void iniziaCombattimento() {
        timer.stop();
        tasti.clear();
        log.clear();
        log.appendText("--- " + battagliaInCorso().getNemico().getNome() + " ---\n");
        aggiornaCombattimento();
        finestra.setScene(scenaCombattimento);
    }

    private void attacca() {
        log.appendText(battagliaInCorso().attaccaConEroe());
        aggiornaCombattimento();
    }

    private void terminaTurno() {
        log.appendText(battagliaInCorso().terminaTurnoGiocatore());
        aggiornaCombattimento();
    }

    private void giocaCarta(int indice) {
        Giocatore giocatore = partita.getGiocatore();
        Battaglia battaglia = battagliaInCorso();
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
        Battaglia battaglia = battagliaInCorso();
        etichettaEroeCombattimento.setText(testoEroe());
        etichettaNemicoCombattimento.setText(testoNemico());
        aggiornaPulsantiCarte();
        pulsanteAttacca.setDisable(battaglia.isFinita()
                || partita.getGiocatore().getPuntiAzione() < Battaglia.COSTO_PA_ATTACCO_BASE);
        pulsanteTerminaTurno.setDisable(battaglia.isFinita());
        pulsanteTorna.setDisable(!battaglia.isVinta());
    }

    private void tornaAlCorridoio() {
        PianoTorre piano = partita.getPianoCorrente();
        etichettaMessaggio.setText(piano.getTipo() == TipoPiano.BOSS_FINALE
                ? "Hai sconfitto il " + piano.getNemico().getNome() + "! Raggiungi la porta per uscire dalla torre."
                : "Nemico sconfitto! Raggiungi la porta per salire.");
        aggiornaCorridoio();
        finestra.setScene(scenaCorridoio);
        timer.start();
    }

    private String testoEroe() {
        Giocatore giocatore = partita.getGiocatore();
        return giocatore.getNome() + " - HP " + giocatore.getHp() + "/" + giocatore.getHpMax()
                + " - PA " + giocatore.getPuntiAzione()
                + " - Attacco " + giocatore.getAttacco() + " - Difesa " + giocatore.getDifesa()
                + " - Livello " + giocatore.getLivello()
                + " (XP " + giocatore.getEsperienza() + "/" + giocatore.getEsperienzaProssimoLivello() + ")";
    }

    private String testoNemico() {
        Optional<Battaglia> battaglia = partita.getBattaglia();
        if (battaglia.isEmpty()) {
            return partita.getPianoCorrente().getTipo() == TipoPiano.CASSA ? "Cassa" : "Fontana di lava";
        }
        Nemico nemicoDelPiano = battaglia.get().getNemico();
        return nemicoDelPiano.getNome() + " - HP " + nemicoDelPiano.getHp() + "/" + nemicoDelPiano.getHpMax()
                + " - Attacco " + nemicoDelPiano.getAttacco() + " - Difesa " + nemicoDelPiano.getDifesa();
    }

    private String testoInvito() {
        return partita.getPianoCorrente().getTipo() == TipoPiano.CASSA
                ? "raggiungi la cassa per aprirla."
                : "raggiungi la fontana di lava per curarti.";
    }

    private Color coloreOggettoDelPiano() {
        TipoPiano tipo = partita.getPianoCorrente().getTipo();
        if (partita.getBattaglia().isPresent()) {
            return tipo == TipoPiano.BOSS_FINALE ? Color.DARKVIOLET : Color.CRIMSON;
        }
        return tipo == TipoPiano.CASSA ? Color.SADDLEBROWN : Color.ORANGE;
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