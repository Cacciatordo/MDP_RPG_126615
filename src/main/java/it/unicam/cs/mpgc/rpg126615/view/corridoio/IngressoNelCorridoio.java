package it.unicam.cs.mpgc.rpg126615.view.corridoio;

public record IngressoNelCorridoio(ArrivoNelCorridoio arrivo, double posizioneEroe) {

    public IngressoNelCorridoio {
        if (arrivo == null) {
            throw new IllegalArgumentException("L'arrivo nel corridoio non può essere nullo");
        }
    }
}