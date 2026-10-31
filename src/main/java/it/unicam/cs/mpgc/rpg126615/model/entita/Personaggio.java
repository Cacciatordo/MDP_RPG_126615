package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Personaggio {

    private static final int DANNO_MINIMO_ATTACCO = 1;

    private final String nome;
    private final int attaccoBase;
    private final int hpMax;
    private int hp;
    private int modificatoreAttacco;
    private int difesa;

    public Personaggio(String nome, int hpMax, int attaccoBase) {
        this.nome = nome;
        this.hpMax = hpMax;
        this.attaccoBase = attaccoBase;
        this.hp = hpMax;
    }

    public String getNome() {
        return nome;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getAttaccoBase() {
        return attaccoBase;
    }

    public int getAttacco() {
        return Math.max(0, attaccoBase + modificatoreAttacco);
    }

    public int getDifesa() {
        return difesa;
    }

    public void modificaAttacco(int variazione) {
        modificatoreAttacco += variazione;
    }

    public void modificaDifesa(int variazione) {
        difesa = Math.max(0, difesa + variazione);
    }

    public void azzeraModificatori() {
        modificatoreAttacco = 0;
        difesa = 0;
    }

    public int attacca(Personaggio bersaglio) {
        int danno = Math.max(DANNO_MINIMO_ATTACCO, getAttacco() - bersaglio.getDifesa());
        bersaglio.subisciDanno(danno);
        return danno;
    }

    public void subisciDanno(int danno) {
        hp = Math.max(0, hp - danno);
    }

    public void cura(int quantita) {
        hp = Math.min(hpMax, hp + quantita);
    }

    public boolean isVivo() {
        return hp > 0;
    }
}