package machines.gui.mt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import machines.App;
import machines.gui.VueEtat;
import machines.gui.VueMachine;
import machines.gui.VuePrincipale;
import machines.gui.VueTransition;
import machines.logique.Etat;
import machines.logique.mt.MachineTuring;
import machines.logique.mt.Mouvement;
import machines.logique.mt.TransitionMT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class VuePrincipaleMT extends VuePrincipale<TransitionMT> {
    private ToolBar barreDeMenu;
    private HBox hBoxLancerMachine;
    private HBox hBoxAjoutTransition;
    private TextFlow textFlowRuban;
    private TextField fieldNouvelleLettre;
    private ToggleGroup groupChoixMvmt;
    private RadioButton boutonGauche;
    private RadioButton boutonDroite;
    private HBox hBoxChoixMvmt;
    private Button boutonStop;
    private Button boutonSetInitial;
    private Pane paneVide1;
    private Pane paneVide2;

    private VueMT vueMT;

    private FileChooser fileChooser;

    private EventHandler<ActionEvent> eventArreter = actionEvent -> vueMT.getMachineTuring().arreter();
    private UnaryOperator<TextFormatter.Change> textFilterLettre = change -> {
        String input = change.getControlNewText();
        if (!change.isContentChange()) {
            return change;
        }
        if (!Pattern.matches(".?", input)) {
            return null;
        }
        return change;
    };
    private EventHandler<ActionEvent> eventSetInitial = actionEvent -> {
        ObservableList<VueEtat<TransitionMT>> vueEtatsSelect = vueMT.getVuesEtatSelectionnes();
        Etat<TransitionMT> etatInitial = vueMT.getMachineTuring().getEtatInitial();
        if (vueEtatsSelect.size() > 0) {
            if (etatInitial != null)
                vueMT.getMachineTuring().getEtatInitial().setEstInitial(false);
            vueEtatsSelect.get(0).getEtat().setEstInitial(true);
        }
    };

    public VuePrincipaleMT(App app) {
        super(app);
        getTextFieldEtiquette().setPrefWidth(30);
        getTextFieldEtiquette().setTextFormatter(new TextFormatter<>(textFilterLettre));
        fieldNouvelleLettre = new TextField();
        fieldNouvelleLettre.setPrefWidth(30);
        fieldNouvelleLettre.setTextFormatter(new TextFormatter<>(textFilterLettre));

        groupChoixMvmt = new ToggleGroup();
        boutonDroite = new RadioButton("D");
        boutonGauche = new RadioButton("G");
        boutonDroite.setToggleGroup(groupChoixMvmt);
        boutonDroite.setSelected(true);
        boutonGauche.setToggleGroup(groupChoixMvmt);

        hBoxChoixMvmt = new HBox(boutonDroite, boutonGauche);

        hBoxAjoutTransition =
                new HBox(getBoutonAjouterTransition(), getTextFieldEtiquette(), fieldNouvelleLettre, hBoxChoixMvmt);

        boutonStop = new Button("STOP");
        boutonStop.setDisable(true);
        textFlowRuban = new TextFlow();
        paneVide1 = new Pane();
        paneVide2 = new Pane();

        hBoxLancerMachine =
                new HBox(paneVide1, textFlowRuban, paneVide2, getBoutonLancer(), getTextFieldMotAutomate(),
                        getSpinnerVitesse(), boutonStop);

        boutonSetInitial = new Button("Initial");

        barreDeMenu = new ToolBar(getBoutonRetourMenu(), new Separator(), getBoutonCharger(), getBoutonSauvegarder(),
                new Separator(), getBoutonCreerEtat(), boutonSetInitial, getCheckBoxEstTerminal(), getBoutonClear(),
                getBoutonSupprimer(), hBoxAjoutTransition);

        initStyle();
        initListenersAndActions();

        setTop(barreDeMenu);
        setBottom(hBoxLancerMachine);
    }

    public HBox gethBoxAjoutTransition() {
        return hBoxAjoutTransition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VueMachine<TransitionMT> creerVueMachine() {
        vueMT = new VueMT(new MachineTuring(), this);
        return vueMT;
    }

    public VueMT getVueMT() {
        return vueMT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sauvegarder() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder machine Turing");

        Path path = Paths.get("./fichiers_machines");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MT", "*.mt"));
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getAbsolutePath();
                int index = selectedFile.getName().lastIndexOf('.');
                if (index < 0 || !selectedFile.getName().substring(index).equals(".mt")) {
                    fileName = fileName.concat(".mt");
                }

                vueMT.sauvegarder(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void charger() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Charger machine Turing");

        Path path = Paths.get("./fichiers_machines");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MT", "*.mt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                vueMT.chargerFichier(selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TextField getFieldNouvelleLettre() {
        return fieldNouvelleLettre;
    }

    public RadioButton getBoutonGauche() {
        return boutonGauche;
    }

    public RadioButton getBoutonDroite() {
        return boutonDroite;
    }

    public HBox gethBoxChoixMvmt() {
        return hBoxChoixMvmt;
    }

    public Button getBoutonStop() {
        return boutonStop;
    }

    private void initListenersAndActions() {
        MachineTuring mt = vueMT.getMachineTuring();

        mt.setListenerValueTaskLancer((observableValue, integer, t1) -> {
            textFlowRuban.getChildren().clear();
            String ruban = mt.getStringRuban();
            for (int i = 0; i < ruban.length(); i++) {
                Label lettre = new Label(String.valueOf(ruban.charAt(i)));
                lettre.setStyle("-fx-font-weight: bold; -fx-font-size: 19");
                if (i == mt.getTeteLecture())
                    lettre.setStyle("-fx-font-weight: bold; -fx-text-fill: #037fdb; -fx-font-size: 19");
                textFlowRuban.getChildren().add(lettre);
            }
        });

        mt.setOnRunning(workerStateEvent -> {
            boutonStop.setDisable(false);
            getSpinnerVitesse().setDisable(true);
        });
        mt.setOnCancelled(workerStateEvent -> {
            boutonStop.setDisable(true);
            getSpinnerVitesse().setDisable(false);
        });

        mt.setOnSucceeded(workerStateEvent -> {
            getSpinnerVitesse().setDisable(false);
            boutonStop.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultat");
            alert.setHeaderText(null);
            if (mt.motReconnu()) alert.setContentText("Le calcul est terminé et est valide");
            else alert.setContentText("Le calcul est terminé mais il n'est pas valide");
            alert.setHeaderText("Résultat obtenu : " + mt.getStringRuban());
            alert.showAndWait();
        });

        boutonStop.setOnAction(eventArreter);
        boutonSetInitial.setOnAction(eventSetInitial);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerTransitionsSelectionnees() {
        HashSet<VueTransition<TransitionMT>> vuesTransitionADeDelectionner = new HashSet<>();
        for (VueTransition<TransitionMT> vueTransition : vueMT.getVuesTransitionSelectionnes()) {
            if (vueMT.getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin())
                    .size() == 1) {
                vueTransition.getVueEtatDep().getEtat().supprimerTransition(vueTransition.getTransition());
                vuesTransitionADeDelectionner.add(vueTransition);
            }
        }

        vuesTransitionADeDelectionner.forEach(VueTransition::deSelectionner);

        if (vueMT.getVuesTransitionSelectionnes().size() > 0) {
            ObservableList<VueTransitionMT> vueTransitionMTs = FXCollections.observableArrayList();
            for (VueTransition<TransitionMT> vueTransition : vueMT.getVuesTransitionSelectionnes()) {
                if (vueTransition instanceof VueTransitionMT) vueTransitionMTs.add((VueTransitionMT) vueTransition);
            }

            StageSupTransMT stageSupTrans = new StageSupTransMT(vueTransitionMTs, getApp().getPrimaryStage());

            ArrayList<VueTransitionMT> transitionsASupprimer = stageSupTrans.showOpenDialog();
            for (VueTransitionMT vueTransition : transitionsASupprimer) {
                vueTransition.getVueEtatDep().getEtat().supprimerTransition(vueTransition.getTransition());
                vueTransition.deSelectionner();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancer() {
        MachineTuring mt = vueMT.getMachineTuring();
        String mot = getTextFieldMotAutomate().getText();
        long dellayMillis = Double.valueOf(getSpinnerVitesse().getValue() * 1000).longValue();
        mt.lancer(mot, dellayMillis);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajouterTransition() {
        ObservableList<VueEtat<TransitionMT>> vuesEtatSelectionnes = vueMT.getVuesEtatSelectionnes();
        if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problème ajout transition");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez sélectionner 1 ou 2 etats");
            alert.showAndWait();
        } else {
            String etiquette = getTextFieldEtiquette().getText();
            String nouvelleLettre = fieldNouvelleLettre.getText();
            VueEtat<TransitionMT> vueEtatDep = vuesEtatSelectionnes.get(0);
            VueEtat<TransitionMT> vueEtatArrivee;
            if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
            else vueEtatArrivee = vuesEtatSelectionnes.get(1);
            if (etiquette.length() >= 1) {
                boolean nouvelleTrans = true;
                for (TransitionMT t : vueMT.getMachineTuring().getTransitions()) {
                    if (t.getEtatDepart() == vueEtatDep.getEtat() && t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                            t.getEtiquette() == etiquette.charAt(0)) {
                        nouvelleTrans = false;
                        break;
                    }
                }
                if (nouvelleTrans) {
                    if (nouvelleLettre.length() < 1) nouvelleLettre = etiquette;
                    Mouvement mvmt = (boutonDroite.isSelected()) ? Mouvement.DROITE : Mouvement.GAUCHE;
                    vueEtatDep.getEtat().ajoutTransition(
                            new TransitionMT(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0),
                                    nouvelleLettre.charAt(0), mvmt));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajouterEtat() {
        vueMT.getMachineTuring().ajouterEtat(new Etat<>(false, getCheckBoxEstTerminal().isSelected()));
    }

    private void initStyle() {
        barreDeMenu.setStyle("-fx-spacing: 10");

        hBoxChoixMvmt.setAlignment(Pos.CENTER);
        hBoxChoixMvmt.setSpacing(5);
        hBoxChoixMvmt.setPadding(new Insets(0, 0, 0, 5));

        hBoxAjoutTransition.setAlignment(Pos.CENTER);

        hBoxLancerMachine.setAlignment(Pos.CENTER_RIGHT);
        hBoxLancerMachine.setPadding(new Insets(5));
        getSpinnerVitesse().setPrefWidth(75);

        boutonStop.setStyle("-fx-font-weight: bold");

        HBox.setHgrow(paneVide1, Priority.ALWAYS);
        HBox.setHgrow(paneVide2, Priority.ALWAYS);
    }
}























