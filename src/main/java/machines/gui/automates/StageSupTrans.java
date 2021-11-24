package machines.gui.automates;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class StageSupTrans extends Stage {
    private ScrollPane scrollPane;
    private VBox contenuScrollPane;
    private VBox contenuPrincipal;
    private HashMap<VueTransitionAtmt, CheckBox> transitionAvecCB;
    private CheckBox cbToutSelectionner;
    private Label labelInformations;
    private Button boutonSupprimer;
    private Button boutonAnnuler;
    private HBox hBoxBouton;
    private HBox hBoxToutSelect;
    private HBox hBoxInfo;
    private Label labelToutSelect;
    private ArrayList<VueTransitionAtmt> transitionsASupprimmer;
    private ChangeListener<Boolean> changementCbToutSelect = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (observableValue.getValue()) {
                for (CheckBox cb : transitionAvecCB.values()) {
                    cb.selectedProperty().set(true);
                }
            } else {
                for (CheckBox cb : transitionAvecCB.values()) {
                    cb.selectedProperty().set(false);
                }
            }
        }
    };
    private EventHandler<ActionEvent> eventSupprimer = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            for (VueTransitionAtmt key : transitionAvecCB.keySet() ) {
                if (transitionAvecCB.get(key).selectedProperty().get()) transitionsASupprimmer.add(key);
            }
            close();
        }
    };

    public StageSupTrans(ObservableList<VueTransitionAtmt> vuesTransition, Stage primaryStage) {
        transitionAvecCB = new HashMap<>();
        for (VueTransitionAtmt vueTransition : vuesTransition) {
            CheckBox cb = new CheckBox();
            transitionAvecCB.put(vueTransition, cb);
        }

        transitionsASupprimmer = new ArrayList<>();
        scrollPane = createScrollPane();
        cbToutSelectionner = new CheckBox();
        cbToutSelectionner.selectedProperty().addListener(changementCbToutSelect);
        labelInformations = new Label("Sélectionnez les transitions à supprimer :");
        hBoxInfo = new HBox(labelInformations);
        boutonAnnuler = new Button("Annuler");
        boutonAnnuler.setOnAction(actionEvent -> close());
        boutonSupprimer = new Button("Supprimer");
        boutonSupprimer.setOnAction(eventSupprimer);
        hBoxBouton = new HBox(boutonAnnuler, boutonSupprimer);
        labelToutSelect = new Label("Tout sélectionner");
        hBoxToutSelect = new HBox(labelToutSelect, cbToutSelectionner);

        contenuPrincipal = new VBox(hBoxInfo, hBoxToutSelect, scrollPane, hBoxBouton);

        initStyle();
        setTitle("Suppression transitions");
        setScene(new Scene(contenuPrincipal));
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        setWidth(500);
        setHeight(400);
    }

    private void initStyle() {
        scrollPane.maxHeight(400);

        hBoxToutSelect.setSpacing(15);
        hBoxToutSelect.setStyle("-fx-padding: 20 0 5 20");
        labelToutSelect.setStyle("-fx-font-size: 14");


        hBoxInfo.setAlignment(Pos.TOP_CENTER);
        labelInformations.setStyle("-fx-font-weight: bold;-fx-font-size: 16 ;-fx-padding: 20 0 0 0");

        hBoxBouton.setStyle("-fx-padding: 20");
        hBoxBouton.setSpacing(15);
        hBoxBouton.setAlignment(Pos.BOTTOM_RIGHT);
    }

    public ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();

        contenuScrollPane = new VBox();

        for (VueTransitionAtmt key : transitionAvecCB.keySet()) {
            Label label = new Label(
                    key.getVueEtatDep().getLabelNumEtat().getText() + key.getLabelEtiquette().getText() +
                            key.getVueEtatFin().getLabelNumEtat().getText());
            HBox hBox = new HBox(transitionAvecCB.get(key), label);
            hBox.setSpacing(15);
            contenuScrollPane.getChildren().add(hBox);
        }

        scrollPane.setContent(contenuScrollPane);

        return scrollPane;
    }

    public ArrayList<VueTransitionAtmt> showOpenDialog(){
        showAndWait();
        return transitionsASupprimmer;
    }
}























