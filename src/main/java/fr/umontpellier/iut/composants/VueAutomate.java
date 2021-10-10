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
                for (Node n : getChildren()) {
                    if (n instanceof VueEtat) {
                        VueEtat vueEtat = (VueEtat) n;
                        vueEtat.setLabelNumEtat(automate.etatsProperty().indexOf(vueEtat.getEtat()));
                    }
                }
            }
        }
    };

    public VueAutomate(Automate automate) {
        this.automate = automate;
        automate.etatsProperty().addListener(miseAJourEtats);
    }

    public void ajouterVueEtat(VueEtat vueEtat) throws InterruptedException {
        getChildren().add(vueEtat);
        automate.ajouterEtat(vueEtat.getEtat());
    }

    public VueEtat getVueEtat(Etat etat){
        for (Node n : getChildren()) {
            if (n instanceof VueEtat){
                VueEtat vueEtat = (VueEtat) n;
                if(vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    public void supprimerVueEtat(VueEtat vueEtat){
        getChildren().remove(vueEtat);
        automate.supprimerEtat(vueEtat.getEtat());
    }
}
