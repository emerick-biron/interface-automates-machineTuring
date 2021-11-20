package machines.logique;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import machines.automates.logique.TransitionAtmt;

import java.util.ArrayList;

public abstract class Etat<T extends Transition> {
    private BooleanProperty estInitial;
    private BooleanProperty estTerminal;
    /**
     * Liste des transitions SORTANTES de l'etat
     */
    private ArrayList<T> listeTransitions;
    private BooleanProperty estActif;

    public Etat() {
        estInitial = new SimpleBooleanProperty(false);
        estTerminal = new SimpleBooleanProperty(false);
        listeTransitions = new ArrayList<>();
        estActif = new SimpleBooleanProperty(false);
    }

    public Etat(boolean estInitial, boolean estTerminal) {
        this.estInitial = new SimpleBooleanProperty(estInitial);
        this.estTerminal = new SimpleBooleanProperty(estTerminal);
        listeTransitions = new ArrayList<>();
        estActif = new SimpleBooleanProperty(false);
    }

    public BooleanProperty estActifProperty() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif.set(estActif);
    }

    public void setEstInitial(boolean estInitial) {
        this.estInitial.set(estInitial);
    }

    public void setEstTerminal(boolean estTerminal) {
        this.estTerminal.set(estTerminal);
    }

    public boolean estInitial() {
        return estInitial.getValue();
    }

    public boolean estTerminal() {
        return estTerminal.getValue();
    }

    public void active() {
        estActif.set(true);
    }

    public void desactive() {
        estActif.set(false);
    }

    public BooleanProperty estInitialProperty() {
        return estInitial;
    }

    public BooleanProperty estTerminalProperty() {
        return estTerminal;
    }

    public boolean estActif() {
        return estActif.getValue();
    }

    public ArrayList<T> getListeTransitions() {
        return listeTransitions;
    }
}





































