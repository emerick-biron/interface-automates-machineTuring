package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VuePrincipale extends BorderPane {

    private Button boutonCreerEtat;
    private VueAutomate vueAutomate;
    private EventHandler<ActionEvent> eventAjouterEtat = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueAutomate.getAutomate().ajouterEtat(new Etat());
        }
    };

    public VuePrincipale() throws IOException {
        boutonCreerEtat = new Button("Ajouter etat");
        vueAutomate = new VueAutomate(new Automate());
        boutonCreerEtat.setOnAction(eventAjouterEtat);

        setTop(boutonCreerEtat);
        setCenter(vueAutomate);

    }
}
