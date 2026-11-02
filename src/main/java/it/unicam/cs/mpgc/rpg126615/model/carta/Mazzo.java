package it.unicam.cs.mpgc.rpg126615.model.carta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Mazzo {

    private final Deque<Carta> pilaPesca = new ArrayDeque<>();
    private final List<Carta> pilaScarti = new ArrayList<>();
    private final List<Carta> carteDelMazzo = new ArrayList<>();

    public Mazzo(Collection<? extends Carta> carte) {
        if (carte == null || carte.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Le carte del mazzo non possono essere nulle");
        }
        carteDelMazzo.addAll(carte);
        ricomponiPilaPesca();
    }

    public Optional<Carta> pesca() {
        if (pilaPesca.isEmpty()) {
            riciclaScarti();
        }
        return Optional.ofNullable(pilaPesca.pollFirst());
    }

    public void scarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException("La carta da scartare non può essere nulla");
        }
        pilaScarti.add(carta);
    }

    public void ripristinaMazzoCompleto(Mano mano) {
        if (mano != null) {
            mano.svuota();
        }
        ricomponiPilaPesca();
    }

    public List<Carta> getPilaScarti() {
        return Collections.unmodifiableList(pilaScarti);
    }

    public int getDimensionePesca() {
        return pilaPesca.size();
    }

    public int getDimensioneScarti() {
        return pilaScarti.size();
    }

    private void ricomponiPilaPesca() {
        pilaPesca.clear();
        pilaScarti.clear();
        List<Carta> tutte = new ArrayList<>(carteDelMazzo);
        Collections.shuffle(tutte);
        pilaPesca.addAll(tutte);
    }

    private void riciclaScarti() {
        if (pilaScarti.isEmpty()) {
            return;
        }
        List<Carta> daRimescolare = new ArrayList<>(pilaScarti);
        Collections.shuffle(daRimescolare);
        pilaPesca.addAll(daRimescolare);
        pilaScarti.clear();
    }
}