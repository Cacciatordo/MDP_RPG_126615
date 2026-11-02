package it.unicam.cs.mpgc.rpg126615.model.entita;

public abstract class EntitaAstratta implements Combattente {

    private static final int DANNO_MINIMO_ATTACCO = 1;

    private final String nome;
    private final int attaccoBase;
    private final int hpMax;
    private int hp;
    private int modificatoreAttacco;
    private int difesa;

    protected EntitaAstratta(String nome, int hpMax, int attaccoBase) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto o nullo");
        }
        if (hpMax <= 0) {
            throw new IllegalArgumentException("Gli HP massimi devono essere positivi");
        }
        if (attaccoBase <= 0) {
            throw new IllegalArgumentException("L'attacco base deve essere positivo");
        }
        this.nome = nome;
        this.hpMax = hpMax;
        this.attaccoBase = attaccoBase;
        this.hp = hpMax;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getHpMax() {
        return hpMax;
    }

    @Override
    public int getAttaccoBase() {
        return attaccoBase;
    }

    @Override
    public int getAttacco() {
        return Math.max(0, attaccoBase + modificatoreAttacco);
    }

    @Override
    public int getDifesa() {
        return difesa;
    }

    @Override
    public void modificaAttacco(int variazione) {
        modificatoreAttacco += variazione;
    }

    @Override
    public void modificaDifesa(int variazione) {
        difesa = Math.max(0, difesa + variazione);
    }

    @Override
    public void azzeraModificatori() {
        modificatoreAttacco = 0;
        difesa = 0;
    }

    @Override
    public int attacca(Combattente bersaglio) {
        int danno = Math.max(DANNO_MINIMO_ATTACCO, getAttacco() - bersaglio.getDifesa());
        bersaglio.subisciDanno(danno);
        return danno;
    }

    @Override
    public void subisciDanno(int danno) {
        if (danno < 0) {
            throw new IllegalArgumentException("Il danno non può essere negativo");
        }
        hp = Math.max(0, hp - danno);
    }

    @Override
    public void cura(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La cura non può essere negativa");
        }
        hp = Math.min(hpMax, hp + quantita);
    }

    @Override
    public boolean isVivo() {
        return hp > 0;
    }
}