package machines.automates.logique;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import machines.logique.Etat;

import java.util.ArrayList;
import java.util.List;

public class EtatAtmt extends Etat<TransitionAtmt> {

    public EtatAtmt() {
        super();
    }

    public EtatAtmt(boolean estInitial, boolean estTerminal) {
        super(estInitial, estTerminal);
    }

    /**
     * Permet de savoir si il existe une transition sortante de cet etat portant une certaine lettre
     *
     * @param c lettre a tester
     * @return true si la transition existe sinon false
     */
    public boolean existeTrans(char c) {
        for (TransitionAtmt transition : getListeTransitions()) {
            if (transition.getEtiquette() == c) return true;
        }
        return false;
    }

    /**
     * Permet d'obtenir l'etat qui peut etre atteint a partir de l'etat courant avec une certaine lettre (ne
     * fonctionne que pour un automate deterministe)
     *
     * @param c lettre a tester
     * @return null si il n'exite pas de transition sortante portant la lettre c sinon l'etat qui peut etre atteint
     * avec la lettre c
     */
    public EtatAtmt cible(char c) {
        if (!existeTrans(c)) return null;
        for (TransitionAtmt transition : getListeTransitions()) {
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
    public List<EtatAtmt> cibleND(char c) {
        List<EtatAtmt> resultat = new ArrayList<>();
        for (TransitionAtmt t : getListeTransitions()) {
            if (t.getEtiquette() == c) resultat.add(t.getEtatArrivee());
        }
        return resultat;
    }
}
























