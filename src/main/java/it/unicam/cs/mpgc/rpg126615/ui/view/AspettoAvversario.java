package it.unicam.cs.mpgc.rpg126615.ui.view;

import it.unicam.cs.mpgc.rpg126615.model.entita.Combattente;
import it.unicam.cs.mpgc.rpg126615.ui.pixelart.SpritePersonaggio;

public enum AspettoAvversario {

    MOSTRO("👾", 14, 22, "NEMICO: %s\nCreatura guardiana della stanza."),
    BOSS("☠ BOSS", 14, 32, "BOSS: %s\nAvversario supremo del piano! Sconfiggilo per salire le scale."),
    BOSS_FINALE("👑 FINALE", 14, 42, "BOSS FINALE: %s\nSconfiggilo per conquistare la torre!");

    private final String icona;
    private final int dimensioneIcona;
    private final int altezzaIcona;
    private final String modelloDescrizione;

    AspettoAvversario(String icona, int dimensioneIcona, int altezzaIcona, String modelloDescrizione) {
        this.icona = icona;
        this.dimensioneIcona = dimensioneIcona;
        this.altezzaIcona = altezzaIcona;
        this.modelloDescrizione = modelloDescrizione;
    }

    public static AspettoAvversario di(Combattente avversario) {
        if (!avversario.isBoss()) {
            return MOSTRO;
        }
        return avversario.isFinale() ? BOSS_FINALE : BOSS;
    }

    public SpritePersonaggio personaggioDelPiano(int numeroPiano) {
        return switch (this) {
            case MOSTRO -> SpritePersonaggio.mostroDelPiano(numeroPiano);
            case BOSS -> SpritePersonaggio.bossDelPiano(numeroPiano);
            case BOSS_FINALE -> SpritePersonaggio.DEMONE_GIGANTE;
        };
    }

    public String getIcona() {
        return this.icona;
    }

    public int getDimensioneIcona() {
        return this.dimensioneIcona;
    }

    public int getAltezzaIcona() {
        return this.altezzaIcona;
    }

    public String descrivi(String nomeAvversario) {
        return this.modelloDescrizione.formatted(nomeAvversario);
    }
}