package it.unicam.cs.mpgc.rpg126615.ui.view.corridoio;

public record RichiestaConferma(String titolo, String testo, String etichettaConferma, String etichettaAnnulla) {

    private static final String CONFERMA_PREDEFINITA = "Conferma";
    private static final String ANNULLA_PREDEFINITA = "Annulla";

    public static RichiestaConferma standard(String titolo, String testo) {
        return new RichiestaConferma(titolo, testo, CONFERMA_PREDEFINITA, ANNULLA_PREDEFINITA);
    }
}