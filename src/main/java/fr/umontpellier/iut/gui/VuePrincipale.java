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

    private ActionSouris actionSouris = ActionSouris.DEPLACER_ETAT;
    private Button boutonCreerEtat;
    private Button boutonSupprimerEtat;
    private VueAutomate vueAutomate;
    private HBox barreDeMenu;
    private EventHandler<ActionEvent> eventAjouterEtat = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueAutomate.getAutomate().ajouterEtat(new Etat());
        }
    };

    public VuePrincipale() throws IOException, InterruptedException {
        boutonSupprimerEtat = new Button("Supprimer Etat");
        boutonSupprimerEtat.setOnAction(actionEvent -> actionSouris = ActionSouris.SUPPRIMER_ETAT);
        boutonCreerEtat = new Button("Ajouter etat");
        Automate automate = new Automate();
        vueAutomate = new VueAutomate(automate, this);
        boutonCreerEtat.setOnAction(eventAjouterEtat);

        barreDeMenu = new HBox(boutonCreerEtat, boutonSupprimerEtat);
        setTop(barreDeMenu);
        setCenter(vueAutomate);
    }

    public ActionSouris getActionsSouris() {
        return actionSouris;
    }

    public void setActionsSouris(ActionSouris actionSouris) {
        this.actionSouris = actionSouris;
    }

    public void setDefaultActionSouris() {
        this.actionSouris = ActionSouris.DEPLACER_ETAT;
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
    }
}
