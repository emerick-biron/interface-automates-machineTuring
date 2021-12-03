package machines.gui.automates;

import javafx.collections.SetChangeListener;
import machines.gui.VueMachine;
import machines.logique.Etat;
import machines.logique.Transition;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;

public class VueAutomate extends VueMachine<TransitionAtmt> {
    Automate automate;

    public VueAutomate(Automate automate, VuePrincipaleAtmt vuePrincipale) {
        super(automate, vuePrincipale);
        this.automate = automate;
    }

    public Automate getAutomate() {
        return automate;
    }

    @Override
    public void ajoutVueTransition(TransitionAtmt transition) {
        VueTransitionAtmt vueTransition = new VueTransitionAtmt(transition, VueAutomate.this);
        getChildren().add(vueTransition);
        vueTransition.toBack();
        int nbrTrans = 0;
        for (TransitionAtmt t : transition.getEtatDepart().getListeTransitions()) {
            if (t.getEtatArrivee() == transition.getEtatArrivee()) nbrTrans++;
        }
        vueTransition.positionnerLabelEtiquette(nbrTrans - 1);
    }
}




































