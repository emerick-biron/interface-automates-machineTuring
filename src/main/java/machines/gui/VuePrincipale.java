package machines.gui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import machines.App;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

public abstract class VuePrincipale extends BorderPane {
    private App app;

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
    private EventHandler<KeyEvent> eventToucheCtrlPresse = new EventHandler<>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            ctrlPresse = keyEvent.isControlDown();
        }
    };

    public VuePrincipale(App app) {
        this.app = app;

        initComposants();

        ctrlPresse = false;
        setOnKeyPressed(eventToucheCtrlPresse);
        setOnKeyReleased(eventToucheCtrlPresse);
    }

    public App getApp() {
        return app;
    }

    public abstract void unbindCheckBoxes();

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








































