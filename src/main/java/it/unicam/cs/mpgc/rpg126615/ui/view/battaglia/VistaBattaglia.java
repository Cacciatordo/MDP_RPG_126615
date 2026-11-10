package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.ui.view.AspettoAvversario;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

public class VistaBattaglia {

    private static final double CIMA_CARTE = 375;
    private static final double CIMA_COMANDI = 530;
    private static final double CIMA_REGISTRO = 564;
    private static final double MARGINE_REGISTRO = 20;
    private static final String TESTO_INIZIO = "Battaglia contro %s. Gioca le carte, attacca o termina il turno.\n";

    private final Battaglia battaglia;
    private final PalcoBattaglia palco;
    private final FilaCarte filaCarte;
    private final BarraComandiBattaglia comandi;
    private final RegistroBattaglia registro;
    private final Scene scena;

    public VistaBattaglia(double larghezza, double altezza, Partita partita) {
        this.battaglia = partita.getBattaglia().orElseThrow();
        Combattente nemico = this.battaglia.getNemico();
        SpritePersonaggio aspettoNemico = AspettoAvversario.di(nemico)
                .personaggioDelPiano(partita.getNumeroPianoCorrente());
        this.palco = new PalcoBattaglia(larghezza, this.battaglia.getGiocatore(), nemico, aspettoNemico);
        this.filaCarte = new FilaCarte(larghezza, this.battaglia.getGiocatore(), indice -> giocaCarta(indice));
        this.comandi = new BarraComandiBattaglia(larghezza, () -> attacca(), () -> terminaTurno());
        this.registro = new RegistroBattaglia(larghezza - 2 * MARGINE_REGISTRO);

        Pane interfaccia = new Pane();
        interfaccia.setPickOnBounds(false);
        aggiungi(interfaccia, this.filaCarte.getNodo(), 0, CIMA_CARTE);
        aggiungi(interfaccia, this.comandi.getNodo(), 0, CIMA_COMANDI);
        aggiungi(interfaccia, this.registro.getNodo(), MARGINE_REGISTRO, CIMA_REGISTRO);

        StackPane radice = new StackPane(ScenarioBattaglia.crea(larghezza, altezza),
                this.palco.getLivello(), interfaccia);
        this.scena = new Scene(radice, larghezza, altezza);
        this.scena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));

        this.registro.aggiungi(TESTO_INIZIO.formatted(nemico.getNome()));
        this.comandi.abilita(!this.battaglia.isFinita());
    }

    public Scene getScena() {
        return this.scena;
    }

    private void attacca() {
        mostraEsito(this.battaglia.attaccaConEroe());
    }

    private void terminaTurno() {
        mostraEsito(this.battaglia.terminaTurnoGiocatore());
    }

    private void giocaCarta(int indice) {
        if (!this.battaglia.isFinita()) {
            mostraEsito(this.battaglia.giocaCartaDallaMano(indice));
        }
    }

    private void mostraEsito(String esito) {
        this.registro.aggiungi(esito);
        this.palco.aggiorna();
        this.filaCarte.aggiorna();
        this.comandi.abilita(!this.battaglia.isFinita());
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