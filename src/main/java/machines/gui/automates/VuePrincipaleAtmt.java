package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextFlow;
import machines.App;
import machines.gui.*;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

public class VuePrincipaleAtmt extends VuePrincipale<TransitionAtmt> {
    private ToolBar barreDeMenu;
    private HBox hBoxLancerAutomate;
    private HBox hBoxAjoutTransition;
    private TextFlow textFlowMot;
    private Button boutonStop;
    private ProgressBar progressBar;
    private Pane paneVide;
    private Label labelSpinner;
    private VueAutomate vueAutomate;
    private FileChooser fileChooser;
    private EventHandler<ActionEvent> eventArreter = actionEvent -> vueAutomate.getAutomate().arreter();
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

    public VuePrincipaleAtmt(App app) {
        super(app);

        getTextFieldEtiquette().setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        getTextFieldEtiquette().setPrefWidth(30);

        textFlowMot = new TextFlow();
        boutonStop = new Button("STOP");
        boutonStop.setDisable(true);
        progressBar = new ProgressBar();
        paneVide = new Pane();

        hBoxAjoutTransition = new HBox(getBoutonAjouterTransition(), getTextFieldEtiquette());
        hBoxLancerAutomate = new HBox(progressBar, paneVide, textFlowMot, getBoutonLancer(), getTextFieldMotAutomate(),
                getSpinnerVitesse(), boutonStop);

        barreDeMenu = new ToolBar(getBoutonRetourMenu(), new Separator(), getBoutonCharger(), getBoutonSauvegarder(),
                new Separator(), getBoutonCreerEtat(), getCheckBoxEstInitial(), getCheckBoxEstTerminal(),
                getBoutonClear(), getBoutonSupprimer(), hBoxAjoutTransition);

        initStyle();
        initListenersAndActions();

        setTop(barreDeMenu);
        setBottom(hBoxLancerAutomate);
    }

    @Override
    public void supprimerTransitionsSelectionnees() {
        HashSet<VueTransition<TransitionAtmt>> vuesTransitionADeDelectionner = new HashSet<>();
        for (VueTransition<TransitionAtmt> vueTransition : getVueMachine().getVuesTransitionSelectionnes()) {
            if (getVueMachine().getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin())
                    .size() == 1) {
                vueTransition.getVueEtatDep().getEtat().supprimerTransition(vueTransition.getTransition());
                vuesTransitionADeDelectionner.add(vueTransition);
            }
        }

        for (VueTransition<TransitionAtmt> vueTransition : vuesTransitionADeDelectionner) {
            vueTransition.deSelectionner();
        }

