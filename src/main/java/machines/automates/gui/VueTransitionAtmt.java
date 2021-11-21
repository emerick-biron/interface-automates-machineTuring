package machines.automates.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
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






























