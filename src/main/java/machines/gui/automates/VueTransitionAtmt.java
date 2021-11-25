package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import machines.gui.VueTransition;

public class VueTransitionAtmt extends
        VueTransition<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt,
                TransitionAtmt> {
    private ChangeListener<Boolean> changementSelection = (observableValue, aBoolean, t1) -> {
        if (aBoolean != t1) {
            if (observableValue.getValue()) {
                for (VueTransitionAtmt vueTransition : getVueAutomate()
                        .getVuesTransition(getVueEtatDep(), getVueEtatFin())) {
                    getVueAutomate().getVuesTransitionSelectionnes().add(vueTransition);
                }
            } else {
                getVueAutomate().getVuesTransitionSelectionnes().remove(VueTransitionAtmt.this);
                for (VueTransitionAtmt vueTransition : getVueAutomate()
                        .getVuesTransition(getVueEtatDep(), getVueEtatFin())) {
                    getVueAutomate().getVuesTransitionSelectionnes().remove(vueTransition);
                }
            }
        }
    };

    public VueTransitionAtmt(TransitionAtmt transition, VueAutomate vueAutomate) {
        super(transition, vueAutomate);
        initListeners();
    }

    @Override
    public void initListeners() {
        estSelectionneProperty().addListener(changementSelection);
    }

    public VueAutomate getVueAutomate() {
        return super.getVueMachine();
    }
}






























