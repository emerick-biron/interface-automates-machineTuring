package machines.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import machines.App;
import machines.logique.Etat;
import machines.logique.Transition;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class VuePrincipale<T extends Transition<T>> extends BorderPane {
    private App app;
    private VueMachine<T> vueMachine;

    private boolean ctrlPresse;
    private Button boutonCreerEtat;
    private Button boutonSupprimer;
    private Button boutonAjouterTransition;
    private Button boutonSauvegarder;
    private Button boutonCharger;
    private Button boutonClear;
    private Button boutonLancer;
    private Button boutonRetourMenu;
    private Spinner<Double> spinnerVitesse;
    private ScrollPane scrollPaneCenter;

    private CheckBox checkBoxEstInitial;
    private CheckBox checkBoxEstTerminal;

    private TextField textFieldMotAutomate;
    private TextField textFieldEtiquette;
    private EventHandler<ActionEvent> eventClear = actionEvent -> vueMachine.clear();
    private EventHandler<ActionEvent> eventSauvegarder = actionEvent -> sauvegarder();
    private EventHandler<ActionEvent> eventCharger = actionEvent -> charger();
    private EventHandler<ActionEvent> eventLancerAutomate = actionEvent -> lancer();
    private EventHandler<ActionEvent> eventAjouterTransition = actionEvent -> ajouterTransition();
    private EventHandler<ActionEvent> eventAjouterEtat = actionEvent -> vueMachine.getMachine()
            .ajouterEtat(new Etat<T>(getCheckBoxEstInitial().isSelected(), getCheckBoxEstTerminal().isSelected()));
    private EventHandler<KeyEvent> eventToucheCtrlPresse = new EventHandler<>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            ctrlPresse = keyEvent.isControlDown();
        }
    };
    private EventHandler<ActionEvent> eventSupprimer = actionEvent -> {
        supprimerEtatsSelectionnes();
        supprimerTransitionsSelectionnees();
    };

    public VuePrincipale(App app) {
        this.app = app;
        initComposants();
        initSetOnAction();

        vueMachine = creerVueMachine();
        scrollPaneCenter = new ScrollPane(vueMachine);
        setCenter(scrollPaneCenter);

        ctrlPresse = false;
        setOnKeyPressed(eventToucheCtrlPresse);
        setOnKeyReleased(eventToucheCtrlPresse);
    }

    private void initSetOnAction() {
        getBoutonClear().setOnAction(eventClear);
        getBoutonCreerEtat().setOnAction(eventAjouterEtat);
        getBoutonCharger().setOnAction(eventCharger);
        getBoutonSauvegarder().setOnAction(eventSauvegarder);
        getBoutonLancer().setOnAction(eventLancerAutomate);
        getBoutonSupprimer().setOnAction(eventSupprimer);
        getBoutonAjouterTransition().setOnAction(eventAjouterTransition);
    }

    /**
     * Permet de creer la vueMachine
     *
     * @return vueMachine creee
     */
    public abstract VueMachine<T> creerVueMachine();

    public VueMachine<T> getVueMachine() {
        return vueMachine;
    }

    public App getApp() {
        return app;
    }

    /**
     * Permet d'enlever le lien entre les checkboxes er la propriete selectionne des etats
     */
    public void unbindCheckBoxes() {
        for (Etat<T> e : vueMachine.getMachine().getEtats()) {
            checkBoxEstInitial.selectedProperty().unbindBidirectional(e.estInitialProperty());
            checkBoxEstTerminal.selectedProperty().unbindBidirectional(e.estTerminalProperty());
        }
    }

    /**
     * Supprime tous les etat selectionnes
     */
    public void supprimerEtatsSelectionnes() {
        ArrayList<VueEtat<T>> vuesEtatADeSelectionner = new ArrayList<>();
        for (VueEtat<T> vueEtat : vueMachine.getVuesEtatSelectionnes()) {
            for (T t : vueMachine.getMachine().getTransitions()) {
                if (t.getEtatDepart() == vueEtat.getEtat() || t.getEtatArrivee() == vueEtat.getEtat()) {
                    VueTransition<T> vueTransition = vueMachine.getVueTransition(t);
                    if (vueTransition != null) vueTransition.deSelectionner();
                    t.getEtatDepart().supprimerTransition(t);
                }
            }
            vuesEtatADeSelectionner.add(vueEtat);
            vueMachine.getMachine().supprimerEtat(vueEtat.getEtat());
        }
        vuesEtatADeSelectionner.forEach(VueEtat::deSelectionner);
    }

    /**
     * Supprime toutes les transition selectionnes
     */
    public void supprimerTransitionsSelectionnees() {
        HashSet<VueTransition<T>> vuesTransitionADeDelectionner = new HashSet<>();
        for (VueTransition<T> vueTransition : vueMachine.getVuesTransitionSelectionnes()) {
            vueTransition.getVueEtatDep().getEtat().supprimerTransition(vueTransition.getTransition());
            vuesTransitionADeDelectionner.add(vueTransition);
        }
        vuesTransitionADeDelectionner.forEach(VueTransition::deSelectionner);
    }

    /**
     * Sauvegarde la machine
     */
    public abstract void sauvegarder();

    /**
     * Charge la machine
     */
    public abstract void charger();

    /**
     * Lance la machine
     */
    public abstract void lancer();

    /**
     * Ajoute une transition a la machine en fonction des entrees de l'utilisateur
     */
    public abstract void ajouterTransition();

    public Button getBoutonSupprimer() {
        return boutonSupprimer;
    }

    public Button getBoutonAjouterTransition() {
        return boutonAjouterTransition;
    }

    public boolean ctrlPresse() {
        return ctrlPresse;
    }

    private void initComposants() {
        boutonSupprimer = new Button("Supprimer");
        boutonCreerEtat = new Button("Ajouter etat");
        boutonLancer = new Button("Lancer");
        boutonClear = new Button("Clear");
        boutonSauvegarder = new Button("Sauvegarder");
        boutonCharger = new Button("Charger");
        boutonAjouterTransition = new Button("Ajouter transition");
        boutonRetourMenu = new Button("Retour menu");
        checkBoxEstInitial = new CheckBox("Initial");
        checkBoxEstTerminal = new CheckBox("Terminal");
        textFieldEtiquette = new TextField();
        textFieldMotAutomate = new TextField();
        spinnerVitesse = new Spinner<>(0, 2, 1, 0.01);
        spinnerVitesse.setEditable(true);
    }

    public Button getBoutonRetourMenu() {
        return boutonRetourMenu;
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

    public Spinner<Double> getSpinnerVitesse() {
        return spinnerVitesse;
    }

    public ScrollPane getScrollPaneCenter() {
        return scrollPaneCenter;
    }
}