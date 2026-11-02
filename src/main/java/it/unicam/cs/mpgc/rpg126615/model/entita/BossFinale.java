package it.unicam.cs.mpgc.rpg126615.model.entita;

public class BossFinale extends Boss {

    private static final String NOME = "Signore della Torre";
    private static final int HP = 300;
    private static final int ATTACCO = 30;
    private static final int ESPERIENZA = 300;

    public BossFinale() {
        super(NOME, HP, ATTACCO, ESPERIENZA);
    }

    @Override
    public boolean isFinale() {
        return true;
    }
}