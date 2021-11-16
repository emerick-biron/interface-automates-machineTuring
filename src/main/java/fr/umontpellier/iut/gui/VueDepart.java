package fr.umontpellier.iut.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VueDepart extends Stage {
    private Button boutonAutomate;
    private Button boutonTuring;
    private Label labelInformations;
    private HBox hBoxAutomateMT;
    private VBox vuePrincipale;
    private Scene scene;
    private ComboBox<String> comboBoxChoixAutomate;
    private VBox vBoxLancementAutomate;

    public VueDepart() {
        boutonAutomate = new Button("Automate");
        boutonTuring = new Button("Machine de Turing");
        labelInformations = new Label(getLabelInformation());

        initComboBox();

        vBoxLancementAutomate = new VBox(boutonAutomate, comboBoxChoixAutomate);

        hBoxAutomateMT = new HBox(vBoxLancementAutomate, boutonTuring);

        vuePrincipale = new VBox(labelInformations, hBoxAutomateMT);

        initStyle();

        setScene(new Scene(vuePrincipale));
        setWidth(600);
        setHeight(350);
        show();
    }

    public Button getBoutonAutomate() {
        return boutonAutomate;
    }

    public ComboBox<String> getComboBoxChoixAutomate() {
        return comboBoxChoixAutomate;
    }

    private void initStyle() {
        vuePrincipale.setAlignment(Pos.CENTER);
        hBoxAutomateMT.setAlignment(Pos.CENTER);
        hBoxAutomateMT.setSpacing(20);
        vuePrincipale.setSpacing(30);
        labelInformations.setStyle("-fx-font-size: 15");
        vBoxLancementAutomate.setSpacing(10);
        vBoxLancementAutomate.setAlignment(Pos.CENTER);
    }

    private void initComboBox() {
        comboBoxChoixAutomate = new ComboBox<>();
        ObservableList<String> choixPossible = FXCollections.observableArrayList();
        choixPossible.add("nouveau");
        Path path = Paths.get("./automates_atmt/default");
        if (Files.isDirectory(path)) {
            File dossierParDefaut = path.toFile();
            for (File file : dossierParDefaut.listFiles()) {
                if (file.isFile()) choixPossible.add(file.getName().split("\\.")[0]);
            }
        }
        comboBoxChoixAutomate.setItems(choixPossible);
        comboBoxChoixAutomate.getSelectionModel().selectFirst();
    }

    private String getLabelInformation() {
        return "Projet S3 \"Interface pour automates et machine de Turing\"\n\n" + "Membres :\n" +
                "  - Lénais Desbos\n" + "  - Alexandre Roussel\n" + "  - Albin Moret\n" + "  - Emerick Biron\n" +
                "Tuteur : Matthieu Rosenfeld";
    }
}
