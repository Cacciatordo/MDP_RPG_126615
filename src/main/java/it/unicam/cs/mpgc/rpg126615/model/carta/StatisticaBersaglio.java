package it.unicam.cs.mpgc.rpg126615.model.carta;

public enum StatisticaBersaglio {
    ATTACCO(TipoCarta.POTENZIAMENTO_ATTACCO, TipoCarta.INDEBOLIMENTO_ATTACCO) {
        @Override
        public void modifica(StatisticheModificabili bersaglio, int variazione) {
            bersaglio.modificaAttacco(variazione);
        }
    },
    SCUDO(TipoCarta.POTENZIAMENTO_DIFESA, TipoCarta.INDEBOLIMENTO_DIFESA) {
        @Override
        public void modifica(StatisticheModificabili bersaglio, int variazione) {
            bersaglio.modificaDifesa(variazione);
        }
    };

    private final TipoCarta tipoPotenziamento;
    private final TipoCarta tipoIndebolimento;

    StatisticaBersaglio(TipoCarta tipoPotenziamento, TipoCarta tipoIndebolimento) {
        this.tipoPotenziamento = tipoPotenziamento;
        this.tipoIndebolimento = tipoIndebolimento;
    }

    public abstract void modifica(StatisticheModificabili bersaglio, int variazione);

    public TipoCarta getTipoPotenziamento() {
        return tipoPotenziamento;
    }

    public TipoCarta getTipoIndebolimento() {
        return tipoIndebolimento;
    }
}