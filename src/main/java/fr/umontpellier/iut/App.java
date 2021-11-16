package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.VueDepart;
import fr.umontpellier.iut.gui.VuePrincipale;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App extends Application {
    private Stage primaryStage;
    private VueDepart vueDepart;
    private EventHandler<ActionEvent> eventLancerAutomate = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueDepart.hide();

            VuePrincipale vuePrincipale = new VuePrincipale(App.this);

            primaryStage.setScene(new Scene(vuePrincipale));
            primaryStage.setWidth(1200);
            primaryStage.setHeight(750);
            primaryStage.setTitle("Automates");

            primaryStage.show();

            Thread thread = new Thread(() -> {
                String choix = vueDepart.getComboBoxChoixAutomate().getValue();

                Path path = Paths.get("./automates_atmt/default/" + choix + ".atmt");
                File file = path.toFile();
                if (!choix.equals("nouveau") && file.isFile()) {
                    try {
                        vuePrincipale.getVueAutomate().chargerFichier(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Platform.runLater(thread);
        }
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
    }
}