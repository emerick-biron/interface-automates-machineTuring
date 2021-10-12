package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class VueAutomate extends Pane {
    private Automate automate;
    private VuePrincipale vuePrincipale;
    private ListChangeListener<Etat> miseAJourEtats = new ListChangeListener<Etat>() {
        @Override
        public void onChanged(Change<? extends Etat> change) {
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
        }
    };

    public VueAutomate(Automate automate, VuePrincipale vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
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
}
