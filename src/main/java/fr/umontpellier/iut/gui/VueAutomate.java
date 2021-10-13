package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import fr.umontpellier.iut.logique.Transition;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class VueAutomate extends Pane {
    private Automate automate;
    private VuePrincipale vuePrincipale;
    private ListChangeListener<Etat> miseAJourEtats = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Etat e : change.getAddedSubList()) {
                    getChildren().add(new VueEtat(e, VueAutomate.this));
                }
            } else if (change.wasRemoved()) {
                for (Etat e : change.getRemoved()) {
                    getChildren().remove(getVueEtat(e));
                }
            }

        }
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat vueEtat = (VueEtat) n;
                vueEtat.setLabelNumEtat(automate.etatsProperty().indexOf(vueEtat.getEtat()));
            }
        }
    };
    private ListChangeListener<Transition> miseAJourTransition = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Transition t : change.getAddedSubList()) {
                    VueTransition vueTransition = new VueTransition(t, VueAutomate.this);
                    getChildren().add(vueTransition);
                    vueTransition.toBack();
                }
            } else if (change.wasRemoved()) {
                for (Transition t : change.getRemoved()) {
                    getChildren().remove(getVueTransition(t));
                }
            }

        }
    };

    public VueAutomate(Automate automate, VuePrincipale vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
        this.automate.transitionsProperty().addListener(miseAJourTransition);
        setOnMousePressed(mouseEvent -> vuePrincipale.setDefaultActionSouris());
    }

    public VuePrincipale getVuePrincipale() {
        return vuePrincipale;
    }

    public Automate getAutomate() {
        return automate;
    }

    public VueEtat getVueEtat(Etat etat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat vueEtat = (VueEtat) n;
                if (vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    public VueTransition getVueTransition(Transition transition) {
        for (Node n : getChildren()) {
            if (n instanceof VueTransition) {
                VueTransition vueTransition = (VueTransition) n;
                if (vueTransition.getTransition().equals(transition)) return vueTransition;
            }
        }
        return null;
    }
}