        if (getVueMachine().getVuesTransitionSelectionnes().size() > 0) {
            ObservableList<VueTransitionAtmt> vueTransitionAtmts = FXCollections.observableArrayList();
            for (VueTransition<TransitionAtmt> vueTransition : getVueMachine().getVuesTransitionSelectionnes()) {
                if (vueTransition instanceof VueTransitionAtmt)
                    vueTransitionAtmts.add((VueTransitionAtmt) vueTransition);
            }

            StageSupTransAtmt stageSupTrans = new StageSupTransAtmt(vueTransitionAtmts, getApp().getPrimaryStage());

            ArrayList<VueTransitionAtmt> transitionsASupprimer = stageSupTrans.showOpenDialog();
            for (VueTransitionAtmt vueTransition : transitionsASupprimer) {
                vueTransition.getVueEtatDep().getEtat().supprimerTransition(vueTransition.getTransition());
                vueTransition.deSelectionner();
            }
        }
    }

    @Override
    public void ajouterTransition() {
        ObservableList<VueEtat<TransitionAtmt>> vuesEtatSelectionnes = getVueMachine().getVuesEtatSelectionnes();
        if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problème ajout transition");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez sélectionner 1 ou 2 etats");
            alert.showAndWait();
        } else {
            String etiquette = getTextFieldEtiquette().getText();
            VueEtat<TransitionAtmt> vueEtatDep = vuesEtatSelectionnes.get(0);
            VueEtat<TransitionAtmt> vueEtatArrivee;
            if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
            else vueEtatArrivee = vuesEtatSelectionnes.get(1);
            if (etiquette.length() >= 1) {
                boolean nouvelleTrans = true;
                for (TransitionAtmt t : getVueMachine().getMachine().getTransitions()) {
                    if (t.getEtatDepart() == vueEtatDep.getEtat() && t.getEtatArrivee() == vueEtatArrivee.getEtat() && t.getEtiquette() == etiquette.charAt(
                            0)) {
                        nouvelleTrans = false;
                        break;
                    }
                }
                if (nouvelleTrans) {
                    vueEtatDep.getEtat().ajoutTransition(
                            new TransitionAtmt(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0)));
                }
            }
        }
    }

    public void initListenersAndActions() {
        Automate automate = vueAutomate.getAutomate();

        progressBar.progressProperty().bind(automate.progressProperty());

        ChangeListener<Integer> listenerIndex = (observableValue, integer, t1) -> {
            if (textFlowMot.getChildren().size() > t1) {
                textFlowMot.getChildren().get(t1)
                        .setStyle("-fx-font-weight: bold; -fx-text-fill: #037fdb; -fx-font-size: 19");
            }
        };

        automate.setListenerValueTaskLancer(listenerIndex);

        automate.setOnRunning(workerStateEvent -> {
            getSpinnerVitesse().setDisable(true);
            boutonStop.setDisable(false);
            textFlowMot.getChildren().clear();
            String mot = automate.getMot();
            for (int i = 0; i < mot.length(); i++) {
                Label lettre = new Label(String.valueOf(mot.charAt(i)));
                lettre.setStyle("-fx-font-weight: bold; -fx-font-size: 19");
                textFlowMot.getChildren().add(lettre);
            }
        });

        automate.setOnCancelled(workerStateEvent -> {
            boutonStop.setDisable(true);
            getSpinnerVitesse().setDisable(false);
        });

        automate.setOnSucceeded(workerStateEvent -> {
            getSpinnerVitesse().setDisable(false);
            boutonStop.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultat");
            alert.setHeaderText(null);
            if (automate.motReconnu()) alert.setContentText("Mot reconnu");
            else alert.setContentText("Mot non reconnu");
            alert.showAndWait();
        });

        boutonStop.setOnAction(eventArreter);
    }

    @Override
    public void lancer() {
        Automate automate = vueAutomate.getAutomate();
        String mot = getTextFieldMotAutomate().getText();
        long dellayMillis = Double.valueOf(getSpinnerVitesse().getValue() * 1000).longValue();
        automate.lancer(mot, dellayMillis);
    }

    @Override
    public void sauvegarder() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder automate");

        Path path = Paths.get("./fichiers_machine");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getAbsolutePath();
                int index = selectedFile.getName().lastIndexOf('.');
                if (index < 0 || !selectedFile.getName().substring(index).equals(".atmt")) {
                    fileName = fileName.concat(".atmt");
                }

                getVueMachine().sauvegarder(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void charger() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Charger automate");

        Path path = Paths.get("./fichiers_machine");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ATMT", "*.atmt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                getVueMachine().chargerFichier(selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public VueMachine<TransitionAtmt> creerVueMachine() {
        vueAutomate = new VueAutomate(new Automate(), this);
        return vueAutomate;
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
    }

    private void initStyle() {
        barreDeMenu.setStyle("-fx-spacing: 10");

        hBoxLancerAutomate.setAlignment(Pos.CENTER_RIGHT);
        hBoxLancerAutomate.setPadding(new Insets(5));
        getSpinnerVitesse().setPrefWidth(75);

        textFlowMot.setPadding(new Insets(0, 20, 0, 0));

        boutonStop.setStyle("-fx-font-weight: bold");

        HBox.setHgrow(paneVide, Priority.ALWAYS);

        progressBar.setStyle("-fx-accent: #037fdb");
    }

    public HBox getHBoxAjoutTransition() {
        return hBoxAjoutTransition;
    }

    public Button getBoutonStop() {
        return boutonStop;
    }
}