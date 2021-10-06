package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Automate;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class VueAutomate extends Pane {
    private Automate automate;

    public VueAutomate(Automate automate) {
        this.automate = automate;
    }

    public void ajouterVueEtat(VueEtat vueEtat){
        getChildren().add(vueEtat);
        automate.ajouterEtat(vueEtat.getEtat());
    }
}
