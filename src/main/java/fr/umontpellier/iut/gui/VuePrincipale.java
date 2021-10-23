package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VuePrincipale extends BorderPane {

    private ActionSouris actionSouris = ActionSouris.DEPLACER_ETAT;
    private VueAutomate vueAutomate;
    private HBox barreDeMenu;

    private Button boutonCreerEtat;
    private Button boutonSupprimerEtat;
    private Button boutonAjouterTransition;
    private Button boutonSauvegarder;
    private Button boutonCharger;
    private Button boutonClear;
    private Button boutonLancer;

    private CheckBox checkBoxEstInitial;
    private CheckBox checkBoxEstTerminal;

    private TextField textFieldMotAutomate;
    private TextField textFieldEtiquette;

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
    private EventHandler<ActionEvent> eventAjouterEtat = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueAutomate.getAutomate()
                    .ajouterEtat(new Etat(checkBoxEstInitial.isSelected(), checkBoxEstTerminal.isSelected()));
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
    private EventHandler<ActionEvent> eventClear = actionEvent -> {
        vueAutomate.clear();
    };

    public VuePrincipale() {
        initComposants();
        initSetOnAction();

        Automate automate = new Automate();
        vueAutomate = new VueAutomate(automate, this);


        textFieldEtiquette.setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        textFieldEtiquette.setPrefWidth(30);

        barreDeMenu = new HBox(boutonCreerEtat, checkBoxEstInitial, checkBoxEstTerminal, boutonSupprimerEtat,
                boutonAjouterTransition, textFieldEtiquette, boutonLancer, textFieldMotAutomate, boutonClear);
        setTop(barreDeMenu);
        setCenter(vueAutomate);
    }

    public void initComposants() {
        boutonSupprimerEtat = new Button("Supprimer Etat");
        boutonCreerEtat = new Button("Ajouter etat");
        boutonLancer = new Button("Lancer");
        boutonClear = new Button("Clear");
        boutonSauvegarder = new Button("Sauvegarder");
        boutonCharger = new Button("Charger");
        boutonAjouterTransition = new Button("Ajouter transition");
        checkBoxEstInitial = new CheckBox("Initial");
        checkBoxEstTerminal = new CheckBox("Terminal");
        textFieldEtiquette = new TextField();
        textFieldMotAutomate = new TextField();
    }

    public void initSetOnAction() {
        boutonCreerEtat.setOnAction(eventAjouterEtat);
        boutonSupprimerEtat.setOnAction(actionEvent -> actionSouris = ActionSouris.SUPPRIMER_ETAT);
        boutonLancer.setOnAction(eventLancerAutomate);
        boutonAjouterTransition.setOnAction(actionEvent -> actionSouris = ActionSouris.AJOUTER_TRANSITION);
        boutonClear.setOnAction(eventClear);
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
