package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class PalcoBattaglia {

    public static final double ALTEZZA = 230;
    public static final double LINEA_PAVIMENTO = 100;

    private static final int SCALA_TORCE = 4;
    private static final int SCALA_EROE = 4;
    private static final String TORCIA = "torcia";
    private static final String SPRITE_EROE = "knight_m_idle_anim";
    private static final double[] POSIZIONI_TORCE = {0.115, 0.885};
    private static final double SCARTO_BASE_TORCIA = 14;
    private static final double POSIZIONE_NEMICO = 0.75;
    private static final double POSIZIONE_EROE = 0.28;
    private static final double Y_PIEDI = 140;
    private static final double SCARTO_SCHEDA = 2;
    private static final int VERSO_DESTRA = 1;
    private static final int VERSO_SINISTRA = -1;

    private final Pane livello = new Pane();
    private final List<SpriteAnimato> animazioni = new ArrayList<>();
    private final EffettoColpo effettoColpo = new EffettoColpo(this.livello);
    private final SpriteAnimato spriteEroe;
    private final SpriteAnimato spriteNemico;
    private final SchedaCombattente schedaEroe;
    private final SchedaCombattente schedaNemico;

    public PalcoBattaglia(double larghezza, Combattente eroe, Combattente nemico,
                          SpritePersonaggio aspettoNemico) {
        this.livello.setPickOnBounds(false);
        aggiungiTorce(larghezza);
        this.spriteNemico = new SpriteAnimato(PixelArt.PERSONAGGI, aspettoNemico.getPrefisso(),
                aspettoNemico.getScala());
        this.spriteEroe = new SpriteAnimato(PixelArt.PERSONAGGI, SPRITE_EROE, SCALA_EROE);
        aggiungiCombattente(this.spriteNemico, larghezza * POSIZIONE_NEMICO, VERSO_SINISTRA);
        aggiungiCombattente(this.spriteEroe, larghezza * POSIZIONE_EROE, VERSO_DESTRA);
        this.schedaNemico = new SchedaCombattente(nemico, Tema.ROSSO);
        this.schedaEroe = new SchedaCombattente(eroe, Tema.VERDE);
        aggiungiScheda(this.schedaNemico, larghezza * POSIZIONE_NEMICO);
        aggiungiScheda(this.schedaEroe, larghezza * POSIZIONE_EROE);
    }

    public Pane getLivello() {
        return this.livello;
    }

    public void aggiorna() {
        this.schedaEroe.aggiorna();
        this.schedaNemico.aggiorna();
    }

    public void mostraAttaccoEroe(int danno) {
        this.effettoColpo.colpisci(this.spriteEroe, VERSO_DESTRA, this.spriteNemico, "-" + danno, Tema.ORO);
    }

    public void mostraAttaccoNemico(int danno) {
        String testo = danno > 0 ? "-" + danno : "Bloccato";
        String colore = danno > 0 ? Tema.ROSSO : Tema.TESTO_SECONDARIO;
        this.effettoColpo.colpisci(this.spriteNemico, VERSO_SINISTRA, this.spriteEroe, testo, colore);
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
            torcia.setLayoutY(Math.round(LINEA_PAVIMENTO + SCARTO_BASE_TORCIA - torcia.getImage().getHeight()));
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

    private void aggiungiScheda(SchedaCombattente scheda, double centroX) {
        Node nodo = scheda.getNodo();
        nodo.setLayoutX(Math.round(centroX - SchedaCombattente.LARGHEZZA / 2));
        nodo.setLayoutY(Y_PIEDI + SCARTO_SCHEDA);
        this.livello.getChildren().add(nodo);
    }
}