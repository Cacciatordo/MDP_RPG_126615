package it.unicam.cs.mpgc.rpg126615.model.entita;

public record StatoGiocatore(
        String nome,
        int livello,
        int esperienza,
        int hp,
        int hpMax,
        int bonusAttaccoPermanente,
        int difesaPermanente
) {
}