package fr.umontpellier.iut.logique;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Etat {
    private boolean estInitial;
    private boolean estTerminal;
    /**
     * Liste des transitions SORTANTES de l'etat
     */
    private ArrayList<Transition> listeTransitions;
    private BooleanProperty estActif;

    public Etat() {
        estInitial = false;
        estTerminal = false;
        listeTransitions = new ArrayList<>();
        desactive();

    }

    public Etat(boolean estInitial, boolean estTerminal) {
        this.estInitial = estInitial;
        this.estTerminal = estTerminal;
        listeTransitions = new ArrayList<>();
        if (estInitial) active();
        else desactive();
    }

    public void setEstInitial(boolean estInitial) {
        this.estInitial = estInitial;
        active();
    }

    public void setEstTerminal(boolean estTerminal) {
        this.estTerminal = estTerminal;
    }

    /**
     * Permet de savoir si il existe une transition sortante de cet etat portant une certaine lettre
     *
     * @param c lettre a tester
     * @return true si la transition existe sinon false
     */
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

    /**
     * Permet d'obtenir l'etat qui peut etre atteint a partir de l'etat courant avec une certaine lettre (ne
     * fonctionne que pour un automate deterministe)
     *
     * @param c lettre a tester
     * @return null si il n'exite pas de transition sortante portant la lettre c sinon l'etat qui peut etre atteint
     * avec la lettre c
     */
    public Etat cible(char c) {
        if (!existeTrans(c)) return null;
        for (Transition transition : listeTransitions) {
            if (transition.getEtiquette() == c) return transition.getEtatArrivee();
        }
        return null;
    }

    /**
     * Permet d'obtenir la liste des etat qui peuvent etre atteint a partir de l'etat qui courant avec la lettre c
     *
     * @param c lettre a tester
     * @return liste des etat qui peuvent etre atteint, null si il n'y en a pas
     */
    public List<Etat> cibleND(char c) {
        List<Etat> resultat = new ArrayList<>();
        for (Transition t : listeTransitions) {
            if (t.getEtiquette() == c) resultat.add(t.getEtatArrivee());
        }
        return resultat;
    }

    public void active() {
        estActif.set(true);
    }

    public void desactive() {
        estActif.set(false);
    }

    public boolean estActif() {
        return estActif.getValue();
    }
}
























