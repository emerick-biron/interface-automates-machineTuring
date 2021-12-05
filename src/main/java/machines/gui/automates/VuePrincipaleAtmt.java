package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import machines.App;
import machines.gui.VueEtat;
import machines.gui.VueMachine;
import machines.gui.VueTransition;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import javafx.collections.ObservableList;
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

public class VuePrincipaleAtmt extends VuePrincipale<TransitionAtmt> {
    private ToolBar barreDeMenu;
    private HBox hBoxLancerAutomate;
    private HBox hBoxAjoutTransition;
    private TextFlow textFlowMot;

    private VueAutomate vueAutomate;

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

    public VuePrincipaleAtmt(App app) {
        super(app);

        getTextFieldEtiquette().setTextFormatter(new TextFormatter<>(textFilterAjoutTransition));
        getTextFieldEtiquette().setPrefWidth(30);

        textFlowMot = new TextFlow();

        hBoxAjoutTransition = new HBox(getBoutonAjouterTransition(), getTextFieldEtiquette());
        hBoxLancerAutomate = new HBox(textFlowMot, getBoutonLancer(), getTextFieldMotAutomate());

        barreDeMenu = new ToolBar(getBoutonRetourMenu(), new Separator(), getBoutonCharger(), getBoutonSauvegarder(),
                new Separator(), getBoutonCreerEtat(), getCheckBoxEstInitial(), getCheckBoxEstTerminal(),
                getBoutonClear(), getBoutonSupprimer(), hBoxAjoutTransition);

        initStyle();
        initListeners();

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

            StageSupTrans stageSupTrans = new StageSupTrans(vueTransitionAtmts, getApp().getPrimaryStage());

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
                    if (t.getEtatDepart() == vueEtatDep.getEtat() && t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                            t.getEtiquette() == etiquette.charAt(0)) {
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

    public void initListeners() {
        Automate automate = vueAutomate.getAutomate();

        ChangeListener<Number> listenerIndex = (observableValue, integer, t1) -> {
            if (textFlowMot.getChildren().size() > t1.intValue()) {
                textFlowMot.getChildren().get(t1.intValue())
                        .setStyle("-fx-font-weight: bold; -fx-text-fill: #037fdb; -fx-font-size: 19");
            }
        };

        automate.indexLettreCouranteProperty().addListener(listenerIndex);

        automate.setOnRunning(workerStateEvent -> {
            String mot = automate.getMot();
            for (int i = 0; i < mot.length(); i++) {
                Label lettre = new Label(String.valueOf(mot.charAt(i)));
                lettre.setStyle("-fx-font-weight: bold; -fx-font-size: 19");
                if (i == 0) lettre.setStyle("-fx-font-weight: bold; -fx-text-fill: #037fdb; -fx-font-size: 19");
                textFlowMot.getChildren().add(lettre);
            }
        });

        automate.setOnCancelled(workerStateEvent -> {
            textFlowMot.getChildren().clear();
        });

        automate.setOnSucceeded(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultat");
            alert.setHeaderText(null);
            if (automate.motReconnu()) alert.setContentText("Mot reconnu");
            else alert.setContentText("Mot non reconnu");
            alert.showAndWait();
            textFlowMot.getChildren().clear();
        });
    }

    @Override
    public void lancer() {
        //TODO Faire des tests pour voir si les entrees sont ok
        Automate automate = vueAutomate.getAutomate();
        String mot = getTextFieldMotAutomate().getText();
        automate.lancer(mot, 1000);
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
        hBoxLancerAutomate.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLancerAutomate.setPadding(new Insets(0, 10, 10, 0));
        textFlowMot.setPadding(new Insets(0, 20, 0, 0));
    }

    public HBox gethBoxAjoutTransition() {
        return hBoxAjoutTransition;
    }
}
