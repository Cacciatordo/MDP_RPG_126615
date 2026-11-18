package it.unicam.cs.mpgc.rpg126615.model.battaglia;

import it.unicam.cs.mpgc.rpg126615.model.carta.Carta;
import it.unicam.cs.mpgc.rpg126615.model.carta.Mano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class CronacaBattaglia {

    private static final String INDENTAZIONE = "   ";

    private CronacaBattaglia() {
    }

    static String attaccoGiaEseguito() {
        return "Hai già sferrato un attacco base in questo turno!";
    }

    static String attaccoSenzaPuntiAzione() {
        return "Punti Azione insufficienti per attaccare!";
    }

    static String attaccoDelGiocatore(EsitoAttacco esito) {
        return "Infliggi " + esito.danno() + " danni! (Attacco " + esito.potenza()
                + " - Armatura nemica " + esito.difesa() + ")";
    }

    static String attaccoDelNemico(String nomeNemico, EsitoAttacco esito) {
        if (esito.danno() == 0) {
            return nomeNemico + " attacca (Attacco " + esito.potenza() + "), ma il tuo scudo ("
                    + esito.difesa() + ") assorbe tutto: subisci 0 danni.";
        }
        return nomeNemico + " ti infligge " + esito.danno() + " danni! (Attacco " + esito.potenza()
                + " - Tuo scudo " + esito.difesa() + ")";
    }

    static String cartaNonValida() {
        return "Indice della carta non valido!";
    }

    static String manoPiena(Carta carta) {
        return "Mano piena (" + Mano.CAPIENZA_MASSIMA + " carte)! Non puoi usare " + carta.getNome() + ".";
    }

    static String cartaSenzaPuntiAzione(Carta carta) {
        return "Punti Azione insufficienti per giocare: " + carta.getNome();
    }

    static String cartaUsata(Carta carta, StatisticheInGioco prima, StatisticheInGioco dopo) {
        List<String> righe = new ArrayList<>();
        righe.add("Carta usata: " + carta.getNome());
        righe.addAll(variazioni(prima, dopo));
        return String.join("\n", righe);
    }

    static String cartePescate(int quantita) {
        return INDENTAZIONE + "Carte pescate: " + quantita;
    }

    static String effettiSvaniti(StatisticheInGioco prima, StatisticheInGioco dopo) {
        List<String> righe = new ArrayList<>();
        righe.add("Nuovo round: gli effetti delle carte svaniscono.");
        righe.addAll(variazioni(prima, dopo));
        return String.join("\n", righe);
    }

    static String curaAnnullata(Carta carta, int pianiRimanenti) {
        return "Hai giocato " + carta.getNome() + ", ma la ferita della trappola ("
                + pianiRimanenti + " piani rimanenti) annulla la cura! Carta scartata.";
    }

    static String curaRiuscita(Carta carta, int hpRecuperati) {
        return "Hai usato " + carta.getNome() + ": recuperi +" + hpRecuperati + " HP!";
    }

    static String vittoria(int esperienza) {
        return "Nemico sconfitto! Hai ottenuto " + esperienza + " punti esperienza!";
    }

    static String sconfitta() {
        return "Sei caduto in battaglia...";
    }

    private static List<String> variazioni(StatisticheInGioco prima, StatisticheInGioco dopo) {
        List<String> righe = new ArrayList<>();
        variazione("Il tuo attacco", prima.attaccoGiocatore(), dopo.attaccoGiocatore()).ifPresent(righe::add);
        variazione("Il tuo scudo", prima.scudoGiocatore(), dopo.scudoGiocatore()).ifPresent(righe::add);
        variazione("Attacco nemico", prima.attaccoNemico(), dopo.attaccoNemico()).ifPresent(righe::add);
        variazione("Armatura nemica", prima.difesaNemico(), dopo.difesaNemico()).ifPresent(righe::add);
        return righe;
    }

    private static Optional<String> variazione(String statistica, int prima, int dopo) {
        if (prima == dopo) {
            return Optional.empty();
        }
        int differenza = dopo - prima;
        String segno = differenza > 0 ? "+" : "";
        return Optional.of(INDENTAZIONE + statistica + ": " + prima + " → " + dopo
                + " (" + segno + differenza + ")");
    }
}