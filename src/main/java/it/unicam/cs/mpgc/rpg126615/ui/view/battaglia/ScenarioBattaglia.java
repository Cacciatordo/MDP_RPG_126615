package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class ScenarioBattaglia {

    private static final int LATO_MATTONELLA = 16;
    private static final int SCALA = 4;
    private static final int LATO = LATO_MATTONELLA * SCALA;
    private static final int SEME = 7;
    private static final int SOGLIA_TRASPARENZA = 128;

    private static final String MURO = "wall_mid";
    private static final String[] MURO_CREPATO = {"wall_hole_1", "wall_hole_2"};
    private static final double PROBABILITA_CREPA = 0.12;
    private static final String[] PAVIMENTO = {
            "floor_1", "floor_1", "floor_1", "floor_2", "floor_3", "floor_5", "floor_5", "floor_6"};

    private static final String PILASTRO = "column_wall";
    private static final double[] POSIZIONI_PILASTRI = {0.04, 0.96};
    private static final String[] STENDARDI = {"wall_banner_red", "wall_banner_yellow", "wall_banner_green"};
    private static final double[] POSIZIONI_STENDARDI = {0.37, 0.50, 0.63};

    private static final String TAPPETO = "tappeto_arena";
    private static final int MARGINE_LATERALE_TAPPETO = 54;
    private static final int DISTANZA_TAPPETO_DAL_MURO = 16;
    private static final int BORDO_TAPPETO = 8;

    private static final double LUMINOSITA_SOFFITTO = 0.36;
    private static final double LUMINOSITA_PARETE_BASSA = 0.72;
    private static final double LUMINOSITA_PAVIMENTO = 0.62;
    private static final double FORZA_VIGNETTATURA = 0.45;
    private static final int ALTEZZA_OMBRA_PAVIMENTO = 10;
    private static final double OMBRA_PAVIMENTO = 0.7;
    private static final double[] TINTA = {1.10, 0.88, 0.78};

    private ScenarioBattaglia() {
    }

    public static ImageView crea(double larghezza, double altezza, int lineaPavimento, int fondoTappeto) {
        int larghezzaPixel = (int) Math.ceil(larghezza);
        int altezzaPixel = (int) Math.ceil(altezza);
        Map<String, Mattonella> mattonelle = new HashMap<>();

        int colonne = (int) Math.ceil(larghezza / LATO);
        int righeMuro = (int) Math.ceil((double) lineaPavimento / LATO);
        int righePavimento = (int) Math.ceil((double) (altezzaPixel - lineaPavimento) / LATO);
        String[][] muro = disponiMuro(colonne, righeMuro, mattonelle);
        String[][] pavimento = disponiPavimento(colonne, righePavimento, mattonelle);
        List<Posizionata> decorazioni = posizionaDecorazioni(larghezzaPixel, mattonelle);
        Tappeto tappeto = creaTappeto(larghezzaPixel, fondoTappeto, lineaPavimento, mattonelle);

        WritableImage immagine = new WritableImage(larghezzaPixel, altezzaPixel);
        PixelWriter scrittore = immagine.getPixelWriter();
        for (int y = 0; y < altezzaPixel; y++) {
            for (int x = 0; x < larghezzaPixel; x++) {
                int colore = y < lineaPavimento
                        ? coloreMuro(x, y, lineaPavimento, muro, decorazioni, mattonelle)
                        : colorePavimento(x, y, lineaPavimento, pavimento, tappeto, mattonelle);
                scrittore.setArgb(x, y, illumina(colore, luminosita(x, y, lineaPavimento, larghezza)));
            }
        }

        ImageView vista = new ImageView(immagine);
        vista.setMouseTransparent(true);
        return vista;
    }

    private static String[][] disponiMuro(int colonne, int righe, Map<String, Mattonella> mattonelle) {
        Random caso = new Random(SEME);
        String[][] nomi = new String[Math.max(righe, 1)][colonne];
        for (String[] riga : nomi) {
            for (int c = 0; c < colonne; c++) {
                boolean crepa = caso.nextDouble() < PROBABILITA_CREPA;
                riga[c] = crepa ? MURO_CREPATO[caso.nextInt(MURO_CREPATO.length)] : MURO;
            }
        }
        carica(mattonelle, MURO);
        carica(mattonelle, MURO_CREPATO);
        return nomi;
    }

    private static String[][] disponiPavimento(int colonne, int righe, Map<String, Mattonella> mattonelle) {
        Random caso = new Random(SEME + 1);
        String[][] nomi = new String[Math.max(righe, 1)][colonne];
        for (String[] riga : nomi) {
            for (int c = 0; c < colonne; c++) {
                riga[c] = PAVIMENTO[caso.nextInt(PAVIMENTO.length)];
            }
        }
        carica(mattonelle, PAVIMENTO);
        return nomi;
    }

    private record Posizionata(Mattonella immagine, int sinistra) {
    }

    private static List<Posizionata> posizionaDecorazioni(int larghezza, Map<String, Mattonella> mattonelle) {
        List<Posizionata> posizionate = new ArrayList<>();
        for (double posizione : POSIZIONI_PILASTRI) {
            posizionate.add(posiziona(PILASTRO, posizione, larghezza, mattonelle));
        }
        for (int i = 0; i < STENDARDI.length; i++) {
            posizionate.add(posiziona(STENDARDI[i], POSIZIONI_STENDARDI[i], larghezza, mattonelle));
        }
        return posizionate;
    }

    private static Posizionata posiziona(String nome, double posizione, int larghezza,
                                         Map<String, Mattonella> mattonelle) {
        carica(mattonelle, nome);
        Mattonella immagine = mattonelle.get(nome);
        int meta = immagine.larghezza() * SCALA / 2;
        return new Posizionata(immagine, (int) (larghezza * posizione) - meta);
    }

    private record Tappeto(Mattonella immagine, int sinistra, int alto, int colonne, int righe) {

        int colore(int x, int y) {
            int colonna = (x - this.sinistra) / SCALA;
            int riga = (y - this.alto) / SCALA;
            if (x < this.sinistra || y < this.alto || colonna >= this.colonne || riga >= this.righe) {
                return 0;
            }
            return this.immagine.colore(
                    corrispondente(colonna, this.colonne, this.immagine.larghezza()),
                    corrispondente(riga, this.righe, this.immagine.altezza()));
        }

        private static int corrispondente(int indice, int totale, int totaleOriginale) {
            if (indice < BORDO_TAPPETO) {
                return indice;
            }
            if (indice >= totale - BORDO_TAPPETO) {
                return totaleOriginale - (totale - indice);
            }
            return BORDO_TAPPETO + (indice - BORDO_TAPPETO) % (totaleOriginale - 2 * BORDO_TAPPETO);
        }
    }

    private static Tappeto creaTappeto(int larghezza, int fondo, int lineaPavimento,
                                       Map<String, Mattonella> mattonelle) {
        carica(mattonelle, TAPPETO);
        int alto = lineaPavimento + DISTANZA_TAPPETO_DAL_MURO;
        int colonne = (larghezza - 2 * MARGINE_LATERALE_TAPPETO) / SCALA;
        int righe = (fondo - alto) / SCALA;
        int sinistra = (larghezza - colonne * SCALA) / 2;
        return new Tappeto(mattonelle.get(TAPPETO), sinistra, alto, colonne, righe);
    }

    private static int coloreMuro(int x, int y, int linea, String[][] muro, List<Posizionata> decorazioni,
                                  Map<String, Mattonella> mattonelle) {
        int sopraLinea = linea - 1 - y;

        for (Posizionata decorazione : decorazioni) {
            Mattonella immagine = decorazione.immagine();
            int px = (x - decorazione.sinistra()) / SCALA;
            int py = immagine.altezza() - 1 - sopraLinea / SCALA;
            if (x >= decorazione.sinistra() && px < immagine.larghezza() && py >= 0) {
                int colore = immagine.colore(px, py);
                if (colore >>> 24 >= SOGLIA_TRASPARENZA) {
                    return colore;
                }
            }
        }

        int px = (x % LATO) / SCALA;
        int py = LATO_MATTONELLA - 1 - (sopraLinea % LATO) / SCALA;
        int colore = mattonelle.get(muro[sopraLinea / LATO][x / LATO]).colore(px, py);
        if (colore >>> 24 < SOGLIA_TRASPARENZA) {
            colore = mattonelle.get(MURO).colore(px, py);
        }
        return colore;
    }

    private static int colorePavimento(int x, int y, int linea, String[][] pavimento, Tappeto tappeto,
                                       Map<String, Mattonella> mattonelle) {
        int coloreTappeto = tappeto.colore(x, y);
        if (coloreTappeto >>> 24 >= SOGLIA_TRASPARENZA) {
            return coloreTappeto;
        }
        int sottoLinea = y - linea;
        int px = (x % LATO) / SCALA;
        int py = (sottoLinea % LATO) / SCALA;
        int colore = mattonelle.get(pavimento[sottoLinea / LATO][x / LATO]).colore(px, py);
        if (sottoLinea < ALTEZZA_OMBRA_PAVIMENTO) {
            colore = scurisci(colore, OMBRA_PAVIMENTO);
        }
        return colore;
    }

    private static double luminosita(int x, int y, int linea, double larghezza) {
        double base;
        if (y < linea) {
            double verso = (double) y / linea;
            base = LUMINOSITA_SOFFITTO + (LUMINOSITA_PARETE_BASSA - LUMINOSITA_SOFFITTO) * verso;
        } else {
            base = LUMINOSITA_PAVIMENTO;
        }
        double distanzaDalCentro = Math.abs(x - larghezza / 2) / (larghezza / 2);
        return base * (1 - FORZA_VIGNETTATURA * distanzaDalCentro * distanzaDalCentro);
    }

    private static int illumina(int argb, double luminosita) {
        int rosso = (int) Math.min(255, ((argb >> 16) & 0xFF) * luminosita * TINTA[0]);
        int verde = (int) Math.min(255, ((argb >> 8) & 0xFF) * luminosita * TINTA[1]);
        int blu = (int) Math.min(255, (argb & 0xFF) * luminosita * TINTA[2]);
        return 0xFF000000 | rosso << 16 | verde << 8 | blu;
    }

    private static int scurisci(int argb, double fattore) {
        int rosso = (int) (((argb >> 16) & 0xFF) * fattore);
        int verde = (int) (((argb >> 8) & 0xFF) * fattore);
        int blu = (int) ((argb & 0xFF) * fattore);
        return (argb & 0xFF000000) | rosso << 16 | verde << 8 | blu;
    }

    private record Mattonella(int larghezza, int altezza, int[] pixel) {

        int colore(int x, int y) {
            return this.pixel[y * this.larghezza + x];
        }
    }

    private static void carica(Map<String, Mattonella> mattonelle, String... nomi) {
        for (String nome : nomi) {
            mattonelle.computeIfAbsent(nome, chiave -> leggi(chiave));
        }
    }

    private static Mattonella leggi(String nome) {
        URL file = ScenarioBattaglia.class.getResource(PixelArt.AMBIENTE + nome + ".png");
        if (file == null) {
            throw new IllegalStateException("Immagine non trovata: " + PixelArt.AMBIENTE + nome + ".png");
        }
        Image immagine = new Image(file.toExternalForm());
        int larghezza = (int) immagine.getWidth();
        int altezza = (int) immagine.getHeight();
        PixelReader lettore = immagine.getPixelReader();
        int[] pixel = new int[larghezza * altezza];
        for (int y = 0; y < altezza; y++) {
            for (int x = 0; x < larghezza; x++) {
                pixel[y * larghezza + x] = lettore.getArgb(x, y);
            }
        }
        return new Mattonella(larghezza, altezza, pixel);
    }
}