package machines.automates.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import machines.gui.VueTransition;

public class VueTransitionAtmt extends VueTransition<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt, TransitionAtmt> {
    public VueTransitionAtmt(TransitionAtmt transition, VueAutomate vueAutomate) {
        super(transition, vueAutomate);
    }


}






























