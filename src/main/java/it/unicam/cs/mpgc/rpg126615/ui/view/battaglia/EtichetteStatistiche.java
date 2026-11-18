package it.unicam.cs.mpgc.rpg126615.ui.view.battaglia;

public enum EtichetteStatistiche {

    EROE("Attacco", "Scudo"),
    AVVERSARIO("Potenza", "Armatura");

    private final String attacco;
    private final String difesa;

    EtichetteStatistiche(String attacco, String difesa) {
        this.attacco = attacco;
        this.difesa = difesa;
    }

    public String getAttacco() {
        return attacco;
    }

    public String getDifesa() {
        return difesa;
    }
}