package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import machines.App;
import machines.logique.automates.Automate;
import machines.logique.automates.EtatAtmt;
import machines.logique.automates.TransitionAtmt;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import machines.gui.VuePrincipale;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VuePrincipaleAtmt extends
        VuePrincipale<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt,
                TransitionAtmt> {

    private VueAutomate vueAutomate;

    private HBox barreDeMenu;
    private HBox hBoxLancerAutomate;
    private HBox hBoxAjoutTransition;
    private HBox hBoxLabelsLettres;

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
    private EventHandler<ActionEvent> eventClear = actionEvent -> {
        getVueAutomate().clear();
    };
    private EventHandler<ActionEvent> eventLancerAutomate = actionEvent -> {
        //TODO Faire des tests pour voir si les entrees sont ok
        Task<Boolean> taskLancer = vueAutomate.getAutomate().getTaskLancer(getTextFieldMotAutomate().getText(), 1000);
        taskLancer.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                String text = getTextFieldMotAutomate().getText();
                int indice = (int) (t1.doubleValue() * text.length());
                if (indice == 0){
                    hBoxLabelsLettres = new HBox();
                    hBoxLabelsLettres.setStyle("-fx-font-weight: bold; -fx-font-size: 19");
                    hBoxLabelsLettres.setPadding(new Insets(0,20,0,0));
                    for (int i = 0; i < text.length(); i++) {
                        Label labelLettre = new Label(String.valueOf(text.charAt(i)));
                        hBoxLabelsLettres.getChildren().add(labelLettre);
                    }
                    hBoxLancerAutomate.getChildren().add(0, hBoxLabelsLettres);
                }
                hBoxLabelsLettres.getChildren().get(indice).setStyle("-fx-text-fill: #037fdb");
            }
        });
        taskLancer.setOnSucceeded(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultat");
            alert.setHeaderText(null);
            if (taskLancer.getValue()) alert.setContentText("Mot reconnu");
            else alert.setContentText("Mot non reconnu");
            alert.showAndWait();
            hBoxLancerAutomate.getChildren().remove(hBoxLabelsLettres);
        });
        vueAutomate.getAutomate().lancer(taskLancer);
    };
    private EventHandler<ActionEvent> eventAjouterEtat = actionEvent -> getVueAutomate().getAutomate()
            .ajouterEtat(new EtatAtmt(getCheckBoxEstInitial().isSelected(), getCheckBoxEstTerminal().isSelected()));
    private EventHandler<ActionEvent> eventSauvegarder = actionEvent -> {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder automate");

        Path path = Paths.get("./automates_atmt");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getAbsolutePath();
                int index = selectedFile.getName().lastIndexOf('.');
                if (index < 0 || !selectedFile.getName().substring(index).equals(".atmt")){
                    fileName = fileName.concat(".atmt");
                }

                getVueAutomate().sauvegarder(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private EventHandler<ActionEvent> eventCharger = actionEvent -> {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Charger automate");

        Path path = Paths.get("./automates_atmt");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                getVueAutomate().chargerFichier(selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private EventHandler<ActionEvent> eventSupprimer = actionEvent -> {
        ArrayList<VueEtatAtmt> vuesEtatADeSelectionner = new ArrayList<>();
        for (VueEtatAtmt vueEtat : getVueAutomate().getVuesEtatSelectionnes()) {
            for (TransitionAtmt t : getVueAutomate().getAutomate().getTransitions()) {
                if (t.getEtatDepart() == vueEtat.getEtat() || t.getEtatArrivee() == vueEtat.getEtat()) {
                    VueTransitionAtmt vueTransition = getVueAutomate().getVueTransition(t);
                    vueTransition.deSelectionner();
                }
            }
            vuesEtatADeSelectionner.add(vueEtat);
            getVueAutomate().getAutomate().supprimerEtat(vueEtat.getEtat());
        }
        for (VueEtatAtmt vueEtat : vuesEtatADeSelectionner) {
            vueEtat.deSelectionner();
        }

        HashSet<VueTransitionAtmt> vuesTransitionADeDelectionner = new HashSet<>();
        for (VueTransitionAtmt vueTransition : getVueAutomate().getVuesTransitionSelectionnes()) {
            if (getVueAutomate().getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin())
                    .size() == 1) {
                getVueAutomate().getAutomate().supprimerTransition(vueTransition.getTransition());
                vuesTransitionADeDelectionner.add(vueTransition);
            }
        }
        getVueAutomate().getVuesTransitionSelectionnes().removeAll(vuesTransitionADeDelectionner);
        for (VueTransitionAtmt vueTransition : vuesTransitionADeDelectionner) {
            vueTransition.deSelectionner();
        }

        if (getVueAutomate().getVuesTransitionSelectionnes().size() > 0) {
            StageSupTrans stageSupTrans =
                    new StageSupTrans(getVueAutomate().getVuesTransitionSelectionnes(), getApp().getPrimaryStage());
            ArrayList<VueTransitionAtmt> transitionsASupprimer = stageSupTrans.showOpenDialog();
            for (VueTransitionAtmt vueTransition : transitionsASupprimer) {
                getVueAutomate().getAutomate().supprimerTransition(vueTransition.getTransition());
                vueTransition.deSelectionner();
            }
        }
    };
    private EventHandler<ActionEvent> eventAjouterTransition = actionEvent -> {
        ObservableList<VueEtatAtmt> vuesEtatSelectionnes = getVueAutomate().getVuesEtatSelectionnes();
        if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problème ajout transition");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez sélectionner 1 ou 2 etats");
            alert.showAndWait();
        } else {
            String etiquette = getTextFieldEtiquette().getText();
            VueEtatAtmt vueEtatDep = vuesEtatSelectionnes.get(0);
            VueEtatAtmt vueEtatArrivee;
            if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
            else vueEtatArrivee = vuesEtatSelectionnes.get(1);
            if (etiquette.length() >= 1) {
                boolean nouvelleTrans = true;
                for (TransitionAtmt t : getVueAutomate().getAutomate().getTransitions()) {
                    if (t.getEtatDepart() == vueEtatDep.getEtat() && t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                            t.getEtiquette() == etiquette.charAt(0)) {
                        nouvelleTrans = false;
                        break;
                    }
                }
                if (nouvelleTrans) {
                    getVueAutomate().getAutomate().ajoutTransition(
                            new TransitionAtmt(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0)));
                }
            }
            getTextFieldEtiquette().setText("");
        }
    };

    public VuePrincipaleAtmt(App app) {
        super(app);
        initSetOnAction();

        vueAutomate = new VueAutomate(new Automate(), this);


        getTextFieldEtiquette().setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        getTextFieldEtiquette().setPrefWidth(30);

        hBoxAjoutTransition = new HBox(getBoutonAjouterTransition(), getTextFieldEtiquette());
        hBoxLancerAutomate = new HBox(getBoutonLancer(), getTextFieldMotAutomate());

        barreDeMenu =
                new HBox(getBoutonCharger(), getBoutonSauvegarder(), getBoutonCreerEtat(), getCheckBoxEstInitial(),
                        getCheckBoxEstTerminal(), getBoutonClear(), getBoutonSupprimer(), hBoxAjoutTransition);

        initStyle();

        setTop(barreDeMenu);
        setCenter(vueAutomate);
        setBottom(hBoxLancerAutomate);
    }

    public VueAutomate getVueMachine() {
        return getVueAutomate();
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
    }

    @Override
    public void unbindCheckBoxes() {
        for (EtatAtmt e : getVueMachine().getMachine().getEtats()) {
            getCheckBoxEstInitial().selectedProperty().unbindBidirectional(e.estInitialProperty());
            getCheckBoxEstTerminal().selectedProperty().unbindBidirectional(e.estTerminalProperty());
        }
    }

    public void initSetOnAction() {
        Button b = getBoutonClear();
        getBoutonClear().setOnAction(eventClear);
        getBoutonLancer().setOnAction(eventLancerAutomate);
        getBoutonCreerEtat().setOnAction(eventAjouterEtat);
        getBoutonSupprimer().setOnAction(eventSupprimer);
        getBoutonAjouterTransition().setOnAction(eventAjouterTransition);
        getBoutonCharger().setOnAction(eventCharger);
        getBoutonSauvegarder().setOnAction(eventSauvegarder);
    }

    private void initStyle() {
        barreDeMenu.setSpacing(20);
        barreDeMenu.setAlignment(Pos.CENTER_LEFT);
        hBoxLancerAutomate.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLancerAutomate.setPadding(new Insets(0, 10, 10, 0));
    }
}
