package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Boss extends Nemico {

    private static final String PREFISSO_NOME = "Boss del Piano ";

    public Boss(int piano) {
        this(PREFISSO_NOME + validaPiano(piano),
                60 + piano * 20,
                8 + piano * 2,
                60 + piano * 10);
    }

    protected Boss(String nome, int hpMax, int attaccoBase, int esperienzaData) {
        super(nome, hpMax, attaccoBase, esperienzaData);
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}