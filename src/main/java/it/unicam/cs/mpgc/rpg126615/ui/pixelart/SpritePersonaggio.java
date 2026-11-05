package it.unicam.cs.mpgc.rpg126615.ui.pixelart;

import java.util.List;

public enum SpritePersonaggio {

    ZOMBIE_PICCOLO("tiny_zombie_idle_anim", 4),
    GOBLIN("goblin_idle_anim", 4),
    DIAVOLETTO("imp_idle_anim", 4),
    SCHELETRO("skelet_idle_anim", 4),
    MELMOSO("muddy_anim", 4),
    PALUDOSO("swampy_anim", 4),
    LUMACA("slug_anim", 4),
    ZOMBIE_DI_GHIACCIO("ice_zombie_anim", 4),
    WOGOL("wogol_idle_anim", 4),
    CHORT("chort_idle_anim", 4),
    ORCO_MASCHERATO("masked_orc_idle_anim", 4),
    UOMO_ZUCCA("pumpkin_dude_idle_anim", 4),
    ORCO_GUERRIERO("orc_warrior_idle_anim", 4),
    ORCO_SCIAMANO("orc_shaman_idle_anim", 4),
    NECROMANTE("necromancer_anim", 4),

    OGRE("ogre_idle_anim", 3),
    ZOMBIE_GIGANTE("big_zombie_idle_anim", 3),

    DEMONE_GIGANTE("big_demon_idle_anim", 3);

    private static final List<SpritePersonaggio> MOSTRI = List.of(
            ZOMBIE_PICCOLO, GOBLIN, DIAVOLETTO, SCHELETRO, MELMOSO, PALUDOSO, LUMACA, ZOMBIE_DI_GHIACCIO,
            WOGOL, CHORT, ORCO_MASCHERATO, UOMO_ZUCCA, ORCO_GUERRIERO, ORCO_SCIAMANO, NECROMANTE);
    private static final List<SpritePersonaggio> BOSS = List.of(OGRE, ZOMBIE_GIGANTE);

    private final String prefisso;
    private final int scala;

    SpritePersonaggio(String prefisso, int scala) {
        this.prefisso = prefisso;
        this.scala = scala;
    }

    public static SpritePersonaggio mostroDelPiano(int numeroPiano) {
        return MOSTRI.get((numeroPiano - 1) % MOSTRI.size());
    }

    public static SpritePersonaggio bossDelPiano(int numeroPiano) {
        return BOSS.get((numeroPiano - 1) % BOSS.size());
    }

    public String getPrefisso() {
        return this.prefisso;
    }

    public int getScala() {
        return this.scala;
    }
}