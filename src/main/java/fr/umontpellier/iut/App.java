package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.VuePrincipale;
import javafx.application.Application;
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
        Pane root = new VuePrincipale();

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}