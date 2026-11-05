package it.unicam.cs.mpgc.rpg126615.ui.view;

import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.ArredoCorridoio;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.PixelArt;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpriteAnimato;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;
import it.unicam.cs.mpgc.rpg126615.ui.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class EntitaCorridoio {

    private static final double VERSO_SINISTRA = -1;
    private static final String STILE_SUGGERIMENTO = "-fx-font-family: '" + Tema.FONT + "'; -fx-font-size: 9px;";

    private final Pane livello = new Pane();
    private final double centroOrizzontale;
    private final double yBase;
    private final List<SpriteAnimato> animazioni = new ArrayList<>();

    public EntitaCorridoio(double larghezzaScena, double yBase) {
        this.centroOrizzontale = larghezzaScena / 2;
        this.yBase = yBase;
        this.livello.setPickOnBounds(false);
    }

    public Pane getLivello() {
        return this.livello;
    }

    public void mostra(PianoTorre piano) {
        switch (piano.getTipo()) {
            case COMBATTIMENTO, BOSS, BOSS_FINALE -> mostraAvversario(piano);
            case CASSA -> mostraCassa();
            case RIPOSO -> mostraFontana();
        }
    }

    public void avvia() {
        this.animazioni.forEach(SpriteAnimato::avvia);
    }

    public void ferma() {
        this.animazioni.forEach(SpriteAnimato::ferma);
    }

    private void mostraAvversario(PianoTorre piano) {
        Nemico nemico = piano.getNemico();
        AspettoAvversario aspetto = AspettoAvversario.di(nemico);
        SpritePersonaggio personaggio = aspetto.personaggioDelPiano(piano.getNumero());
        SpriteAnimato sprite = new SpriteAnimato(PixelArt.PERSONAGGI, personaggio.getPrefisso(), personaggio.getScala());
        sprite.setScaleX(VERSO_SINISTRA);
        this.animazioni.add(sprite);

        Text icona = new Text(aspetto.getIcona());
        icona.setFont(Font.font(Tema.FONT, aspetto.getDimensioneIcona()));
        icona.setFill(Color.WHITE);
        icona.setTranslateY(-aspetto.getAltezzaIcona());
        StackPane.setAlignment(icona, Pos.TOP_CENTER);

        aggiungi(new StackPane(sprite, icona), sprite.getImage().getWidth(), sprite.getImage().getHeight(),
                aspetto.descrivi(nemico.getNome()));
    }

    private void mostraCassa() {
        aggiungi(ArredoCorridoio.cassaChiusa(), ArredoCorridoio.LATO, ArredoCorridoio.LATO,
                "Antica cassa: aprila per trovare un premio... o una trappola!");
    }

    private void mostraFontana() {
        ArredoCorridoio.Fontana fontana = ArredoCorridoio.fontanaAccesa();
        this.animazioni.addAll(fontana.animazioni());
        aggiungi(fontana.nodo(), ArredoCorridoio.LATO, ArredoCorridoio.ALTEZZA_FONTANA,
                "Fontana di lava: riposa e recupera i tuoi HP!");
    }

    private void aggiungi(Node nodo, double larghezza, double altezza, String descrizione) {
        StackPane postazione = new StackPane(nodo);
        postazione.setMinSize(larghezza, altezza);
        postazione.setPrefSize(larghezza, altezza);
        postazione.setMaxSize(larghezza, altezza);
        postazione.setLayoutX(Math.round(this.centroOrizzontale - larghezza / 2));
        postazione.setLayoutY(this.yBase - altezza);

        Tooltip suggerimento = new Tooltip(descrizione);
        suggerimento.setStyle(STILE_SUGGERIMENTO);
        Tooltip.install(postazione, suggerimento);

        this.livello.getChildren().add(postazione);
    }
}