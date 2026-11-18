package it.unicam.cs.mpgc.rpg126615.model.battaglia;

public record EsitoAttacco(int potenza, int difesa, int danno) {

    public EsitoAttacco {
        if (potenza < 0 || difesa < 0 || danno < 0) {
            throw new IllegalArgumentException("Potenza, difesa e danno non possono essere negativi: "
                    + potenza + ", " + difesa + ", " + danno);
        }
    }
}