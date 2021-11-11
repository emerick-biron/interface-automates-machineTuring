package fr.umontpellier.iut;

import fr.umontpellier.iut.gui.StageSupTrans;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Test extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StageSupTrans stageSupTrans = new StageSupTrans(new ArrayList<>());
        stageSupTrans.showAndWait();
    }
}
