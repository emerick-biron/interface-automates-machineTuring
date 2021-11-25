package machines.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine{
    private ObservableList<Etat> etats;
    private ObservableList<Transition> transitions;
    private List<Etat> etatsActifs;


    public Machine(List<Etat> etats) {
        this.etats = FXCollections.observableArrayList(etats);
        transitions = FXCollections.observableArrayList();
        etatsActifs = new ArrayList<>();
    }

    public Machine(Etat... etats) {
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

    public void lancer(Task<Boolean> taskLancer){
        new Thread(taskLancer).start();
    }

    public abstract Task<Boolean> getTaskLancer(String mot, long dellayMillis);

    public final void ajoutTransition(Transition... transitions) {
        this.transitions.addAll(transitions);
        for (Transition t : transitions) {
            t.getEtatDepart().getListeTransitions().add(t);
        }
    }

    public void ajoutTransition(ArrayList<? extends Transition> transitions) {
        this.transitions.addAll(transitions);
        for (Transition t : transitions) {
            t.getEtatDepart().getListeTransitions().add(t);
        }
    }

    public void supprimerTransition(List<Transition> transitions) {
        this.transitions.removeAll(transitions);
        for (Transition t : transitions) {
            t.getEtatDepart().getListeTransitions().remove(t);
        }
    }

    public void supprimerTransition(Transition t) {
        this.transitions.remove(t);
        t.getEtatDepart().getListeTransitions().remove(t);
    }

    public void ajouterEtat(Etat etat) {
        etats.add(etat);
    }

    public void supprimerEtat(Etat etat) {
        ArrayList<Transition> transitionsASupprimer = new ArrayList<>();
        for (Transition t : transitions) {
            if (t.getEtatDepart() == etat) transitionsASupprimer.add(t);
            else if (t.getEtatArrivee() == etat) transitionsASupprimer.add(t);
        }
        supprimerTransition(transitionsASupprimer);
        etats.remove(etat);
    }

    public ObservableList<Transition> getTransitions() {
        return transitions;
    }

    public ObservableList<Transition> transitionsProperty() {
        return transitions;
    }


    public ObservableList<Etat> etatsProperty() {
        return etats;
    }

    public List<Etat> getEtatsInitiaux() {
        List<Etat> res = new ArrayList<>();
        for (Etat etat : etats) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    public List<Etat> getEtats() {
        return etats;
    }

    public List<Etat> getEtatsActifs() {
        return etatsActifs;
    }
}































