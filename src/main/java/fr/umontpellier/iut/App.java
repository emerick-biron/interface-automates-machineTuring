package fr.umontpellier.iut;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;

public class App extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        primaryStage.setTitle("Automates");
    }
}