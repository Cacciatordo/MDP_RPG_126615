package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.view.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.view.pixelart.SpriteAnimato;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.EnumSet;
import java.util.Set;

public class EroeNelCorridoio {

    private static final int SCALA = 3;
    private static final String SPRITE_FERMO = "knight_m_idle_anim";
    private static final String SPRITE_CORSA = "knight_m_run_anim";
    private static final double MARGINE_CAMMINO = 20;
    private static final double VELOCITA = 220;
    private static final double NANOSECONDI_PER_SECONDO = 1_000_000_000.0;
    private static final Duration DURATA_SCALA = Duration.millis(700);
    private static final double SALITA_DX = 56;
    private static final double SALITA_DY = -28;
    private static final double DISCESA_DX = -40;
    private static final double DISCESA_DY = 32;
    private static final double VERSO_DESTRA = 1;
    private static final double VERSO_SINISTRA = -1;
    private static final double FRAZIONE_VISIBILE_SALITA = 0.4;
    private static final double FRAZIONE_VISIBILE_DISCESA = 0.75;
    private static final double FRAZIONE_VISIBILE_ARRIVO = 0.6;
    private static final CorsaEroe CORSA_SALITA =
            new CorsaEroe(0, 0, SALITA_DX, SALITA_DY, FRAZIONE_VISIBILE_SALITA, VERSO_DESTRA);
    private static final CorsaEroe CORSA_DISCESA =
            new CorsaEroe(0, 0, DISCESA_DX, DISCESA_DY, FRAZIONE_VISIBILE_DISCESA, VERSO_SINISTRA);
    private static final CorsaEroe CORSA_ARRIVO_DA_SOTTO =
            new CorsaEroe(DISCESA_DX, DISCESA_DY, 0, 0, FRAZIONE_VISIBILE_ARRIVO, VERSO_DESTRA);
    private static final CorsaEroe CORSA_ARRIVO_DA_SOPRA =
            new CorsaEroe(SALITA_DX, SALITA_DY, 0, 0, FRAZIONE_VISIBILE_ARRIVO, VERSO_SINISTRA);

    private final double larghezzaScena;
    private final Runnable dopoOgniSpostamento;
    private final SpriteAnimato spriteFermo = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_FERMO, SCALA);
    private final SpriteAnimato spriteCorsa = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_CORSA, SCALA);
    private final StackPane postazione = new StackPane(this.spriteFermo, this.spriteCorsa);
    private final Set<KeyCode> tastiPremuti = EnumSet.noneOf(KeyCode.class);
    private final CicloMovimento ciclo = new CicloMovimento();

    private double posizione;
    private boolean inAnimazione;

    public EroeNelCorridoio(double larghezzaScena, double yPiedi, double posizioneIniziale,
                            Runnable dopoOgniSpostamento) {
        this.larghezzaScena = larghezzaScena;
        this.dopoOgniSpostamento = dopoOgniSpostamento;
        this.posizione = posizioneIniziale;
        this.spriteCorsa.setVisible(false);
        this.postazione.setAlignment(Pos.BOTTOM_CENTER);
        this.postazione.setLayoutY(yPiedi - this.spriteFermo.getImage().getHeight());
        this.postazione.setLayoutX(Math.round(this.posizione));
    }

    public Node getNodo() {
        return this.postazione;
    }

    public double getPosizione() {
        return this.posizione;
    }

    public double getCentroX() {
        return this.posizione + this.spriteFermo.getImage().getWidth() / 2;
    }

    public void tastoPremuto(KeyCode tasto) {
        if (!this.inAnimazione) {
            this.tastiPremuti.add(tasto);
        }
    }

    public void fermaMovimento() {
        this.tastiPremuti.clear();
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

    public void nascondi() {
        this.postazione.setOpacity(0);
    }

    public void animaSalita(Runnable alTermine) {
        muovi(CORSA_SALITA, alTermine);
    }

    public void animaDiscesa(Runnable alTermine) {
        muovi(CORSA_DISCESA, alTermine);
    }

    public void animaArrivoDaSotto(Runnable alTermine) {
        muovi(CORSA_ARRIVO_DA_SOTTO, alTermine);
    }

    public void animaArrivoDaSopra(Runnable alTermine) {
        muovi(CORSA_ARRIVO_DA_SOPRA, alTermine);
    }

    private void muovi(CorsaEroe corsa, Runnable alTermine) {
        this.inAnimazione = true;
        this.tastiPremuti.clear();
        this.postazione.setScaleX(corsa.orientamento());
        this.spriteFermo.setVisible(false);
        this.spriteCorsa.setVisible(true);
        posiziona(corsa, 0);

        Transition transizione = new Transition() {
            {
                setCycleDuration(DURATA_SCALA);
            }

            @Override
            protected void interpolate(double frazione) {
                posiziona(corsa, frazione);
            }
        };
        transizione.setOnFinished(evento -> {
            this.inAnimazione = false;
            this.spriteFermo.setVisible(true);
            this.spriteCorsa.setVisible(false);
            alTermine.run();
        });
        transizione.play();
    }

    private void posiziona(CorsaEroe corsa, double frazione) {
        this.postazione.setTranslateX(Math.round(corsa.spostamentoX(frazione)));
        this.postazione.setTranslateY(Math.round(corsa.spostamentoY(frazione)));
        this.postazione.setOpacity(corsa.opacita(frazione));
    }

    private int direzioneDeiTasti() {
        boolean sinistra = this.tastiPremuti.contains(KeyCode.A) || this.tastiPremuti.contains(KeyCode.LEFT);
        boolean destra = this.tastiPremuti.contains(KeyCode.D) || this.tastiPremuti.contains(KeyCode.RIGHT);
        return (destra ? 1 : 0) - (sinistra ? 1 : 0);
    }

    private void muovi(double secondi) {
        if (this.inAnimazione) {
            return;
        }
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