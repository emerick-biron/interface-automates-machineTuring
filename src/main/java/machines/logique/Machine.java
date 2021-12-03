package machines.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.*;

public abstract class Machine<T extends Transition<T>> {
    private ObservableSet<Etat<T>> etats;

    public Machine(Set<Etat<T>> etats) {
        this.etats = FXCollections.observableSet(etats);
    }

    public Machine() {
        etats = FXCollections.observableSet(new HashSet<>());
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;

    public void lancer(Task<?> taskLancer) {
        new Thread(taskLancer).start();
    }

    public abstract Task<?> getTaskLancer(String mot, long dellayMillis);

    public Task<?> getTaskLancer(String mot) {
        return getTaskLancer(mot, 0);
    }

    public abstract void step(char lettre);

    public abstract boolean motReconnu();

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
}































