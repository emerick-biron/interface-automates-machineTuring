package machines.gui.mt;

import machines.gui.VueMachine;
import machines.gui.VuePrincipale;
import machines.logique.mt.MachineTuring;
import machines.logique.mt.TransitionMT;

public class VueMT extends VueMachine<TransitionMT> {
    private MachineTuring mt;
    public VueMT(MachineTuring machine, VuePrincipale<TransitionMT> vuePrincipale) {
        super(machine, vuePrincipale);
        mt = machine;
    }

    public MachineTuring getMt() {
        return mt;
    }

    @Override
    public void ajoutVueTransition(TransitionMT transition) {
        VueTransitionMT vueTransition = new VueTransitionMT(transition, VueMT.this);
        getChildren().add(vueTransition);
        vueTransition.toBack();
        int nbrTrans = 0;
        for (TransitionMT t : transition.getEtatDepart().getListeTransitions()) {
            if (t.getEtatArrivee() == transition.getEtatArrivee()) nbrTrans++;
        }
        vueTransition.positionnerLabelEtiquette(nbrTrans - 1);
    }
}
