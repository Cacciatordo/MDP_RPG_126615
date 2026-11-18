package it.unicam.cs.mpgc.rpg126615.model.entita;

public class BossFinale extends Boss {

    private static final String NOME = "Boss della Torre";
    private static final double MOLTIPLICATORE_HP = 1.35;
    private static final double MOLTIPLICATORE_ATTACCO = 1.08;
    private static final double MOLTIPLICATORE_ARMATURA = 1.06;

    public BossFinale(int piano) {
        this(new Boss(piano));
    }

    private BossFinale(Boss riferimento) {
        super(NOME,
                potenzia(riferimento.getHpMax(), MOLTIPLICATORE_HP),
                potenzia(riferimento.getAttaccoBase(), MOLTIPLICATORE_ATTACCO),
                potenzia(riferimento.getDifesa(), MOLTIPLICATORE_ARMATURA),
                riferimento.getEsperienzaData());
    }

    private static int potenzia(int valore, double moltiplicatore) {
        return (int) Math.round(valore * moltiplicatore);
    }

    @Override
    public boolean isFinale() {
        return true;
    }
}