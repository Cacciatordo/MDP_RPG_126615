package it.unicam.cs.mpgc.rpg126615.persistenza;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ArchivioSalvataggiFile implements ArchivioSalvataggi {

    private static final String ESTENSIONE = ".json";

    private final Path cartella;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ArchivioSalvataggiFile(Path cartella) {
        this.cartella = cartella;
    }

    @Override
    public List<String> elencaNomi() throws ErrorePersistenzaException {
        if (!Files.isDirectory(cartella)) {
            return List.of();
        }
        try (Stream<Path> file = Files.list(cartella)) {
            return file.map(percorso -> percorso.getFileName().toString())
                    .filter(nome -> nome.endsWith(ESTENSIONE))
                    .map(nome -> nome.substring(0, nome.length() - ESTENSIONE.length()))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new ErrorePersistenzaException("Impossibile leggere la cartella dei salvataggi", e);
        }
    }

    @Override
    public boolean esiste(String nome) {
        return Files.exists(percorso(nome));
    }

    @Override
    public void salva(String nome, DatiSalvataggio dati) throws ErrorePersistenzaException {
        try {
            Files.createDirectories(cartella);
            try (Writer scrittore = Files.newBufferedWriter(percorso(nome), StandardCharsets.UTF_8)) {
                gson.toJson(dati, scrittore);
            }
        } catch (IOException e) {
            throw new ErrorePersistenzaException("Impossibile scrivere il salvataggio '" + nome + "'", e);
        }
    }

    @Override
    public DatiSalvataggio carica(String nome) throws ErrorePersistenzaException {
        Path file = percorso(nome);
        if (!Files.exists(file)) {
            throw new ErrorePersistenzaException("Salvataggio non trovato: " + nome);
        }
        try (Reader lettore = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            DatiSalvataggio dati = gson.fromJson(lettore, DatiSalvataggio.class);
            if (dati == null) {
                throw new ErrorePersistenzaException("Il salvataggio '" + nome + "' è vuoto");
            }
            return dati;
        } catch (IOException e) {
            throw new ErrorePersistenzaException("Impossibile leggere il salvataggio '" + nome + "'", e);
        } catch (JsonParseException e) {
            throw new ErrorePersistenzaException("Il salvataggio '" + nome + "' è danneggiato", e);
        }
    }

    @Override
    public void elimina(String nome) throws ErrorePersistenzaException {
        try {
            Files.deleteIfExists(percorso(nome));
        } catch (IOException e) {
            throw new ErrorePersistenzaException("Impossibile eliminare il salvataggio '" + nome + "'", e);
        }
    }

    private Path percorso(String nome) {
        return cartella.resolve(nome + ESTENSIONE);
    }
}