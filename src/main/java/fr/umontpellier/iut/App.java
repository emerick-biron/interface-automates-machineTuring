package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.StageSupTrans;
import fr.umontpellier.iut.gui.VueAutomate;
import fr.umontpellier.iut.gui.VueEtat;
import fr.umontpellier.iut.gui.VuePrincipale;
import fr.umontpellier.iut.logique.Etat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Automates");
        VuePrincipale vuePrincipale = new VuePrincipale();

        primaryStage.setScene(new Scene(vuePrincipale));
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);

        primaryStage.show();
    }
}