package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.ui.view.AspettoAvversario;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

public class VistaBattaglia {

    private static final double CIMA_CARTE = 375;

    private final PalcoBattaglia palco;
    private final FilaCarte filaCarte;
    private final Scene scena;

    public VistaBattaglia(double larghezza, double altezza, Partita partita) {
        Combattente nemico = partita.getPianoCorrente().getNemico();
        SpritePersonaggio aspettoNemico = AspettoAvversario.di(nemico)
                .personaggioDelPiano(partita.getNumeroPianoCorrente());
        this.palco = new PalcoBattaglia(larghezza, partita.getGiocatore(), nemico, aspettoNemico);
        this.filaCarte = new FilaCarte(larghezza, partita.getGiocatore());
        this.filaCarte.getNodo().setLayoutY(CIMA_CARTE);

        Pane interfaccia = new Pane(this.filaCarte.getNodo());
        interfaccia.setPickOnBounds(false);

        StackPane radice = new StackPane(ScenarioBattaglia.crea(larghezza, altezza),
                this.palco.getLivello(), interfaccia);
        this.scena = new Scene(radice, larghezza, altezza);
        this.scena.windowProperty().addListener(
                (osservato, precedente, attuale) -> animaSoloSeMostrata(attuale));
    }

    public Scene getScena() {
        return this.scena;
    }

    private void animaSoloSeMostrata(Window finestra) {
        if (finestra == null) {
            this.palco.ferma();
        } else {
            this.palco.avvia();
        }
    }
}