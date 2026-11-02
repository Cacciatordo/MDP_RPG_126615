package it.unicam.cs.mpgc.rpg126615.model.carta;

import it.unicam.cs.mpgc.rpg126615.model.progressione.Livellabile;

public abstract class CartaStatistica extends CartaAstratta {

    private final StatisticaBersaglio bersaglio;

    protected CartaStatistica(String nome, String descrizione, int valoreBase,
                              Livellabile riferimentoLivello, StatisticaBersaglio bersaglio) {
        super(nome, descrizione, COSTO_PA_STANDARD, valoreBase, riferimentoLivello);
        if (bersaglio == null) {
            throw new IllegalArgumentException("La statistica bersaglio non può essere nulla");
        }
        this.bersaglio = bersaglio;
    }

    public StatisticaBersaglio getBersaglio() {
        return bersaglio;
    }
}