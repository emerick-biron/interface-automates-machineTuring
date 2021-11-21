package machines.automates.gui;

import machines.App;
import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
                getVueAutomate().sauvegarder(selectedFile.getAbsolutePath());
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
            if (getVueAutomate().getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin()).size() ==
                    1) {
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
                    if (t.getEtatDepart() == vueEtatDep.getEtat() &&
                            t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                            t.getEtiquette() == etiquette.charAt(0)) {
                        nouvelleTrans = false;
                        break;
                    }
                }
                if (nouvelleTrans) {
                    getVueAutomate().getAutomate().ajoutTransition(
                            new TransitionAtmt(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(),
                                    etiquette.charAt(0)));
                }
            }
            getTextFieldEtiquette().setText("");
        }
    };

    public VuePrincipaleAtmt(App app) {
        super(app);

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
        setBottom(hBoxLancerAutomate);
    }

    @Override
    public VueAutomate getVueMachine() {
        return getVueAutomate();
    }

    public VueAutomate getVueAutomate(){
        return vueAutomate;
    }

    @Override
    public void initSetOnAction() {
        super.initSetOnAction();
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
