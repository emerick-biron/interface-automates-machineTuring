package machines.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.*;

public abstract class Machine<T extends Transition<T>> implements Runnable {
    private ObservableSet<Etat<T>> etats;
    private StatusExecution statusExecution;

    public Machine(Set<Etat<T>> etats) {
        this.etats = FXCollections.observableSet(etats);
        statusExecution = StatusExecution.PRETE;
    }

    public Machine() {
        etats = FXCollections.observableSet(new HashSet<>());
        statusExecution = StatusExecution.PRETE;
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;

    public abstract void run(String mot) throws InterruptedException;

    @Override
    public void run() {
        statusExecution = StatusExecution.EN_COURS;
        try {
            run("");
            statusExecution = StatusExecution.TERMINEE;
        } catch (InterruptedException e) {
            e.printStackTrace();
            statusExecution = StatusExecution.INTERROMPUE;
        }
    }

    public abstract void step(char lettre);

    public void ajouterEtat(Etat<T> etat) {
        etats.add(etat);
    }

    public void supprimerEtat(Etat<T> etat) {
        etats.remove(etat);
    }

    public Set<T> getTransitions() {
        Set<T> transitions = new HashSet<>();
        for (Etat<T> e : etats) {
            transitions.addAll(e.getListeTransitions());
        }
        return transitions;
    }

    public void clear() {
        for (Etat<T> etat : etats) {
            etat.clearTransitions();
        }
        etats.clear();
    }

    public ObservableSet<Etat<T>> etatsProperty() {
        return etats;
    }

    public Set<Etat<T>> getEtatsInitiaux() {
        Set<Etat<T>> res = new HashSet<>();
        for (Etat<T> etat : etats) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    public Set<Etat<T>> getEtats() {
        return etats;
    }

    public enum StatusExecution {
        PRETE, EN_COURS, INTERROMPUE, TERMINEE
    }
}































