package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class VuePrincipale extends BorderPane {

    private ActionsSouris actionsSouris = ActionsSouris.DEPLACER_ETAT;
    private Button boutonCreerEtat;
    private Button boutonSupprimerEtat;
    private VueAutomate vueAutomate;
    private HBox barreDeMenu;
    private EventHandler<ActionEvent> eventAjouterEtat = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueAutomate.getAutomate().ajouterEtat(new Etat());
        }
    };

    public VuePrincipale() throws IOException {
        boutonSupprimerEtat = new Button("Supprimer Etat");
        boutonSupprimerEtat.setOnAction(actionEvent -> actionsSouris = ActionsSouris.SUPPRIMER_ETAT);
        boutonCreerEtat = new Button("Ajouter etat");
        vueAutomate = new VueAutomate(new Automate(), this);
        boutonCreerEtat.setOnAction(eventAjouterEtat);

        barreDeMenu = new HBox(boutonCreerEtat, boutonSupprimerEtat);
        setTop(barreDeMenu);
        setCenter(vueAutomate);

    }

    public ActionsSouris getActionsSouris() {
        return actionsSouris;
    }
}
