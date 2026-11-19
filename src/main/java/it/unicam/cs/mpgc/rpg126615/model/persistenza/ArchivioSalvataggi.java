package it.unicam.cs.mpgc.rpg126615.model.persistenza;

import java.util.List;

public interface ArchivioSalvataggi {

    List<String> elencaNomi() throws ErrorePersistenzaException;

    boolean esiste(String nome);

    void salva(String nome, DatiSalvataggio dati) throws ErrorePersistenzaException;

    DatiSalvataggio carica(String nome) throws ErrorePersistenzaException;

    void elimina(String nome) throws ErrorePersistenzaException;
}