package machines.logique;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * Permet de creer une machine a partir d'un fichier
     *
     * @param nomFichier nom du fichier contenant la machine
     * @throws IOException
     */
    public abstract void chargerFichier(String nomFichier) throws IOException;

    /**
     * Permet de sauvegarder la machine dans un fichier
     *
     * @param nomFichier nom du fichier dans lequel sauvegarder la machine
     * @throws IOException
     */
    public abstract void sauvegarder(String nomFichier) throws IOException;

    /**
     * Permet de lancer la machine
     *
     * @param mot mot a donner a la machine
     */
    public abstract void lancer(String mot);

    /**
     * Permet de savoir si un mot et reconnu par la machine
     *
     * @return true si c'est le cas sinon false
     */
    public abstract boolean motReconnu();

    /**
     * Ajoute un etat a la machine
     *
     * @param etat etat a ajouter
     */
    public void ajouterEtat(Etat<T> etat) {
        etats.add(etat);
    }

    /**
     * Supprime un etat de la machine
     *
     * @param etat etat a supprimer
     */
    public void supprimerEtat(Etat<T> etat) {
        etats.remove(etat);
    }

    /**
     * @return transition de la machine
     */
    public Set<T> getTransitions() {
        Set<T> transitions = new HashSet<>();
        etats.forEach(e -> transitions.addAll(e.getListeTransitions()));
        return transitions;
    }

    /**
     * Supprime tous les etat et toutes les transitions le la machine
     */
    public void clear() {
        etats.forEach(Etat::clearTransitions);
        etats.clear();
    }

    public ObservableSet<Etat<T>> etatsProperty() {
        return etats;
    }

    /**
     * @return etats inition de la machine
     */
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

    public void setOnRunning(EventHandler<WorkerStateEvent> onRunning) {
        this.onRunning.set(onRunning);
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onRunningProperty() {
        return onRunning;
    }

    public EventHandler<WorkerStateEvent> getOnSucceeded() {
        return onSucceeded.get();
    }

    public void setOnSucceeded(EventHandler<WorkerStateEvent> onSucceeded) {
        this.onSucceeded.set(onSucceeded);
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onSucceededProperty() {
        return onSucceeded;
    }

    public EventHandler<WorkerStateEvent> getOnCancelled() {
        return onCancelled.get();
    }

    public void setOnCancelled(EventHandler<WorkerStateEvent> onCancelled) {
        this.onCancelled.set(onCancelled);
    }

    public ObjectProperty<EventHandler<WorkerStateEvent>> onCancelledProperty() {
        return onCancelled;
    }
}































