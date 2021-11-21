package machines.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine<E extends Etat<E, T>, T extends Transition<T, E>> {
    private ObservableList<E> etats;
    private ObservableList<T> transitions;
    private List<E> etatsActifs;


    public Machine(List<E> etats) {
        this.etats = FXCollections.observableArrayList(etats);
        transitions = FXCollections.observableArrayList();
        etatsActifs = new ArrayList<>();
    }

    @SafeVarargs
    public Machine(E... etats) {
        this.etats = FXCollections.observableArrayList(etats);
        transitions = FXCollections.observableArrayList();
        etatsActifs = new ArrayList<>();
    }

    public Machine() {
        etats = FXCollections.observableArrayList();
        transitions = FXCollections.observableArrayList();
        etatsActifs = new ArrayList<>();
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;

    public abstract boolean lancer(String mot);

    public abstract boolean lancer(String mot, long dellayMillis);

    @SafeVarargs
    public final void ajoutTransition(T... transitions) {
        this.transitions.addAll(transitions);
        for (T t : transitions) {
            t.getEtatDepart().getListeTransitions().add(t);
        }
    }

    public void ajoutTransition(ArrayList<T> transitions) {
        this.transitions.addAll(transitions);
        for (T t : transitions) {
            t.getEtatDepart().getListeTransitions().add(t);
        }
    }

    public void supprimerTransition(List<T> transitions) {
        this.transitions.removeAll(transitions);
        for (T t : transitions) {
            t.getEtatDepart().getListeTransitions().remove(t);
        }
    }

    public void supprimerTransition(T t) {
        this.transitions.remove(t);
        t.getEtatDepart().getListeTransitions().remove(t);
    }

    public void ajouterEtat(E etat) {
        etats.add(etat);
    }

    public void supprimerEtat(E etat) {
        ArrayList<T> transitionsASupprimer = new ArrayList<>();
        for (T t : transitions) {
            if (t.getEtatDepart() == etat) transitionsASupprimer.add(t);
            else if (t.getEtatArrivee() == etat) transitionsASupprimer.add(t);
        }
        supprimerTransition(transitionsASupprimer);
        etats.remove(etat);
    }

    public ObservableList<T> getTransitions() {
        return transitions;
    }

    public ObservableList<T> transitionsProperty() {
        return transitions;
    }


    public ObservableList<E> etatsProperty() {
        return etats;
    }

    public List<E> getEtatsInitiaux() {
        List<E> res = new ArrayList<>();
        for (E etat : etats) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    public List<E> getEtats() {
        return etats;
    }

    public List<E> getEtatsActifs() {
        return etatsActifs;
    }
}































