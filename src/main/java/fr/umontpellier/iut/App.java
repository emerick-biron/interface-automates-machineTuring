package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.VuePrincipale;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello World!");
        VuePrincipale vuePrincipale = new VuePrincipale();

        primaryStage.setScene(new Scene(vuePrincipale, 300, 250));
        primaryStage.show();

        vuePrincipale.getVueAutomate().getAutomate().chargerFichier("automates_txt/input.txt");

    }
}