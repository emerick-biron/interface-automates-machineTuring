package machines.automates.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import machines.gui.VueEtat;

public class VueEtatAtmt extends
        VueEtat<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt, TransitionAtmt> {
    private ChangeListener<Boolean> changementActivationEtat = (observableValue, ancienneValeur, nouvelleValeur) -> {
        if (observableValue.getValue()) getCercle().setFill(Color.GREEN);
        else getCercle().setFill(Color.RED);
    };
    private ChangeListener<Boolean> changementEstInitial = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(getImageViewInitial());
        else getChildren().remove(getImageViewInitial());
    };
    private ChangeListener<Boolean> changementEstTerminal = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(getImageViewTerminal());
        else getChildren().remove(getImageViewTerminal());
    };
    private ChangeListener<Boolean> changementSelection = (observableValue, aBoolean, t1) -> {
        if (aBoolean != t1) {
            if (observableValue.getValue()) {
                getVueAutomate().getVuesEtatSelectionnes().add(VueEtatAtmt.this);
            } else {
                getVueAutomate().getVuesEtatSelectionnes().remove(VueEtatAtmt.this);
            }
        }
    };

    public VueEtatAtmt(EtatAtmt etat, VueAutomate vueAutomate) {
        super(etat, vueAutomate);
    }

    @Override
    public void initListeners() {
        getEtat().estActifProperty().addListener(changementActivationEtat);
        getEtat().estInitialProperty().addListener(changementEstInitial);
        getEtat().estTerminalProperty().addListener(changementEstTerminal);
        estSelectionneProperty().addListener(changementSelection);
    }

    public VueAutomate getVueAutomate() {
        return getVueMachine();
    }
}
