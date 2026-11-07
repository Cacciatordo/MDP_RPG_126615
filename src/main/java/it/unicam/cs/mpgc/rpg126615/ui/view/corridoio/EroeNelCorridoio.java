package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.EnumSet;
import java.util.Set;

public class EroeNelCorridoio {

    private static final int SCALA = 3;
    private static final String SPRITE_FERMO = "knight_m_idle_anim";
    private static final String SPRITE_CORSA = "knight_m_run_anim";
    private static final double MARGINE_CAMMINO = 20;
    private static final double POSIZIONE_INIZIALE = 0.2;
    private static final double VELOCITA = 220;
    private static final double NANOSECONDI_PER_SECONDO = 1_000_000_000.0;

    private final double larghezzaScena;
    private final Runnable dopoOgniSpostamento;
    private final SpriteAnimato spriteFermo = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_FERMO, SCALA);
    private final SpriteAnimato spriteCorsa = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_CORSA, SCALA);
    private final StackPane postazione = new StackPane(this.spriteFermo, this.spriteCorsa);
    private final Set<KeyCode> tastiPremuti = EnumSet.noneOf(KeyCode.class);
    private final CicloMovimento ciclo = new CicloMovimento();

    private double posizione;

    public EroeNelCorridoio(double larghezzaScena, double yPiedi, Runnable dopoOgniSpostamento) {
        this.larghezzaScena = larghezzaScena;
        this.dopoOgniSpostamento = dopoOgniSpostamento;
        this.posizione = larghezzaScena * POSIZIONE_INIZIALE;
        this.spriteCorsa.setVisible(false);
        this.postazione.setAlignment(Pos.BOTTOM_CENTER);
        this.postazione.setLayoutY(yPiedi - this.spriteFermo.getImage().getHeight());
        this.postazione.setLayoutX(Math.round(this.posizione));
    }

    public Node getNodo() {
        return this.postazione;
    }

    public double getCentroX() {
        return this.posizione + this.spriteFermo.getImage().getWidth() / 2;
    }

    public void tastoPremuto(KeyCode tasto) {
        this.tastiPremuti.add(tasto);
    }

    public void tastoRilasciato(KeyCode tasto) {
        this.tastiPremuti.remove(tasto);
    }

    public void avvia() {
        this.spriteFermo.avvia();
        this.spriteCorsa.avvia();
        this.ciclo.start();
    }

    public void ferma() {
        this.ciclo.stop();
        this.spriteFermo.ferma();
        this.spriteCorsa.ferma();
        this.tastiPremuti.clear();
    }

    private int direzioneDeiTasti() {
        boolean sinistra = this.tastiPremuti.contains(KeyCode.A) || this.tastiPremuti.contains(KeyCode.LEFT);
        boolean destra = this.tastiPremuti.contains(KeyCode.D) || this.tastiPremuti.contains(KeyCode.RIGHT);
        return (destra ? 1 : 0) - (sinistra ? 1 : 0);
    }

    private void muovi(double secondi) {
        int direzione = direzioneDeiTasti();
        this.spriteFermo.setVisible(direzione == 0);
        this.spriteCorsa.setVisible(direzione != 0);
        if (direzione == 0) {
            return;
        }
        double minimo = MARGINE_CAMMINO;
        double massimo = this.larghezzaScena - MARGINE_CAMMINO - this.spriteFermo.getImage().getWidth();
        double nuovaPosizione = this.posizione + direzione * VELOCITA * secondi;
        this.posizione = Math.max(minimo, Math.min(massimo, nuovaPosizione));
        this.postazione.setLayoutX(Math.round(this.posizione));
        this.postazione.setScaleX(direzione);
        this.dopoOgniSpostamento.run();
    }

    private class CicloMovimento extends AnimationTimer {

        private long istantePrecedente;

        @Override
        public void handle(long istante) {
            if (this.istantePrecedente != 0) {
                muovi((istante - this.istantePrecedente) / NANOSECONDI_PER_SECONDO);
            }
            this.istantePrecedente = istante;
        }

        @Override
        public void stop() {
            super.stop();
            this.istantePrecedente = 0;
        }
    }
}