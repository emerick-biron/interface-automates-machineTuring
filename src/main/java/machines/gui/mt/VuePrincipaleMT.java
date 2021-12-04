package machines.gui.mt;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import machines.App;
import machines.gui.VueEtat;
import machines.gui.VueMachine;
import machines.gui.VuePrincipale;
import machines.gui.automates.VueAutomate;
import machines.logique.automates.Automate;
import machines.logique.automates.TransitionAtmt;
import machines.logique.mt.MachineTuring;
import machines.logique.mt.Mouvement;
import machines.logique.mt.TransitionMT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VuePrincipaleMT extends VuePrincipale<TransitionMT> {
    private HBox barreDeMenu;
    private HBox hBoxLancerMachine;
    private HBox hBoxAjoutTransition;
    private HBox hBoxLabelsLettres;
    private TextField fieldNouvelleLette;
    private ToggleGroup groupChoixMvmt;
    private RadioButton boutonGauche;
    private RadioButton boutonDroite;
    private HBox hBoxChoixMvmt;
    private Button boutonArret;

    private FileChooser fileChooser;

    public VuePrincipaleMT(App app) {
        super(app);
        getTextFieldEtiquette().setPrefWidth(30);
        fieldNouvelleLette = new TextField();
        fieldNouvelleLette.setPrefWidth(30);

        groupChoixMvmt = new ToggleGroup();
        boutonDroite = new RadioButton("D");
        boutonGauche = new RadioButton("G");
        boutonDroite.setToggleGroup(groupChoixMvmt);
        boutonDroite.setSelected(true);
        boutonGauche.setToggleGroup(groupChoixMvmt);

        hBoxChoixMvmt = new HBox(boutonDroite, boutonGauche);

        hBoxAjoutTransition =
                new HBox(getBoutonAjouterTransition(), getTextFieldEtiquette(), fieldNouvelleLette, hBoxChoixMvmt);

        boutonArret = new Button("STOP");

        hBoxLancerMachine = new HBox(getBoutonLancer(), getTextFieldMotAutomate(), boutonArret);

        barreDeMenu = new HBox(getBoutonRetourMenu(), getBoutonCharger(), getBoutonSauvegarder(), getBoutonCreerEtat(),
                getCheckBoxEstInitial(), getCheckBoxEstTerminal(), getBoutonClear(), getBoutonSupprimer(),
                hBoxAjoutTransition);

        initStyle();

        setTop(barreDeMenu);
        setBottom(hBoxLancerMachine);
    }

    @Override
    public VueMachine<TransitionMT> creerVueMachine() {
        return new VueMT(new MachineTuring(), this);
    }

    @Override
    public void sauvegarder() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder machine Turing");

        Path path = Paths.get("./fichiers_machine");
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

                getVueMachine().sauvegarder(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void charger() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Charger machine Turing");

        Path path = Paths.get("./fichiers_machine");
        if (Files.isDirectory(path)) fileChooser.setInitialDirectory(path.toFile());
        else fileChooser.setInitialDirectory(new File("./"));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MT", "*.mt"));
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
    public void lancer() {

    }

    @Override
    public void ajouterTransition() {
        ObservableList<VueEtat<TransitionMT>> vuesEtatSelectionnes = getVueMachine().getVuesEtatSelectionnes();
        if (vuesEtatSelectionnes.size() > 2 || vuesEtatSelectionnes.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problème ajout transition");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez sélectionner 1 ou 2 etats");
            alert.showAndWait();
        } else {
            String etiquette = getTextFieldEtiquette().getText();
            VueEtat<TransitionMT> vueEtatDep = vuesEtatSelectionnes.get(0);
            VueEtat<TransitionMT> vueEtatArrivee;
            if (vuesEtatSelectionnes.size() == 1) vueEtatArrivee = vueEtatDep;
            else vueEtatArrivee = vuesEtatSelectionnes.get(1);
            if (etiquette.length() >= 1) {
                boolean nouvelleTrans = true;
                for (TransitionMT t : getVueMachine().getMachine().getTransitions()) {
                    if (t.getEtatDepart() == vueEtatDep.getEtat() && t.getEtatArrivee() == vueEtatArrivee.getEtat() &&
                            t.getEtiquette() == etiquette.charAt(0)) {
                        nouvelleTrans = false;
                        break;
                    }
                }
                if (nouvelleTrans) {
                    Mouvement mvmt = (boutonDroite.isSelected()) ? Mouvement.DROITE : Mouvement.GAUCHE;
                    vueEtatDep.getEtat().ajoutTransition(
                            new TransitionMT(vueEtatDep.getEtat(), vueEtatArrivee.getEtat(), etiquette.charAt(0), fieldNouvelleLette.getText().charAt(0),
                                    mvmt));
                }
            }
        }
    }

    private void initStyle() {
        barreDeMenu.setSpacing(10);
        barreDeMenu.setAlignment(Pos.CENTER_LEFT);

        hBoxChoixMvmt.setAlignment(Pos.CENTER);
        hBoxChoixMvmt.setSpacing(5);
        hBoxChoixMvmt.setPadding(new Insets(0,0,0,5));

        hBoxAjoutTransition.setAlignment(Pos.CENTER);

        hBoxLancerMachine.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLancerMachine.setPadding(new Insets(0, 10, 10, 0));

        boutonArret.setStyle("-fx-font-weight: bold");
    }
}























