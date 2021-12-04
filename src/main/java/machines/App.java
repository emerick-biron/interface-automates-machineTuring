package machines;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import machines.gui.VueDepart;
import machines.gui.VuePrincipale;
import machines.gui.automates.VuePrincipaleAtmt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import machines.gui.mt.VuePrincipaleMT;
import machines.logique.Transition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class App extends Application {
    private Stage primaryStage;
    private VueDepart vueDepart;
    private VuePrincipale<? extends Transition<?>> vuePrincipale;
    private EventHandler<ActionEvent> eventLancerAutomate = actionEvent -> {
        vueDepart.hide();
        startAutomate();
    };
    private EventHandler<ActionEvent> eventLancerMT = actionEvent -> {
        vueDepart.hide();
        startMT();
    };


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public VueDepart getVueDepart() {
        return vueDepart;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        vueDepart = new VueDepart();
        vueDepart.setTitle("Interface pour automates et machine de Turing");
        vueDepart.getBoutonAutomate().setOnAction(eventLancerAutomate);
        vueDepart.getBoutonTuring().setOnAction(eventLancerMT);
    }

    public void startAutomate() {
        vuePrincipale = new VuePrincipaleAtmt(App.this);

        primaryStage.setScene(new Scene(vuePrincipale));
        primaryStage.setWidth(1200);
        primaryStage.setHeight(750);
        primaryStage.setTitle("Automates");

        primaryStage.setOnCloseRequest(windowEvent -> {
            arretMachine();
            windowEvent.consume();
        });
        vuePrincipale.getBoutonRetourMenu().setOnAction(actionEvent -> retourMenu());

        primaryStage.show();

        String choix = vueDepart.getComboBoxChoixAutomate().getValue();

        Path path = Paths.get("./fichiers_machine/" + choix + ".atmt");
        File file = path.toFile();
        if (!choix.equals("nouveau") && file.isFile()) {
            try {
                vuePrincipale.getVueMachine().chargerFichier(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void startMT() {
        vuePrincipale = new VuePrincipaleMT(App.this);

        primaryStage.setScene(new Scene(vuePrincipale));
        primaryStage.setWidth(1200);
        primaryStage.setHeight(750);
        primaryStage.setTitle("Machine de Turing");

        primaryStage.setOnCloseRequest(windowEvent -> {
            arretMachine();
            windowEvent.consume();
        });
        vuePrincipale.getBoutonRetourMenu().setOnAction(actionEvent -> retourMenu());

        primaryStage.show();

        String choix = vueDepart.getComboBoxChoixMT().getValue();

        Path path = Paths.get("./fichiers_machine/" + choix + ".mt");
        File file = path.toFile();
        if (!choix.equals("nouveau") && file.isFile()) {
            try {
                vuePrincipale.getVueMachine().chargerFichier(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void arretMachine() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sauvegarder avant de quitter ?");
        alert.setHeaderText("Voulez vous sauvegarder avant la fermeture ?");
        alert.setContentText("Vos modifications seront perdues si vous ne les enregistrez pas.");
        alert.getButtonTypes().clear();

        ButtonType boutonQuitter = new ButtonType("Quitter");
        ButtonType boutonAnnuler = new ButtonType("Annuler");
        ButtonType boutonSauvegarder = new ButtonType("Sauvegarder");

        alert.getButtonTypes().addAll(boutonQuitter, boutonAnnuler, boutonSauvegarder);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == boutonQuitter) {
            Platform.exit();
        } else if (result.isPresent() && result.get() == boutonSauvegarder){
            vuePrincipale.sauvegarder();
            Platform.exit();
        }
    }

    public void retourMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sauvegarder avant retour menu ?");
        alert.setHeaderText("Voulez vous sauvegarder avant de retourner au menu ?");
        alert.setContentText("Vos modifications seront perdues si vous ne les enregistrez pas.");
        alert.getButtonTypes().clear();

        ButtonType boutonMenu = new ButtonType("Menu");
        ButtonType boutonAnnuler = new ButtonType("Annuler");
        ButtonType boutonSauvegarder = new ButtonType("Sauvegarder");

        alert.getButtonTypes().addAll(boutonMenu, boutonAnnuler, boutonSauvegarder);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == boutonMenu) {
            primaryStage.close();
            vueDepart.show();
        } else if (result.isPresent() && result.get() == boutonSauvegarder){
            vuePrincipale.sauvegarder();
            primaryStage.close();
            vueDepart.show();
        }
    }
}






















