package machines.gui.mt;

import machines.App;
import machines.gui.VueMachine;
import machines.gui.VuePrincipale;
import machines.logique.mt.TransitionMT;

public class VuePrincipaleMT extends VuePrincipale<TransitionMT> {
    public VuePrincipaleMT(App app) {
        super(app);
    }

    @Override
    public VueMachine<TransitionMT> creerVueMachine() {
        return null;
    }

    @Override
    public void sauvegarder() {

    }

    @Override
    public void charger() {

    }

    @Override
    public void lancer() {

    }

    @Override
    public void ajouterTransition() {

    }
}
