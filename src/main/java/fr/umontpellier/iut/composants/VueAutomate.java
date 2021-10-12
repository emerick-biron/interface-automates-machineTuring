package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class VueAutomate extends Pane {
    private Automate automate;
    private ListChangeListener<Etat> miseAJourEtats = new ListChangeListener<Etat>() {
        @Override
        public void onChanged(Change<? extends Etat> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Etat e : change.getAddedSubList()) {
                        getChildren().add(new VueEtat(e));
                    }
                } else if (change.wasRemoved()){
                    for (Etat e : change.getRemoved()){
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

    public VueAutomate(Automate automate){
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
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
