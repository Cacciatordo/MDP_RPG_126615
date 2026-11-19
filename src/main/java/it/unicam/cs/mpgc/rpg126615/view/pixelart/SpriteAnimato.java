package it.unicam.cs.mpgc.rpg126615.view.pixelart;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SpriteAnimato extends ImageView {

    private static final int FOTOGRAMMI_MASSIMI = 16;
    private static final double MILLISECONDI_PER_FOTOGRAMMA = 160;

    private final List<Image> fotogrammi;
    private final Timeline animazione;
    private int indice;

    public SpriteAnimato(String cartella, String prefisso, int scala) {
        this.fotogrammi = carica(cartella, prefisso, scala);
        setImage(this.fotogrammi.get(0));
        setSmooth(false);

        this.animazione = new Timeline(new KeyFrame(
                Duration.millis(MILLISECONDI_PER_FOTOGRAMMA), e -> mostraProssimoFotogramma()));
        this.animazione.setCycleCount(Animation.INDEFINITE);
    }

    public void avvia() {
        this.animazione.play();
    }

    public void ferma() {
        this.animazione.stop();
    }

    private void mostraProssimoFotogramma() {
        this.indice = (this.indice + 1) % this.fotogrammi.size();
        setImage(this.fotogrammi.get(this.indice));
    }

    private static List<Image> carica(String cartella, String prefisso, int scala) {
        List<Image> immagini = new ArrayList<>();
        for (int i = 0; i < FOTOGRAMMI_MASSIMI; i++) {
            String nome = prefisso + "_f" + i;
            if (!PixelArt.esiste(cartella, nome)) {
                break;
            }
            immagini.add(PixelArt.carica(cartella, nome, scala));
        }
        if (immagini.isEmpty()) {
            throw new IllegalStateException("Immagini non trovate in " + cartella + " per: " + prefisso);
        }
        return immagini;
    }
}