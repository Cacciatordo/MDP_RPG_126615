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

    public StatoGiocatore {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome del giocatore non può essere nullo o vuoto");
        }
        if (livello <= 0) {
            throw new IllegalArgumentException("Il livello deve essere positivo: " + livello);
        }
        controllaNonNegativo("L'esperienza", esperienza);
        controllaNonNegativo("Gli HP", hp);
        controllaNonNegativo("Il bonus attacco permanente", bonusAttaccoPermanente);
        controllaNonNegativo("La difesa permanente", difesaPermanente);
        if (hpMax <= 0) {
            throw new IllegalArgumentException("Gli HP massimi devono essere positivi: " + hpMax);
        }
        if (hp > hpMax) {
            throw new IllegalArgumentException("Gli HP (" + hp + ") superano il massimo (" + hpMax + ")");
        }
    }

    private static void controllaNonNegativo(String descrizione, int valore) {
        if (valore < 0) {
            throw new IllegalArgumentException(descrizione + " non può essere negativo: " + valore);
        }
    }
}