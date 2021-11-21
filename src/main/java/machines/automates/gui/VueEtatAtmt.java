package machines.automates.gui;

import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import machines.gui.VueEtat;

public class VueEtatAtmt extends
        VueEtat<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt, TransitionAtmt> {
    public VueEtatAtmt(EtatAtmt etat, VueAutomate vueAutomate) {
        super(etat, vueAutomate);
    }

    public VueAutomate getVueAutomate() {
        return getVueMachine();
    }
}
