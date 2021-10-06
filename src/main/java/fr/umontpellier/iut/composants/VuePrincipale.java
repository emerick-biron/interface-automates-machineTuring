package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class VuePrincipale extends BorderPane {

    private Button boutonCreerEtat;
    private VueAutomate vueAutomate;

    public VuePrincipale() {
        boutonCreerEtat = new Button("Ajouter etat");
        vueAutomate = new VueAutomate(new Automate());

        boutonCreerEtat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                VueEtat vueEtat = new VueEtat(new Etat());
                vueAutomate.ajouterVueEtat(vueEtat);
            }
        });

        setTop(boutonCreerEtat);
        setCenter(vueAutomate);

    }
}
