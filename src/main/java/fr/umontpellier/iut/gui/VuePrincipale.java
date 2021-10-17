package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VuePrincipale extends BorderPane {

    private ActionSouris actionSouris = ActionSouris.DEPLACER_ETAT;
    private Button boutonCreerEtat;
    private Button boutonSupprimerEtat;
    private Button boutonAjouterTransition;
    private VueAutomate vueAutomate;
    private HBox barreDeMenu;
    private Button boutonLancer;
    private TextField textFieldMotAutomate = new TextField();
    private UnaryOperator<TextFormatter.Change> textFilterAjoutTransition = change -> {
        String input = change.getControlNewText();
        if (!change.isContentChange()) {
            return change;
        }
        if (!Pattern.matches("[a-z]?", input)) {
            return null;
        }
        return change;
    };
    private TextField textFieldEtiquette = new TextField();
    private EventHandler<ActionEvent> eventAjouterEtat = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueAutomate.getAutomate().ajouterEtat(new Etat());
        }
    };
    private EventHandler<ActionEvent> eventLancerAutomate = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Task<Void> taskLancer = vueAutomate.getAutomate().getTaskLancer(textFieldMotAutomate.getText(), 1000);
            //TODO Faire des tests pour voir si les entrees sont ok
            try {
                vueAutomate.getAutomate().lancer(taskLancer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public VuePrincipale() {
        boutonSupprimerEtat = new Button("Supprimer Etat");
        boutonCreerEtat = new Button("Ajouter etat");
        boutonLancer = new Button("Lancer");
        boutonAjouterTransition = new Button("Ajouter transition");


        Automate automate = new Automate();
        vueAutomate = new VueAutomate(automate, this);

        boutonCreerEtat.setOnAction(eventAjouterEtat);
        boutonSupprimerEtat.setOnAction(actionEvent -> actionSouris = ActionSouris.SUPPRIMER_ETAT);
        boutonLancer.setOnAction(eventLancerAutomate);
        boutonAjouterTransition.setOnAction(actionEvent -> actionSouris = ActionSouris.AJOUTER_TRANSITION);

        textFieldEtiquette.setTextFormatter(new TextFormatter<Object>(textFilterAjoutTransition));

        barreDeMenu = new HBox(boutonCreerEtat, boutonSupprimerEtat, boutonAjouterTransition, textFieldEtiquette,
                boutonLancer, textFieldMotAutomate);
        setTop(barreDeMenu);
        setCenter(vueAutomate);
    }

    public TextField getTextFieldEtiquette() {
        return textFieldEtiquette;
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
