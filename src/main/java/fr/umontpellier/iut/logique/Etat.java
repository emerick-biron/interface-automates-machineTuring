package fr.umontpellier.iut.logique;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Etat {
    private boolean estInitial;
    private boolean estTerminal;
    private ArrayList<Transition> listeTransitions;
    private BooleanProperty estActif;

    public void setEstInitial(boolean estInitial) {
        this.estInitial = estInitial;
    }

    public void setEstTerminal(boolean estTerminal) {
        this.estTerminal = estTerminal;
    }

    public Etat(boolean estInitial, boolean estTerminal, ArrayList<Transition> listeTransitions) {
        this.estInitial = estInitial;
        this.estTerminal = estTerminal;
        this.listeTransitions = listeTransitions;
        estActif = new SimpleBooleanProperty(false);
    }

    public Etat() {
        estInitial = false;
        estTerminal = false;
        listeTransitions = new ArrayList<>();
        estActif = new SimpleBooleanProperty(false);

    }

    public Etat(boolean estInitial, boolean estTerminal) {
        this.estInitial = estInitial;
        this.estTerminal = estTerminal;
        listeTransitions = new ArrayList<>();
        estActif = new SimpleBooleanProperty(false);
    }

    public boolean existeTrans(char c) {
        for (Transition transition : listeTransitions) {
            if (transition.getEtiquette() == c) return true;
        }
        return false;
    }

    public boolean estInitial() {
        return estInitial;
    }

    public boolean estTerminal() {
        return estTerminal;
    }

    public ArrayList<Transition> getListeTransitions() {
        return listeTransitions;
    }

    public Etat cible(char c) {
        if (!existeTrans(c)) return null;
        for (Transition transition : listeTransitions) {
            if (transition.getEtiquette() == c) return transition.getEtatArrivee();
        }
        return null;
    }

    public void ajoutTransition(Transition... t){
        listeTransitions.addAll(Arrays.asList(t));
    }

    public List<Etat> cibleND(char c){
        List<Etat> resultat = new ArrayList<>();
        for (Transition t : listeTransitions) {
            if (t.getEtiquette()==c) resultat.add(t.getEtatArrivee());
        }
        return resultat;
    }
}
























