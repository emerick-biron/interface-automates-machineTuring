package machines.gui.mt;

import javafx.collections.ListChangeListener;
import machines.gui.VueEtat;
import machines.gui.VueMachine;
import machines.logique.mt.MachineTuring;
import machines.logique.mt.TransitionMT;

public class VueMT extends VueMachine<TransitionMT> {
    private MachineTuring machineTuring;
    private VuePrincipaleMT vuePrincipaleMT;
    private ListChangeListener<VueEtat<TransitionMT>> miseAJourVuesEtatSelectionnes =
            change -> vuePrincipaleMT.gethBoxAjoutTransition()
                    .setVisible(getVuesEtatSelectionnes().size() <= 2 && getVuesEtatSelectionnes().size() >= 1);

    public VueMT(MachineTuring machine, VuePrincipaleMT vuePrincipale) {
        super(machine, vuePrincipale);
        this.vuePrincipaleMT = vuePrincipale;
        machineTuring = machine;

        getVuesEtatSelectionnes().addListener(miseAJourVuesEtatSelectionnes);
    }

    public VuePrincipaleMT getVuePrincipaleMT() {
        return vuePrincipaleMT;
    }

    public MachineTuring getMachineTuring() {
        return machineTuring;
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
