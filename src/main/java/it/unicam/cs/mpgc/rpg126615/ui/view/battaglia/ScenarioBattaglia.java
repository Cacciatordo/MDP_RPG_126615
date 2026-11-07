package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Random;

public final class ScenarioBattaglia {

    private static final int SCALA = 4;
    private static final int LATO = 16 * SCALA;
    private static final int RIGHE_MURO = 3;
    private static final int SEME = 7;
    private static final double LUMINOSITA = -0.3;

    public static final double LINEA_PAVIMENTO = RIGHE_MURO * LATO;

    private static final String MURO = "wall_mid";
    private static final List<String> MURO_CREPATO = List.of("wall_hole_1", "wall_hole_2");
    private static final double PROBABILITA_CREPA = 0.12;
    private static final List<String> PAVIMENTO = List.of(
            "floor_1", "floor_1", "floor_1", "floor_2", "floor_3", "floor_5", "floor_5", "floor_6");

    private ScenarioBattaglia() {
    }

    public static Pane crea(double larghezza, double altezza) {
        Pane scenario = new Pane();
        scenario.setPrefSize(larghezza, altezza);
        scenario.setMouseTransparent(true);
        scenario.setEffect(new ColorAdjust(0, 0, LUMINOSITA, 0));

        Random caso = new Random(SEME);
        int colonne = (int) Math.ceil(larghezza / LATO);
        int righe = (int) Math.ceil(altezza / LATO);
        for (int riga = 0; riga < righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                String nome = riga < RIGHE_MURO ? scegliMattonellaMuro(caso) : scegliLastra(caso);
                aggiungi(scenario, nome, colonna * LATO, riga * LATO);
            }
        }
        return scenario;
    }

    private static String scegliMattonellaMuro(Random caso) {
        if (caso.nextDouble() < PROBABILITA_CREPA) {
            return MURO_CREPATO.get(caso.nextInt(MURO_CREPATO.size()));
        }
        return MURO;
    }

    private static String scegliLastra(Random caso) {
        return PAVIMENTO.get(caso.nextInt(PAVIMENTO.size()));
    }

    private static void aggiungi(Pane scenario, String nome, double x, double y) {
        ImageView immagine = new ImageView(PixelArt.carica(PixelArt.AMBIENTE, nome, SCALA));
        immagine.setSmooth(false);
        immagine.setLayoutX(x);
        immagine.setLayoutY(y);
        scenario.getChildren().add(immagine);
    }
}