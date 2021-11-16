package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.VueDepart;
import fr.umontpellier.iut.gui.VuePrincipale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;
    private VueDepart vueDepart;
    private EventHandler<ActionEvent> eventLancerAutomate = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            vueDepart.hide();

            primaryStage.setScene(new Scene(new VuePrincipale(App.this)));
            primaryStage.setWidth(1200);
            primaryStage.setHeight(750);
            primaryStage.setTitle("Automates");

            primaryStage.show();
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