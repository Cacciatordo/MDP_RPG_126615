package it.unicam.cs.mpgc.rpg126615.model.gioco;

import it.unicam.cs.mpgc.rpg126615.model.battaglia.Battaglia;
import it.unicam.cs.mpgc.rpg126615.model.carta.CreatoreMazzo;
import it.unicam.cs.mpgc.rpg126615.model.carta.FabbricaCarte;
import it.unicam.cs.mpgc.rpg126615.model.entita.Giocatore;
import it.unicam.cs.mpgc.rpg126615.model.entita.Nemico;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.MappaturaPiani;
import it.unicam.cs.mpgc.rpg126615.model.torre.GeneratorePiani;
import it.unicam.cs.mpgc.rpg126615.model.torre.PianoTorre;
import it.unicam.cs.mpgc.rpg126615.model.torre.TipoPiano;
import it.unicam.cs.mpgc.rpg126615.model.torre.Torre;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Partita {

    public static final int PIANI_MINIMI = 10;
    public static final int PIANI_MASSIMI = 999;
    public static final int PIANI_PREDEFINITI = 50;

    private static final String NOME_PREDEFINITO = "Eroe";
    private static final int HP_INIZIALI_GIOCATORE = 100;
    private static final int LIVELLI_EXTRA_SENZA_CURE = 4;
    private static final int ATTACCO_INIZIALE_GIOCATORE = 10;

    private final Giocatore giocatore;
    private final Torre torre;
    private int numeroPianoCorrente;
    private int pianoMassimoRaggiunto;
    private final long semeTorre;
    private Battaglia battaglia;
    private boolean riaffrontoInCorso;

    public Partita(Giocatore giocatore, Torre torre, long semeTorre) {
        if (giocatore == null || torre == null) {
            throw new IllegalArgumentException("Giocatore e torre non possono essere nulli");
        }
        this.giocatore = giocatore;
        this.torre = torre;
        this.semeTorre = semeTorre;
        this.numeroPianoCorrente = 1;
        this.pianoMassimoRaggiunto = 1;
        preparaPianoCorrente();
    }

    public static boolean isNumeroPianiValido(int totalePiani) {
        return totalePiani >= PIANI_MINIMI && totalePiani <= PIANI_MASSIMI;
    }

    public static Partita nuova(String nomeEroe, int totalePiani) {
        return nuova(nomeEroe, totalePiani, ThreadLocalRandom.current().nextLong());
    }

    public static Partita nuova(String nomeEroe, int totalePiani, long semeTorre) {
        return nuova(nomeEroe, totalePiani, semeTorre, new FabbricaCarte());
    }

    public static Partita nuova(String nomeEroe, int totalePiani, long semeTorre, CreatoreMazzo creatoreMazzo) {
        if (!isNumeroPianiValido(totalePiani)) {
            throw new IllegalArgumentException("Il numero di piani deve essere tra " + PIANI_MINIMI
                    + " e " + PIANI_MASSIMI + ": " + totalePiani);
        }
        String nome = (nomeEroe == null || nomeEroe.isBlank()) ? NOME_PREDEFINITO : nomeEroe.trim();
        Giocatore nuovoGiocatore = new Giocatore(nome, HP_INIZIALI_GIOCATORE, ATTACCO_INIZIALE_GIOCATORE,
                creatoreMazzo);
        Torre nuovaTorre = new Torre(new GeneratorePiani(new Random(semeTorre)).generaPiani(totalePiani));
        return new Partita(nuovoGiocatore, nuovaTorre, semeTorre);
    }

    public static Partita daSalvataggio(DatiSalvataggio dati) {
        Partita partita = nuova(dati.giocatore().nome(), dati.piani().size(), dati.semeTorre());
        partita.ripristina(dati);
        return partita;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public int getTotalePiani() {
        return torre.getNumeroPiani();
    }

    public int getNumeroPianoCorrente() {
        return numeroPianoCorrente;
    }

    public PianoTorre getPianoCorrente() {
        return torre.getPiano(numeroPianoCorrente);
    }

    public Optional<Battaglia> getBattaglia() {
        return Optional.ofNullable(battaglia);
    }

    public int getLivelloConsigliato() {
        int livello = Torre.getLivelloConsigliato(numeroPianoCorrente);
        return giocatore.isGuarigioneBloccata() ? livello + LIVELLI_EXTRA_SENZA_CURE : livello;
    }

    public boolean haProssimoPiano() {
        return numeroPianoCorrente < torre.getNumeroPiani();
    }

    public boolean haPianoPrecedente() {
        return numeroPianoCorrente > 1;
    }

    public boolean isTorreCompletata() {
        return isPianoSuperato() && !haProssimoPiano();
    }

    public boolean isPianoSuperato() {
        return battaglia != null ? battaglia.isVinta() : getPianoCorrente().isCompletato();
    }

    public boolean isPassaggioLibero() {
        return isPianoSuperato() || getPianoCorrente().getTipo() == TipoPiano.RIPOSO;
    }

    public String entraNelPianoCorrente() {
        return getPianoCorrente().entra(giocatore);
    }

    public boolean avanzaAlProssimoPiano() {
        if (!haProssimoPiano()) {
            return false;
        }
        numeroPianoCorrente++;
        if (numeroPianoCorrente > pianoMassimoRaggiunto) {
            pianoMassimoRaggiunto = numeroPianoCorrente;
            giocatore.avanzaPiano();
        }
        preparaPianoCorrente();
        return true;
    }

    public boolean tornaAlPianoPrecedente() {
        if (!haPianoPrecedente()) {
            return false;
        }
        numeroPianoCorrente--;
        preparaPianoCorrente();
        return true;
    }

    public void riaffrontaAvversario() {
        PianoTorre piano = getPianoCorrente();
        if (!piano.haNemico()) {
            throw new IllegalStateException("Il piano corrente non ha avversari");
        }
        Nemico nemico = piano.getNemico();
        nemico.cura(nemico.getHpMax());
        preparaPianoCorrente();
        riaffrontoInCorso = true;
    }

    public void ritiratiDopoSconfitta() {
        if (giocatore.isVivo()) {
            throw new IllegalStateException("Il giocatore è ancora vivo");
        }
        PianoTorre pianoDellaSconfitta = getPianoCorrente();
        if (pianoDellaSconfitta.haNemico()) {
            pianoDellaSconfitta.getNemico().impostaSconfitto(riaffrontoInCorso);
        }
        giocatore.recuperaDopoSconfitta();
        numeroPianoCorrente = trovaPianoDiRipartenza();
        preparaPianoCorrente();
    }

    public DatiSalvataggio creaDatiSalvataggio() {
        return new DatiSalvataggio(numeroPianoCorrente, pianoMassimoRaggiunto, semeTorre,
                giocatore.getStato(), MappaturaPiani.creaDati(torre));
    }

    public void ripristina(DatiSalvataggio dati) {
        if (!MappaturaPiani.corrisponde(torre, dati.piani())) {
            throw new IllegalArgumentException("Il salvataggio non corrisponde alla torre attuale");
        }
        giocatore.applicaStato(dati.giocatore());
        MappaturaPiani.ripristina(torre, dati.piani());
        numeroPianoCorrente = dati.pianoCorrente();
        pianoMassimoRaggiunto = dati.pianoMassimoRaggiunto();
        preparaPianoCorrente();
    }

    private int trovaPianoDiRipartenza() {
        for (int numero = numeroPianoCorrente - 1; numero >= 1; numero--) {
            if (torre.getPiano(numero).getTipo() == TipoPiano.RIPOSO) {
                return numero;
            }
        }
        return 1;
    }

    private void preparaPianoCorrente() {
        riaffrontoInCorso = false;
        PianoTorre piano = getPianoCorrente();
        battaglia = piano.haNemico() ? new Battaglia(giocatore, piano.getNemico()) : null;
    }
}