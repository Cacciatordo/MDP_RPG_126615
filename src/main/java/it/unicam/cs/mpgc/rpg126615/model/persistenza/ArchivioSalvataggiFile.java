package it.unicam.cs.mpgc.rpg126615.model.persistenza;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ArchivioSalvataggiFile implements ArchivioSalvataggi {

    private static final String ESTENSIONE = ".json";
    private static final Pattern NOME_VALIDO = Pattern.compile("[a-zA-Z0-9_-]+");

    private final Path cartella;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ArchivioSalvataggiFile(Path cartella) {
        if (cartella == null) {
            throw new IllegalArgumentException("La cartella dei salvataggi non può essere nulla");
        }
        this.cartella = cartella;
    }

    @Override
    public List<String> elencaNomi() throws ErrorePersistenzaException {
        List<String> nomi = new ArrayList<>();
        File[] contenuto = cartella.toFile().listFiles();
        if (contenuto == null) {
            return nomi;
        }
        for (File file : contenuto) {
            String nomeFile = file.getName();
            if (nomeFile.endsWith(ESTENSIONE)) {
                String nome = nomeFile.substring(0, nomeFile.length() - ESTENSIONE.length());
                if (isNomeValido(nome)) {
                    nomi.add(nome);
                }
            }
        }
        Collections.sort(nomi);
        return nomi;
    }

    @Override
    public boolean esiste(String nome) {
        return Files.exists(percorso(nome));
    }

    @Override
    public void salva(String nome, DatiSalvataggio dati) throws ErrorePersistenzaException {
        if (dati == null) {
            throw new IllegalArgumentException("I dati di salvataggio non possono essere nulli");
        }
        Path file = percorso(nome);
        try {
            Files.createDirectories(cartella);
            try (Writer scrittore = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
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
        } catch (RuntimeException e) {
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
        if (!isNomeValido(nome)) {
            throw new IllegalArgumentException("Nome di salvataggio non valido: " + nome);
        }
        return cartella.resolve(nome + ESTENSIONE);
    }

    private static boolean isNomeValido(String nome) {
        return nome != null && NOME_VALIDO.matcher(nome).matches();
    }
}