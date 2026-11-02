package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public abstract class CartaAstratta implements Carta {

    public static final String SEGNAPOSTO_VALORE = "{valore}";

    protected static final int COSTO_PA_STANDARD = 1;

    private final String nome;
    private final String descrizione;
    private final int costoPA;
    private final int valoreBase;
    private final Livellabile riferimentoLivello;

    protected CartaAstratta(String nome, String descrizione, int costoPA, int valoreBase,
                            Livellabile riferimentoLivello) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della carta non può essere nullo o vuoto");
        }
        if (descrizione == null || descrizione.isBlank()) {
            throw new IllegalArgumentException("La descrizione non può essere nulla o vuota");
        }
        if (costoPA <= 0) {
            throw new IllegalArgumentException("Il costo in PA deve essere positivo: " + costoPA);
        }
        if (valoreBase < 0) {
            throw new IllegalArgumentException("Il valore base non può essere negativo: " + valoreBase);
        }
        if (riferimentoLivello == null) {
            throw new IllegalArgumentException("Il riferimento al livello non può essere nullo");
        }
        this.nome = nome;
        this.descrizione = descrizione;
        this.costoPA = costoPA;
        this.valoreBase = valoreBase;
        this.riferimentoLivello = riferimentoLivello;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione.replace(SEGNAPOSTO_VALORE, String.valueOf(getValoreEffettivo()));
    }

    @Override
    public int getCostoPA() {
        return costoPA;
    }

    public int getValoreEffettivo() {
        return valoreBase * getLivelloRiferimento();
    }

    @Override
    public final void applica(ContestoCarta contesto) {
        if (contesto == null) {
            throw new IllegalArgumentException("Il contesto della carta non può essere nullo");
        }
        produciEffetto(contesto);
    }

    protected abstract void produciEffetto(ContestoCarta contesto);

    protected int getValoreBase() {
        return valoreBase;
    }

    protected int getLivelloRiferimento() {
        return riferimentoLivello.getLivello();
    }
}