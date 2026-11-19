package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.view.pixelart.PixelArt;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public final class ScenarioBattaglia {

    private static final int SCALA = 4;
    private static final int LATO = 16 * SCALA;
    private static final int SEME = 7;

    private static final String MURO = "wall_mid";
    private static final String[] MURO_CREPATO = {"wall_hole_1", "wall_hole_2"};
    private static final double PROBABILITA_CREPA = 0.12;
    private static final String[] PAVIMENTO = {
            "floor_1", "floor_1", "floor_1", "floor_2", "floor_3", "floor_5", "floor_5", "floor_6"};

    private static final String PILASTRO = "column_wall";
    private static final double[] POSIZIONI_PILASTRI = {0.04, 0.96};
    private static final String[] STENDARDI = {"wall_banner_red", "wall_banner_yellow", "wall_banner_green"};
    private static final double[] POSIZIONI_STENDARDI = {0.37, 0.50, 0.63};

    private static final int MARGINE_LATERALE_TAPPETO = 54;
    private static final int DISTANZA_TAPPETO_DAL_MURO = 16;
    private static final int SPESSORE_BORDO_TAPPETO = 2 * SCALA;
    private static final Color COLORE_TAPPETO = Color.web("#da4e38");
    private static final Color COLORE_BORDO_TAPPETO = Color.web("#ee8e2e");

    private static final Color COLORE_OMBRA = Color.rgb(20, 8, 0);
    private static final double OPACITA_SOFFITTO = 0.64;
    private static final double OPACITA_PARETE_BASSA = 0.28;
    private static final double OPACITA_PAVIMENTO = 0.38;
    private static final double OPACITA_BORDI = 0.3;
    private static final int ALTEZZA_OMBRA_PAVIMENTO = 10;
    private static final double OPACITA_OMBRA_PAVIMENTO = 0.3;

    private ScenarioBattaglia() {
    }

    public static Node crea(double larghezza, double altezza, int lineaPavimento, int fondoTappeto) {
        Pane scenario = new Pane();
        scenario.setPrefSize(larghezza, altezza);
        scenario.setMinSize(larghezza, altezza);
        scenario.setMaxSize(larghezza, altezza);
        scenario.setClip(new Rectangle(larghezza, altezza));
        scenario.setMouseTransparent(true);

        disponiMuro(scenario, larghezza, lineaPavimento);
        disponiPavimento(scenario, larghezza, altezza, lineaPavimento);
        disponiTappeto(scenario, larghezza, lineaPavimento, fondoTappeto);
        aggiungiLuci(scenario, larghezza, altezza, lineaPavimento);
        return scenario;
    }

    private static void disponiMuro(Pane scenario, double larghezza, int lineaPavimento) {
        Random caso = new Random(SEME);
        Image muro = carica(MURO);
        Image[] muriCrepati = caricaTutte(MURO_CREPATO);
        int colonne = (int) Math.ceil(larghezza / LATO);
        int righe = (int) Math.ceil((double) lineaPavimento / LATO);
        for (int riga = 1; riga <= righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                int x = colonna * LATO;
                int y = lineaPavimento - riga * LATO;
                aggiungiImmagine(scenario, muro, x, y);
                if (caso.nextDouble() < PROBABILITA_CREPA) {
                    aggiungiImmagine(scenario, muriCrepati[caso.nextInt(muriCrepati.length)], x, y);
                }
            }
        }
        for (double posizione : POSIZIONI_PILASTRI) {
            aggiungiDecorazione(scenario, PILASTRO, posizione, larghezza, lineaPavimento);
        }
        for (int i = 0; i < STENDARDI.length; i++) {
            aggiungiDecorazione(scenario, STENDARDI[i], POSIZIONI_STENDARDI[i], larghezza, lineaPavimento);
        }
    }

    private static void aggiungiDecorazione(Pane scenario, String nome, double posizione,
                                            double larghezza, int lineaPavimento) {
        Image immagine = carica(nome);
        double x = larghezza * posizione - immagine.getWidth() / 2;
        aggiungiImmagine(scenario, immagine, Math.round(x), lineaPavimento - immagine.getHeight());
    }

    private static void disponiPavimento(Pane scenario, double larghezza, double altezza, int lineaPavimento) {
        Random caso = new Random(SEME + 1);
        Image[] mattonelle = caricaTutte(PAVIMENTO);
        int colonne = (int) Math.ceil(larghezza / LATO);
        int righe = (int) Math.ceil((altezza - lineaPavimento) / LATO);
        for (int riga = 0; riga < righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                aggiungiImmagine(scenario, mattonelle[caso.nextInt(mattonelle.length)],
                        colonna * LATO, lineaPavimento + riga * LATO);
            }
        }
    }

    private static void disponiTappeto(Pane scenario, double larghezza, int lineaPavimento, int fondoTappeto) {
        double alto = lineaPavimento + DISTANZA_TAPPETO_DAL_MURO;
        Rectangle tappeto = new Rectangle(MARGINE_LATERALE_TAPPETO, alto,
                larghezza - 2 * MARGINE_LATERALE_TAPPETO, fondoTappeto - alto);
        tappeto.setFill(COLORE_TAPPETO);
        tappeto.setStroke(COLORE_BORDO_TAPPETO);
        tappeto.setStrokeWidth(SPESSORE_BORDO_TAPPETO);
        scenario.getChildren().add(tappeto);
    }

    private static void aggiungiLuci(Pane scenario, double larghezza, double altezza, int lineaPavimento) {
        Paint sfumaturaParete = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, ombra(OPACITA_SOFFITTO)), new Stop(1, ombra(OPACITA_PARETE_BASSA)));
        Paint sfumaturaBordi = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, ombra(OPACITA_BORDI)), new Stop(0.3, ombra(0)),
                new Stop(0.7, ombra(0)), new Stop(1, ombra(OPACITA_BORDI)));
        scenario.getChildren().addAll(
                rettangolo(0, 0, larghezza, lineaPavimento, sfumaturaParete),
                rettangolo(0, lineaPavimento, larghezza, altezza - lineaPavimento, ombra(OPACITA_PAVIMENTO)),
                rettangolo(0, lineaPavimento, larghezza, ALTEZZA_OMBRA_PAVIMENTO, ombra(OPACITA_OMBRA_PAVIMENTO)),
                rettangolo(0, 0, larghezza, altezza, sfumaturaBordi));
    }

    private static Rectangle rettangolo(double x, double y, double larghezza, double altezza, Paint colore) {
        Rectangle rettangolo = new Rectangle(x, y, larghezza, altezza);
        rettangolo.setFill(colore);
        return rettangolo;
    }

    private static Color ombra(double opacita) {
        return Color.color(COLORE_OMBRA.getRed(), COLORE_OMBRA.getGreen(), COLORE_OMBRA.getBlue(), opacita);
    }

    private static void aggiungiImmagine(Pane scenario, Image immagine, double x, double y) {
        ImageView vista = new ImageView(immagine);
        vista.setLayoutX(x);
        vista.setLayoutY(y);
        scenario.getChildren().add(vista);
    }

    private static Image carica(String nome) {
        return PixelArt.carica(PixelArt.AMBIENTE, nome, SCALA);
    }

    private static Image[] caricaTutte(String[] nomi) {
        Image[] immagini = new Image[nomi.length];
        for (int i = 0; i < nomi.length; i++) {
            immagini[i] = carica(nomi[i]);
        }
        return immagini;
    }
}