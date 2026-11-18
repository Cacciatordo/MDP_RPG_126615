package it.unicam.cs.mpgc.rpg126615.model.entita;

public abstract class EntitaAstratta implements Combattente {

    private static final int DANNO_MINIMO_ATTACCO = 1;

    private final String nome;
    private final int attaccoBase;
    private final int difesaBase;
    private int hpMax;
    private int hp;
    private int modificatoreAttacco;
    private int modificatoreDifesa;

    protected EntitaAstratta(String nome, int hpMax, int attaccoBase) {
        this(nome, hpMax, attaccoBase, 0);
    }

    protected EntitaAstratta(String nome, int hpMax, int attaccoBase, int difesaBase) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto o nullo");
        }
        if (hpMax <= 0) {
            throw new IllegalArgumentException("Gli HP massimi devono essere positivi");
        }
        if (attaccoBase <= 0) {
            throw new IllegalArgumentException("L'attacco base deve essere positivo");
        }
        if (difesaBase < 0) {
            throw new IllegalArgumentException("La difesa base non può essere negativa");
        }
        this.nome = nome;
        this.hpMax = hpMax;
        this.attaccoBase = attaccoBase;
        this.difesaBase = difesaBase;
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
        return Math.max(0, difesaBase + modificatoreDifesa);
    }

    @Override
    public void modificaAttacco(int variazione) {
        modificatoreAttacco += variazione;
    }

    @Override
    public void modificaDifesa(int variazione) {
        modificatoreDifesa += variazione;
    }

    @Override
    public void azzeraModificatori() {
        modificatoreAttacco = 0;
        modificatoreDifesa = 0;
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

    protected void ripristinaHp(int punti, int massimo) {
        if (massimo <= 0) {
            throw new IllegalArgumentException("Gli HP massimi devono essere positivi");
        }
        hpMax = massimo;
        hp = Math.max(0, Math.min(punti, massimo));
    }
}