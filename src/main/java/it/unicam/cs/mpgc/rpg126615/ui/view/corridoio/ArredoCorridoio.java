package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public final class ArredoCorridoio {

    private static final int SCALA = 4;

    public static final double LATO = 16 * SCALA;
    public static final double ALTEZZA_FONTANA = 3 * LATO;

    private static final String CIMA_FONTANA = "wall_fountain_top_1";
    private static final String CENTRO_FONTANA = "wall_fountain_mid_red_anim";
    private static final String BASE_FONTANA = "wall_fountain_basin_red_anim";
    private static final String CASSA_CHIUSA = "chest_full_open_anim_f0";

    private ArredoCorridoio() {
    }

    public record Fontana(Node nodo, List<SpriteAnimato> animazioni) {
    }

    public static Fontana fontanaAccesa() {
        SpriteAnimato centro = new SpriteAnimato(PixelArt.AMBIENTE, CENTRO_FONTANA, SCALA);
        SpriteAnimato base = new SpriteAnimato(PixelArt.AMBIENTE, BASE_FONTANA, SCALA);
        return new Fontana(new VBox(vista(CIMA_FONTANA), centro, base), List.of(centro, base));
    }

    public static ImageView cassaChiusa() {
        return vista(CASSA_CHIUSA);
    }

    private static ImageView vista(String nomeImmagine) {
        ImageView vista = new ImageView(PixelArt.carica(PixelArt.AMBIENTE, nomeImmagine, SCALA));
        vista.setSmooth(false);
        return vista;
    }
}