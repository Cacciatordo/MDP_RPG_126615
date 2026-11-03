package it.unicam.cs.mpgc.rpg126615.persistenza;

public class ErrorePersistenzaException extends Exception {

    public ErrorePersistenzaException(String messaggio) {
        super(messaggio);
    }

    public ErrorePersistenzaException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}