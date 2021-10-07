package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
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
                        vueEtat.setLabelNumEtat(10);
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

    public void ajouterVueEtat(VueEtat vueEtat) {
        getChildren().add(vueEtat);
        automate.ajouterEtat(vueEtat.getEtat());
    }
}
