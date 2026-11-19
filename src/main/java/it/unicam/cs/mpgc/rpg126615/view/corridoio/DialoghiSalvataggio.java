package it.unicam.cs.mpgc.rpg126615.view.corridoio;

import it.unicam.cs.mpgc.rpg126615.model.gioco.Partita;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.ArchivioSalvataggi;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg126615.model.persistenza.ErrorePersistenzaException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class DialoghiSalvataggio {

    private static final String CARATTERI_NON_AMMESSI = "[^a-zA-Z0-9_-]";
    private static final double ALTEZZA_ELENCO = 180;
    private static final double SPAZIATURA = 8;

    private final ArchivioSalvataggi archivio;

    public DialoghiSalvataggio(ArchivioSalvataggi archivio) {
        if (archivio == null) {
            throw new IllegalArgumentException("L'archivio dei salvataggi non può essere nullo");
        }
        this.archivio = archivio;
    }

    public Optional<String> salva(Partita partita) {
        try {
            List<String> esistenti = this.archivio.elencaNomi();
            String predefinito = sanitizza(partita.getGiocatore().getNome() + "_Piano"
                    + partita.getNumeroPianoCorrente());
            Optional<String> inserito = chiediNome(predefinito, esistenti);
            if (inserito.isEmpty()) {
                return Optional.empty();
            }
            String nome = sanitizza(inserito.get());
            if (nome.isBlank()) {
                nome = predefinito;
            }
            if (esistenti.contains(nome) && !confermaSovrascrittura(nome)) {
                return Optional.empty();
            }
            this.archivio.salva(nome, partita.creaDatiSalvataggio());
            return Optional.of("✔ Partita salvata come '" + nome + "' al Piano "
                    + partita.getNumeroPianoCorrente() + "!");
        } catch (ErrorePersistenzaException errore) {
            return Optional.of("✖ Errore durante il salvataggio: " + errore.getMessage());
        }
    }

    public Optional<DatiSalvataggio> scegliDaCaricare() {
        try {
            List<String> nomi = this.archivio.elencaNomi();
            if (nomi.isEmpty()) {
                mostraAvviso("Nessun salvataggio trovato. Salva prima una partita.");
                return Optional.empty();
            }
            Optional<String> scelto = chiediSalvataggio(nomi);
            if (scelto.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(this.archivio.carica(scelto.get()));
        } catch (ErrorePersistenzaException errore) {
            mostraAvviso("Impossibile caricare: " + errore.getMessage());
            return Optional.empty();
        }
    }

    private Optional<String> chiediNome(String predefinito, List<String> esistenti) {
        Dialog<String> dialogo = new Dialog<>();
        dialogo.setTitle("Salva Partita");
        dialogo.setHeaderText(esistenti.isEmpty()
                ? "Scegli il nome del salvataggio."
                : "Scrivi un nome nuovo oppure seleziona un salvataggio da sovrascrivere.");

        TextField campoNome = new TextField(predefinito);
        ButtonType salva = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(salva, ButtonType.CANCEL);
        dialogo.getDialogPane().setContent(creaContenutoSalvataggio(campoNome, esistenti));
        dialogo.setResultConverter(pulsante -> pulsante == salva ? campoNome.getText() : null);
        return dialogo.showAndWait();
    }

    private VBox creaContenutoSalvataggio(TextField campoNome, List<String> esistenti) {
        VBox contenuto = new VBox(SPAZIATURA, new Label("Nome:"), campoNome);
        if (!esistenti.isEmpty()) {
            contenuto.getChildren().addAll(new Label("Salvataggi esistenti:"),
                    creaElencoEsistenti(campoNome, esistenti));
        }
        return contenuto;
    }

    private ListView<String> creaElencoEsistenti(TextField campoNome, List<String> esistenti) {
        ListView<String> elenco = new ListView<>();
        elenco.getItems().addAll(esistenti);
        elenco.setPrefHeight(ALTEZZA_ELENCO);
        elenco.getSelectionModel().selectedItemProperty().addListener((osservato, vecchio, nuovo) -> {
            if (nuovo != null) {
                campoNome.setText(nuovo);
            }
        });
        return elenco;
    }

    private boolean confermaSovrascrittura(String nome) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Sovrascrivi");
        conferma.setHeaderText("Il salvataggio '" + nome + "' esiste già.");
        conferma.setContentText("Vuoi sovrascriverlo?");
        Optional<ButtonType> risposta = conferma.showAndWait();
        return risposta.isPresent() && risposta.get() == ButtonType.OK;
    }

    private Optional<String> chiediSalvataggio(List<String> nomi) {
        Dialog<String> dialogo = new Dialog<>();
        dialogo.setTitle("Carica Partita");
        dialogo.setHeaderText("Seleziona il salvataggio da caricare:");

        ListView<String> elenco = new ListView<>();
        elenco.getItems().addAll(nomi);
        elenco.setPrefHeight(ALTEZZA_ELENCO);
        elenco.getSelectionModel().selectFirst();

        ButtonType carica = new ButtonType("Carica", ButtonBar.ButtonData.OK_DONE);
        ButtonType elimina = new ButtonType("Elimina", ButtonBar.ButtonData.OTHER);
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogo.getDialogPane().getButtonTypes().addAll(carica, elimina, annulla);
        dialogo.getDialogPane().setContent(elenco);

        dialogo.setResultConverter(pulsante -> {
            String selezionato = elenco.getSelectionModel().getSelectedItem();
            if (pulsante == carica) {
                return selezionato;
            }
            if (pulsante == elimina && selezionato != null) {
                elimina(selezionato);
            }
            return null;
        });
        return dialogo.showAndWait();
    }

    private void elimina(String nome) {
        try {
            this.archivio.elimina(nome);
        } catch (ErrorePersistenzaException errore) {
            mostraAvviso("Impossibile eliminare: " + errore.getMessage());
        }
    }

    private void mostraAvviso(String testo) {
        Alert avviso = new Alert(Alert.AlertType.INFORMATION);
        avviso.setTitle("Salvataggi");
        avviso.setHeaderText(null);
        avviso.setContentText(testo);
        avviso.showAndWait();
    }

    private static String sanitizza(String nome) {
        return nome.trim().replaceAll(CARATTERI_NON_AMMESSI, "_");
    }
}