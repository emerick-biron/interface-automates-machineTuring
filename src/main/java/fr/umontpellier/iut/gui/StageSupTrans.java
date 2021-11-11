package fr.umontpellier.iut.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class StageSupTrans extends Stage {
    private ArrayList<VueTransition> vuesTransition;
    private ScrollPane scrollPane;
    private VBox contenuScrollPane;
    private VBox contenuPrincipal;
    private HashMap<VueTransition, CheckBox> resultVuesTransition;
    private CheckBox cbToutSelectionner;
    private Label labelInformations;
    private Button boutonSupprimer;
    private Button boutonAnnuler;
    private HBox hBoxBouton;
    private HBox hBoxToutSelect;
    private HBox hBoxInfo;
    private Label labelToutSelect;

    public StageSupTrans(ArrayList<VueTransition> vuesTransition) {
        this.vuesTransition = vuesTransition;
        cbToutSelectionner = new CheckBox();
        scrollPane = getScrollPane();
        labelInformations = new Label("Sélectionnez les transitions à supprimer :");
        hBoxInfo = new HBox(labelInformations);
        boutonAnnuler = new Button("Annuler");
        boutonSupprimer = new Button("Supprimer");
        hBoxBouton = new HBox(boutonAnnuler, boutonSupprimer);
        labelToutSelect = new Label("Tout sélectionner");
        hBoxToutSelect = new HBox(labelToutSelect, cbToutSelectionner);

        contenuPrincipal = new VBox(hBoxInfo, hBoxToutSelect, scrollPane, hBoxBouton);

        initStyle();
        setTitle("Suppression transitions");
        setScene(new Scene(contenuPrincipal));
        setWidth(500);
        setHeight(250);
    }

    private void initStyle() {

        hBoxToutSelect.setSpacing(15);
        hBoxToutSelect.setStyle("-fx-padding: 20 0 5 20");
        labelToutSelect.setStyle("-fx-font-size: 14");


        hBoxInfo.setAlignment(Pos.TOP_CENTER);
        labelInformations.setStyle("-fx-font-weight: bold;-fx-font-size: 16 ;-fx-padding: 20 0 0 0");

        hBoxBouton.setStyle("-fx-padding: 20");
        hBoxBouton.setSpacing(15);
        hBoxBouton.setAlignment(Pos.BOTTOM_RIGHT);
    }

    public ScrollPane getScrollPane() {
        ScrollPane scrollPane = new ScrollPane();

        contenuScrollPane = new VBox();
        resultVuesTransition = new HashMap<>();

        for (VueTransition vueTransition : vuesTransition) {
            CheckBox cb = new CheckBox();
            Label label = new Label("Salut");
            HBox hBox = new HBox(cb, label);
            contenuScrollPane.getChildren().add(hBox);
            resultVuesTransition.put(vueTransition, cb);
        }

        scrollPane.setContent(contenuScrollPane);

        return scrollPane;
    }
}























