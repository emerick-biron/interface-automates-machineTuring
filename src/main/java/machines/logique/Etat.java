package machines.logique;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import machines.logique.automates.TransitionAtmt;

import java.util.*;

public class Etat<T extends Transition<T>> {
    private BooleanProperty estInitial;
    private BooleanProperty estTerminal;
    /**
     * Liste des transitions SORTANTES de l'etat
     */
    private ObservableSet<T> listeTransitions;
    private BooleanProperty estActif;

    public Etat() {
        estInitial = new SimpleBooleanProperty(false);
        estTerminal = new SimpleBooleanProperty(false);
        listeTransitions = FXCollections.observableSet(new HashSet<>());
        estActif = new SimpleBooleanProperty(false);
    }

    public Etat(boolean estInitial, boolean estTerminal) {
        this.estInitial = new SimpleBooleanProperty(estInitial);
        this.estTerminal = new SimpleBooleanProperty(estTerminal);
        listeTransitions = FXCollections.observableSet(new HashSet<>());
        estActif = new SimpleBooleanProperty(false);
    }

    /**
     * Permet de savoir si il existe une transition sortante de cet etat portant une certaine lettre
     *
     * @param c lettre a tester
     * @return true si la transition existe sinon false
     */
    public boolean existeTrans(char c) {
        for (T transition : getListeTransitions()) {
            if (transition.getEtiquette() == c) return true;
        }
        return false;
    }

    /**
     * Permet d'obtenir la liste des etat qui peuvent etre atteint a partir de l'etat qui courant avec la lettre c
     *
     * @param c lettre a tester
     * @return liste des etat qui peuvent etre atteint, null si il n'y en a pas
     */
    public Set<Etat<T>> cible(char c) {
        Set<Etat<T>> resultat = new HashSet<>();
        for (T t : getListeTransitions()) {
            if (t.getEtiquette() == c) resultat.add(t.getEtatArrivee());
        }
        return resultat;
    }

    public void clearTransitions(){
        listeTransitions.clear();
    }

    public void ajoutTransition(T transition){
        listeTransitions.add(transition);
    }

    public void ajoutTransition(Collection<T> transitions){
        listeTransitions.addAll(transitions);
    }

    public void supprimerTransition(T transition){
        listeTransitions.remove(transition);
    }

    public void supprimerTransition(Collection<T> transitions){
        listeTransitions.removeAll(transitions);
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

    public Set<T> getListeTransitions() {
        return listeTransitions;
    }
    public ObservableSet<T> transitionsProperty() {
        return listeTransitions;
    }
}





































