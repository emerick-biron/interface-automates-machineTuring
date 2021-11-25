package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import machines.gui.VueTransition;

public class VueTransitionAtmt extends VueTransition {

    public VueTransitionAtmt(TransitionAtmt transition, VueAutomate vueAutomate) {
        super(transition, vueAutomate);
        initListeners();
    }


    public VueAutomate getVueAutomate() {
        return (VueAutomate) super.getVueMachine();
    }
}






























