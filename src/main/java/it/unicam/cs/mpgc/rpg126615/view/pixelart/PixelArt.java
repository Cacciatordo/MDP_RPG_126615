package it.unicam.cs.mpgc.rpg126615.view.pixelart;

import javafx.scene.image.Image;

import java.net.URL;

public final class PixelArt {

    public static final String PERSONAGGI = "/assets/personaggi/";
    public static final String AMBIENTE = "/assets/ambiente/";
    public static final String EROE_FERMO = "knight_m_idle_anim";

    private PixelArt() {
    }

    public static boolean esiste(String cartella, String nome) {
        return PixelArt.class.getResource(cartella + nome + ".png") != null;
    }

    public static Image carica(String cartella, String nome, int scala) {
        URL file = PixelArt.class.getResource(cartella + nome + ".png");
        if (file == null) {
            throw new IllegalStateException("Immagine non trovata: " + cartella + nome + ".png");
        }
        String indirizzo = file.toExternalForm();
        Image originale = new Image(indirizzo);
        return new Image(indirizzo, originale.getWidth() * scala, originale.getHeight() * scala, false, false);
    }
}