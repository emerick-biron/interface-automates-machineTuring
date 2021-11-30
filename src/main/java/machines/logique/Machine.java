package machines.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.*;

public abstract class Machine {
    private ObservableSet<Etat> etats;
    private Set<Etat> etatsActifs;

    public Machine(Set<Etat> etats) {
        this.etats = FXCollections.observableSet(etats);
        etatsActifs = new HashSet<>();
    }

    public Machine() {
        etats = FXCollections.observableSet(new HashSet<>());
        etatsActifs = new HashSet<>();
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;

    public void lancer(Task<Boolean> taskLancer) {
        new Thread(taskLancer).start();
    }

    public abstract Task<Boolean> getTaskLancer(String mot, long dellayMillis);

    public void ajouterEtat(Etat etat) {
        etats.add(etat);
    }

    public void supprimerEtat(Etat etat) {
        etats.remove(etat);
    }

    public Set<Transition> getTransitions() {
        Set<Transition> transitions = new HashSet<>();
        for (Etat e : etats) {
            transitions.addAll(e.getListeTransitions());
        }
        return transitions;
    }

    public void clear() {
        for (Etat etat : etats) {
            etat.clearTransitions();
        }
        etats.clear();
        etatsActifs.clear();
    }

    public ObservableSet<Etat> etatsProperty() {
        return etats;
    }

    public Set<Etat> getEtatsInitiaux() {
        Set<Etat> res = new HashSet<>();
        for (Etat etat : etats) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    public Set<Etat> getEtats() {
        return etats;
    }

    public Set<Etat> getEtatsActifs() {
        return etatsActifs;
    }
}































