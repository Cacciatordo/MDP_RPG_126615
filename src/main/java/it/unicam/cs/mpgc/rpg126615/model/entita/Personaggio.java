package it.unicam.cs.mpgc.rpg126615.model.entita;

public class Personaggio {

    private final String nome;
    private final int attaccoBase;
    private final int hpMax;
    private int hp;

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