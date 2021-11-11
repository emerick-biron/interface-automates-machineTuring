package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.VuePrincipale;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage primStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimStage() {
        return primStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primStage = primaryStage;
        primaryStage.setTitle("Automates");
        VuePrincipale vuePrincipale = new VuePrincipale();

        primaryStage.setScene(new Scene(vuePrincipale));
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);

        primaryStage.show();
    }
}