package fr.umontpellier.iut;

import fr.umontpellier.iut.composants.VueEtat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        Pane root = new Pane();

        VueEtat vueEtat = new VueEtat();

        root.getChildren().add(vueEtat);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}