package machines.automates.gui;

import machines.App;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VuePrincipaleAtmt extends BorderPane {
    private App app;

    private VueAutomate vueAutomate;
    private HBox barreDeMenu;
    private HBox hBoxLancerAutomate;
    private HBox hBoxAjoutTransition;

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

    private FileChooser fileChooser;

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
                    .ajouterEtat(new EtatAtmt(checkBoxEstInitial.isSelected(), checkBoxEstTerminal.isSelected()));
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
    private EventHandler<ActionEvent> eventSauvegarder = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder automate");

            Path path = Paths.get("./automates_atmt");
            if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
            else fileChooser.setInitialDirectory(new File("./"));

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                try {
                    vueAutomate.sauvegarder(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private EventHandler<ActionEvent> eventCharger = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Charger automate");

            Path path = Paths.get("./automates_atmt");
            if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
            else fileChooser.setInitialDirectory(new File("./"));

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    vueAutomate.chargerFichier(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private EventHandler<KeyEvent> eventToucheCtrlPresse = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            ctrlPresse = keyEvent.isControlDown();
        }
    };
    private EventHandler<ActionEvent> eventSupprimer = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            ArrayList<VueEtatAtmt> vuesEtatADeSelectionner = new ArrayList<>();
            for (VueEtatAtmt vueEtat : vueAutomate.getVuesEtatSelectionnes()) {
                for (TransitionAtmt t : vueAutomate.getAutomate().getTransitions()) {
                    if (t.getEtatDepart() == vueEtat.getEtat() || t.getEtatArrivee() == vueEtat.getEtat()) {
                        VueTransitionAtmt vueTransition = vueAutomate.getVueTransition(t);
                        vueTransition.deSelectionner();
                    }
                }
                vuesEtatADeSelectionner.add(vueEtat);
                vueAutomate.getAutomate().supprimerEtat(vueEtat.getEtat());
            }
            for (VueEtatAtmt vueEtat : vuesEtatADeSelectionner) {
                vueEtat.deSelectionner();
            }

            HashSet<VueTransitionAtmt> vuesTransitionADeDelectionner = new HashSet<>();
            for (VueTransitionAtmt vueTransition : vueAutomate.getVuesTransitionSelectionnes()) {
                if (vueAutomate.getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin())
                        .size() == 1) {
                    vueAutomate.getAutomate().supprimerTransition(vueTransition.getTransition());
                    vuesTransitionADeDelectionner.add(vueTransition);
                }
            }
            vueAutomate.getVuesTransitionSelectionnes().removeAll(vuesTransitionADeDelectionner);
            for (VueTransitionAtmt vueTransition : vuesTransitionADeDelectionner) {
                vueTransition.deSelectionner();
            }

            if (vueAutomate.getVuesTransitionSelectionnes().size() > 0) {
                StageSupTrans stageSupTrans = new StageSupTrans(vueAutomate.getVuesTransitionSelectionnes(), app.getPrimaryStage());
                ArrayList<VueTransitionAtmt> transitionsASupprimer = stageSupTrans.showOpenDialog();
                for (VueTransitionAtmt vueTransition : transitionsASupprimer) {
                    vueAutomate.getAutomate().supprimerTransition(vueTransition.getTransition());
                    vueTransition.deSelectionner();
                }
            }
        }
    };
    private EventHandler<ActionEvent> eventAjouterTransition = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            ObservableList<VueEtatAtmt> vuesEtatSelectionnes = vueAutomate.getVuesEtatSelectionnes();
            if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Problème ajout transition");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez sélectionner 1 ou 2 etats");
                alert.showAndWait();
            } else {
                String etiquette = textFieldEtiquette.getText();
                VueEtatAtmt vueEtatDep = vuesEtatSelectionnes.get(0);
                VueEtatAtmt vueEtatArrivee;
                if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
                else vueEtatArrivee = vuesEtatSelectionnes.get(1);
                if (etiquette.length() >= 1) {
                    boolean nouvelleTrans = true;
                    for (TransitionAtmt t : vueAutomate.getAutomate().getTransitions()) {
                        if (t.getEtatDepart() == vueEtatDep.getEtat() &&
                                t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                                t.getEtiquette() == etiquette.charAt(0)) {
                            nouvelleTrans = false;
                            break;
                        }
                    }
                    if (nouvelleTrans) {
                        vueAutomate.getAutomate().ajoutTransition(
                                new TransitionAtmt(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0)));
                    }
                }
                textFieldEtiquette.setText("");
            }
        }
    };

    public VuePrincipaleAtmt(App app) {
        this.app = app;
        initComposants();
        initSetOnAction();

        Automate automate = new Automate();
        vueAutomate = new VueAutomate(automate, this);

        ctrlPresse = false;
        setOnKeyPressed(eventToucheCtrlPresse);
        setOnKeyReleased(eventToucheCtrlPresse);

        textFieldEtiquette.setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        textFieldEtiquette.setPrefWidth(30);

        hBoxAjoutTransition = new HBox(boutonAjouterTransition, textFieldEtiquette);
        hBoxLancerAutomate = new HBox(boutonLancer, textFieldMotAutomate);

        barreDeMenu =
                new HBox(boutonCharger, boutonSauvegarder, boutonCreerEtat, checkBoxEstInitial, checkBoxEstTerminal,
                        boutonClear, boutonSupprimer, hBoxAjoutTransition);
        initStyle();

        setTop(barreDeMenu);
        setCenter(vueAutomate);
        setBottom(hBoxLancerAutomate);
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

    public void initSetOnAction() {
        boutonCreerEtat.setOnAction(eventAjouterEtat);
        boutonSupprimer.setOnAction(eventSupprimer);
        boutonLancer.setOnAction(eventLancerAutomate);
        boutonAjouterTransition.setOnAction(eventAjouterTransition);
        boutonClear.setOnAction(eventClear);
        boutonCharger.setOnAction(eventCharger);
        boutonSauvegarder.setOnAction(eventSauvegarder);
    }

    public CheckBox getCheckBoxEstInitial() {
        return checkBoxEstInitial;
    }

    public CheckBox getCheckBoxEstTerminal() {
        return checkBoxEstTerminal;
    }

    public TextField getTextFieldEtiquette() {
        return textFieldEtiquette;
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
    }

    public void unbindCheckBoxes() {
        for (EtatAtmt e : vueAutomate.getAutomate().getEtats()) {
            checkBoxEstInitial.selectedProperty().unbindBidirectional(e.estInitialProperty());
            checkBoxEstTerminal.selectedProperty().unbindBidirectional(e.estTerminalProperty());
        }
    }

    private void initStyle() {
        barreDeMenu.setSpacing(20);
        barreDeMenu.setAlignment(Pos.CENTER_LEFT);
        hBoxLancerAutomate.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLancerAutomate.setPadding(new Insets(0, 10, 10, 0));
    }
}
