package it.unicam.cs.mpgc.rpg126615.view.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.view.stile.Componenti;
import it.unicam.cs.mpgc.rpg126615.view.stile.Tema;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class SchedaCombattente {

    public static final double LARGHEZZA = 260;

    private static final double LARGHEZZA_BARRA = 230;
    private static final double SPAZIO = 4;
    private static final int DIMENSIONE_NOME = 10;
    private static final int DIMENSIONE_HP = 9;
    private static final int DIMENSIONE_STATISTICHE = 8;

    private final Combattente combattente;
    private final Supplier<String> fornitoreNome;
    private final EtichetteStatistiche etichette;
    private final Label etichettaNome = creaEtichettaCentrata(DIMENSIONE_NOME, Tema.TESTO);
    private final ProgressBar barraHp = new ProgressBar(1.0);
    private final Label etichettaHp = creaEtichettaCentrata(DIMENSIONE_HP, Tema.TESTO);
    private final Label etichettaStatistiche = creaEtichettaCentrata(DIMENSIONE_STATISTICHE, Tema.ORO);
    private final VBox contenitore;

    private SchedaCombattente(Combattente combattente, Supplier<String> fornitoreNome,
                              EtichetteStatistiche etichette, String coloreBarra) {
        this.combattente = combattente;
        this.fornitoreNome = fornitoreNome;
        this.etichette = etichette;
        this.barraHp.setPrefWidth(LARGHEZZA_BARRA);
        this.barraHp.setStyle("-fx-accent: " + coloreBarra + "; -fx-control-inner-background: " + Tema.SFONDO + "; "
                + "-fx-background-radius: 0; -fx-border-color: " + Tema.BORDO + "; -fx-border-width: 2;");

        this.contenitore = new VBox(SPAZIO, this.etichettaNome, this.barraHp,
                this.etichettaHp, this.etichettaStatistiche);
        this.contenitore.setAlignment(Pos.CENTER);
        this.contenitore.setMinWidth(LARGHEZZA);
        this.contenitore.setPrefWidth(LARGHEZZA);
        this.contenitore.setMaxWidth(LARGHEZZA);
        aggiorna();
    }

    public static SchedaCombattente perEroe(Giocatore eroe, String coloreBarra) {
        return new SchedaCombattente(eroe,
                () -> eroe.getNome() + " (Liv. " + eroe.getLivello() + ")",
                EtichetteStatistiche.EROE, coloreBarra);
    }

    public static SchedaCombattente perAvversario(Combattente avversario, String coloreBarra) {
        return new SchedaCombattente(avversario, avversario::getNome,
                EtichetteStatistiche.AVVERSARIO, coloreBarra);
    }

    public Node getNodo() {
        return this.contenitore;
    }

    public void aggiorna() {
        this.etichettaNome.setText(this.fornitoreNome.get());
        this.barraHp.setProgress((double) this.combattente.getHp() / this.combattente.getHpMax());
        this.etichettaHp.setText("HP: " + this.combattente.getHp() + " / " + this.combattente.getHpMax());
        this.etichettaStatistiche.setText(creaStatistiche());
    }

    private String creaStatistiche() {
        return "⚔ " + this.etichette.getAttacco() + ": " + this.combattente.getAttacco()
                + "  |  🛡 " + this.etichette.getDifesa() + ": " + this.combattente.getDifesa();
    }


    private static Label creaEtichettaCentrata(int dimensione, String colore) {
        return Componenti.etichettaCentrata("", dimensione, colore, LARGHEZZA);
    }

}