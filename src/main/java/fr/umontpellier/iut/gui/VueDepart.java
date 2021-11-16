package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VueDepart extends Stage {
    private Button boutonAutomate;
    private Button boutonTuring;
    private Label labelInformations;
    private HBox hBoxBoutons;
    private VBox vuePrincipale;
    private Scene scene;

    public VueDepart() {
        boutonAutomate = new Button("Automate");
        boutonTuring = new Button("Machine de Turing");
        labelInformations = new Label(getLabelInformation());

        hBoxBoutons = new HBox(boutonAutomate, boutonTuring);

        vuePrincipale = new VBox(labelInformations, hBoxBoutons);

        initStyle();

        setScene(new Scene(vuePrincipale));
        setWidth(600);
        setHeight(350);
        show();
    }

    public Button getBoutonAutomate() {
        return boutonAutomate;
    }

    private void initStyle(){
        vuePrincipale.setAlignment(Pos.CENTER);
        hBoxBoutons.setAlignment(Pos.CENTER);
        hBoxBoutons.setSpacing(20);
        vuePrincipale.setSpacing(30);
        labelInformations.setStyle("-fx-font-size: 15");
    }

    private String getLabelInformation(){
        return "Projet S3 \"Interface pour automates et machine de Turing\"\n\n"+
                "Membres :\n"+
                "  - Lénais Desbos\n"+
                "  - Alexandre Roussel\n"+
                "  - Albin Moret\n"+
                "  - Emerick Biron\n" +
                "Tuteur : Matthieu Rosenfeld";
    }
}
