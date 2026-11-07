package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class PalcoBattaglia {

    private static final int SCALA_TORCE = 4;
    private static final int SCALA_EROE = 3;
    private static final String TORCIA = "torcia";
    private static final String SPRITE_EROE = "knight_m_idle_anim";
    private static final double[] POSIZIONI_TORCE = {0.115, 0.885};
    private static final double SCARTO_BASE_TORCIA = 14;
    private static final double POSIZIONE_NEMICO = 0.28;
    private static final double POSIZIONE_EROE = 0.75;
    private static final double SCARTO_PIEDI = 48;
    private static final double Y_PIEDI = ScenarioBattaglia.LINEA_PAVIMENTO + SCARTO_PIEDI;
    private static final int VERSO_DESTRA = 1;
    private static final int VERSO_SINISTRA = -1;

    private final Pane livello = new Pane();
    private final List<SpriteAnimato> animazioni = new ArrayList<>();

    public PalcoBattaglia(double larghezza, SpritePersonaggio nemico) {
        this.livello.setPickOnBounds(false);
        aggiungiTorce(larghezza);
        aggiungiCombattente(new SpriteAnimato(PixelArt.PERSONAGGI, nemico.getPrefisso(), nemico.getScala()),
                larghezza * POSIZIONE_NEMICO, VERSO_DESTRA);
        aggiungiCombattente(new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_EROE, SCALA_EROE),
                larghezza * POSIZIONE_EROE, VERSO_SINISTRA);
    }

    public Pane getLivello() {
        return this.livello;
    }

    public void avvia() {
        this.animazioni.forEach(sprite -> sprite.avvia());
    }

    public void ferma() {
        this.animazioni.forEach(sprite -> sprite.ferma());
    }

    private void aggiungiTorce(double larghezza) {
        for (double posizione : POSIZIONI_TORCE) {
            SpriteAnimato torcia = new SpriteAnimato(PixelArt.AMBIENTE, TORCIA, SCALA_TORCE);
            torcia.setLayoutX(Math.round(larghezza * posizione - torcia.getImage().getWidth() / 2));
            torcia.setLayoutY(Math.round(ScenarioBattaglia.LINEA_PAVIMENTO + SCARTO_BASE_TORCIA
                    - torcia.getImage().getHeight()));
            this.animazioni.add(torcia);
            this.livello.getChildren().add(torcia);
        }
    }

    private void aggiungiCombattente(SpriteAnimato sprite, double centroX, int verso) {
        sprite.setLayoutX(Math.round(centroX - sprite.getImage().getWidth() / 2));
        sprite.setLayoutY(Math.round(Y_PIEDI - sprite.getImage().getHeight()));
        sprite.setScaleX(verso);
        this.animazioni.add(sprite);
        this.livello.getChildren().add(sprite);
    }
}