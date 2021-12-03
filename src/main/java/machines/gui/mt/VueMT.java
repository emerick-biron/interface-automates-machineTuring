package machines.gui.mt;

import machines.gui.VueMachine;
import machines.gui.VuePrincipale;
import machines.logique.Machine;
import machines.logique.mt.TransitionMT;

public class VueMT extends VueMachine<TransitionMT> {
    public VueMT(Machine<TransitionMT> machine, VuePrincipale<TransitionMT> vuePrincipale) {
        super(machine, vuePrincipale);
    }

    @Override
    public void ajoutVueTransition(TransitionMT transition) {

    }
}
