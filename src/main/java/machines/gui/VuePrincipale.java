package machines.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import machines.App;
import machines.automates.gui.VueAutomate;
import machines.automates.logique.EtatAtmt;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public abstract class VuePrincipale<VP extends VuePrincipale<VP, VM, VE, VT, M, E, T>, VM extends VueMachine<VP, VM,
        VE, VT, M, E, T>, VE extends VueEtat<VP, VM, VE, VT, M, E, T>, VT extends VueTransition<VP, VM, VE, VT, M, E,
        T>, M extends Machine<E, T>, E extends Etat<E, T>, T extends Transition<T, E>>
        extends BorderPane {
    private App app;

    private VM vueMachine;

    private boolean ctrlPresse;
    private Button boutonCreerEtat;
    private Button boutonSupprimer;
    private Button boutonAjouterTransition;
    private Button boutonSauvegarder;
    private Button boutonCharger;
    private Button boutonClear;
    private Button boutonLancer;

    private CheckBox checkBoxEstInitial;
    private CheckBox checkBoxEstTerminal;

    private TextField textFieldMotAutomate;
    private TextField textFieldEtiquette;
    private EventHandler<ActionEvent> eventLancerAutomate = actionEvent -> {
        //TODO Faire des tests pour voir si les entrees sont ok
        vueMachine.getMachine().lancer(textFieldMotAutomate.getText(), 1000);
    };
    private EventHandler<ActionEvent> eventClear = actionEvent -> {
        vueMachine.clear();
    };
    private EventHandler<KeyEvent> eventToucheCtrlPresse = new EventHandler<>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            ctrlPresse = keyEvent.isControlDown();
        }
    };

    public VuePrincipale(App app) {
        this.app = app;

        vueMachine = getVueMachine();

        initComposants();
        initSetOnAction();

        ctrlPresse = false;
        setOnKeyPressed(eventToucheCtrlPresse);
        setOnKeyReleased(eventToucheCtrlPresse);

        setCenter(vueMachine);
    }

    public abstract VM getVueMachine();

    public App getApp() {
        return app;
    }

    public void initSetOnAction() {
        boutonLancer.setOnAction(eventLancerAutomate);
        boutonClear.setOnAction(eventClear);
    }

    public void unbindCheckBoxes() {
        for (E e : vueMachine.getMachine().getEtats()) {
            checkBoxEstInitial.selectedProperty().unbindBidirectional(e.estInitialProperty());
            checkBoxEstTerminal.selectedProperty().unbindBidirectional(e.estTerminalProperty());
        }
    }

    public Button getBoutonSupprimer() {
        return boutonSupprimer;
    }

    public Button getBoutonAjouterTransition() {
        return boutonAjouterTransition;
    }

    public boolean ctrlPresse() {
        return ctrlPresse;
    }

    public void initComposants() {
        boutonSupprimer = new Button("Supprimer");
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

    public Button getBoutonCreerEtat() {
        return boutonCreerEtat;
    }

    public Button getBoutonSauvegarder() {
        return boutonSauvegarder;
    }

    public Button getBoutonCharger() {
        return boutonCharger;
    }

    public Button getBoutonClear() {
        return boutonClear;
    }

    public Button getBoutonLancer() {
        return boutonLancer;
    }

    public CheckBox getCheckBoxEstInitial() {
        return checkBoxEstInitial;
    }

    public CheckBox getCheckBoxEstTerminal() {
        return checkBoxEstTerminal;
    }

    public TextField getTextFieldMotAutomate() {
        return textFieldMotAutomate;
    }

    public TextField getTextFieldEtiquette() {
        return textFieldEtiquette;
    }
}








































