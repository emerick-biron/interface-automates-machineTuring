package machines.gui.automates;

import javafx.collections.SetChangeListener;
import javafx.stage.Screen;
import javafx.stage.Stage;
import machines.gui.VueEtat;
import machines.gui.VueTransition;
import machines.logique.Etat;
import machines.logique.Transition;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.VueMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends VueMachine {
    private SetChangeListener<Transition> miseAJourTransitions = change -> {
        if (change.wasAdded()) {
            Transition t = change.getElementAdded();
            ajoutVueTransition(t);
        } else if (change.wasRemoved()) {
            Transition t = change.getElementRemoved();
            supprimerVueTransition(t);
        }
    };
    private SetChangeListener<Etat> miseAJourEtats = change -> {
        if (change.wasAdded()) change.getElementAdded().transitionsProperty().addListener(miseAJourTransitions);
    };

    public VueAutomate(Automate automate, VuePrincipaleAtmt vuePrincipale) {
        super(automate, vuePrincipale);
        automate.etatsProperty().addListener(miseAJourEtats);
    }

    public Automate getAutomate() {
        return (Automate) super.getMachine();
    }

    @Override
    public void ajoutVueTransition(Transition transition) {
        VueTransitionAtmt vueTransition = new VueTransitionAtmt((TransitionAtmt) transition, VueAutomate.this);
        getChildren().add(vueTransition);
        vueTransition.toBack();
        int nbrTrans = 0;
        for (Transition t : transition.getEtatDepart().getListeTransitions()) {
            if (t.getEtatArrivee() == transition.getEtatArrivee()) nbrTrans++;
        }
        vueTransition.positionnerLabelEtiquette(nbrTrans - 1);
    }
}




































