package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import machines.logique.automates.Automate;
import machines.logique.automates.EtatAtmt;
import machines.logique.automates.TransitionAtmt;
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
        initListeners();
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
