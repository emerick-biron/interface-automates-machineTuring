package fr.umontpellier.iut;

import fr.umontpellier.iut.vues.VueEtat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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