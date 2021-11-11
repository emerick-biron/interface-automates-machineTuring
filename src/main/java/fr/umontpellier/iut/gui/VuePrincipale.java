package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.App;
import fr.umontpellier.iut.logique.Automate;
import fr.umontpellier.iut.logique.Etat;
import fr.umontpellier.iut.logique.Transition;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class VuePrincipale extends BorderPane {

    private VueAutomate vueAutomate;
    private HBox barreDeMenu;

    private boolean ctrlPresse;
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
                    vueAutomate.sauvegarder(selectedFile.getAbsolutePath() + ".atmt");
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
            ArrayList<VueEtat> vuesEtatADeSelectionner = new ArrayList<>();
            for (VueEtat vueEtat : vueAutomate.getVuesEtatSelectionnes()) {
                for (Transition t : vueAutomate.getAutomate().getTransitions()) {
                    if (t.getEtatDepart() == vueEtat.getEtat() || t.getEtatArrivee() == vueEtat.getEtat()) {
                        VueTransition vueTransition = vueAutomate.getVueTransition(t);
                        vueTransition.deSelectionner();
                    }
                }
                vuesEtatADeSelectionner.add(vueEtat);
                vueAutomate.getAutomate().supprimerEtat(vueEtat.getEtat());
            }
            for (VueEtat vueEtat : vuesEtatADeSelectionner) {
                vueEtat.deSelectionner();
            }

            HashSet<VueTransition> vuesTransitionADeDelectionner = new HashSet<>();
            for (VueTransition vueTransition : vueAutomate.getVuesTransitionSelectionnes()) {
                if (vueAutomate.getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin())
                        .size() == 1) {
                    vueAutomate.getAutomate().supprimerTransition(vueTransition.getTransition());
                    vuesTransitionADeDelectionner.add(vueTransition);
                }
            }
            vueAutomate.getVuesTransitionSelectionnes().removeAll(vuesTransitionADeDelectionner);
            for (VueTransition vueTransition : vuesTransitionADeDelectionner) {
                vueTransition.deSelectionner();
            }

            StageSupTrans stageSupTrans =
                    new StageSupTrans(vueAutomate.getVuesTransitionSelectionnes(), App.getPrimStage());
            ArrayList<VueTransition> transitionsASupprimer = stageSupTrans.showOpenDialog();
            for (VueTransition vueTransition : transitionsASupprimer) {
                vueAutomate.getAutomate().supprimerTransition(vueTransition.getTransition());
                vueTransition.deSelectionner();
            }
        }
    };
    private EventHandler<ActionEvent> eventAjouterTransition = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            ObservableList<VueEtat> vuesEtatSelectionnes = vueAutomate.getVuesEtatSelectionnes();
            if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Problème ajout transition");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez sélectionner exactement 2 etats");
                alert.showAndWait();
            } else {
                String etiquette = textFieldEtiquette.getText();
                VueEtat vueEtatDep = vuesEtatSelectionnes.get(0);
                VueEtat vueEtatArrivee;
                if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
                else vueEtatArrivee = vuesEtatSelectionnes.get(1);
                if (etiquette.length() >= 1) {
                    boolean nouvelleTrans = true;
                    for (Transition t : vueAutomate.getAutomate().getTransitions()) {
                        if (t.getEtatDepart() == vueEtatDep.getEtat() &&
                                t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                                t.getEtiquette() == etiquette.charAt(0)) {
                            nouvelleTrans = false;
                            break;
                        }
                    }
                    if (nouvelleTrans) {
                        vueAutomate.getAutomate().ajoutTransition(
                                new Transition(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0)));
                    }
                }
                textFieldEtiquette.setText("");
            }
        }
    };

    public VuePrincipale() {
        initComposants();
        initSetOnAction();

        Automate automate = new Automate();
        vueAutomate = new VueAutomate(automate, this);

        ctrlPresse = false;
        setOnKeyPressed(eventToucheCtrlPresse);
        setOnKeyReleased(eventToucheCtrlPresse);

        textFieldEtiquette.setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        textFieldEtiquette.setPrefWidth(30);

        barreDeMenu =
                new HBox(boutonCharger, boutonSauvegarder, boutonCreerEtat, checkBoxEstInitial, checkBoxEstTerminal,
                        boutonLancer, textFieldMotAutomate, boutonClear, boutonSupprimerEtat, boutonAjouterTransition,
                        textFieldEtiquette);
        setTop(barreDeMenu);
        setCenter(vueAutomate);
    }

    public Button getBoutonSupprimerEtat() {
        return boutonSupprimerEtat;
    }

    public Button getBoutonAjouterTransition() {
        return boutonAjouterTransition;
    }

    public boolean ctrlPresse() {
        return ctrlPresse;
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
        boutonSupprimerEtat.setOnAction(eventSupprimer);
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
        for (Etat e : vueAutomate.getAutomate().getEtats()) {
            checkBoxEstInitial.selectedProperty().unbindBidirectional(e.estInitialProperty());
            checkBoxEstTerminal.selectedProperty().unbindBidirectional(e.estTerminalProperty());
        }
    }
}
