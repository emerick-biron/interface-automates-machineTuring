package machines.logique;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.*;

public abstract class Machine<T extends Transition<T>> {
    private ObservableSet<Etat<T>> etats;
    private ObjectProperty<EventHandler<WorkerStateEvent>> onRunning;
    private ObjectProperty<EventHandler<WorkerStateEvent>> onSucceeded;
    private ObjectProperty<EventHandler<WorkerStateEvent>> onCancelled;

    public Machine(Set<Etat<T>> etats) {
        this.etats = FXCollections.observableSet(etats);
        onRunning = new SimpleObjectProperty<>();
        onSucceeded = new SimpleObjectProperty<>();
        onCancelled = new SimpleObjectProperty<>();
    }

    public Machine() {
        etats = FXCollections.observableSet(new HashSet<>());
        onRunning = new SimpleObjectProperty<>();
        onSucceeded = new SimpleObjectProperty<>();
        onCancelled = new SimpleObjectProperty<>();
    }

    public abstract void chargerFichier(String nomFichier) throws IOException;

    public abstract void sauvegarder(String nomFichier) throws IOException;

    public abstract void lancer(String mot);

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


    public EventHandler<WorkerStateEvent> getOnRunning() {
        return onRunning.get();
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onRunningProperty() {
        return onRunning;
    }

    public void setOnRunning(EventHandler<WorkerStateEvent> onRunning) {
        this.onRunning.set(onRunning);
    }

    public EventHandler<WorkerStateEvent> getOnSucceeded() {
        return onSucceeded.get();
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onSucceededProperty() {
        return onSucceeded;
    }

    public void setOnSucceeded(EventHandler<WorkerStateEvent> onSucceeded) {
        this.onSucceeded.set(onSucceeded);
    }

    public EventHandler<WorkerStateEvent> getOnCancelled() {
        return onCancelled.get();
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onCancelledProperty() {
        return onCancelled;
    }

    public void setOnCancelled(EventHandler<WorkerStateEvent> onCancelled) {
        this.onCancelled.set(onCancelled);
    }
}































