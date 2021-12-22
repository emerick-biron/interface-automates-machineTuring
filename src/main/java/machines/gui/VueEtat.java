package machines.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import machines.logique.Etat;
import machines.logique.Transition;

public class VueEtat<T extends Transition<T>> extends StackPane {
    private Etat<T> etat;
    private Circle cercle;
    private Label labelNumEtat;
    private VueMachine<T> vueMachine;
    private double mouseX;
    private double mouseY;
    private ImageView imageViewInitial;
    private ImageView imageViewTerminal;
    private BooleanProperty estSelectionne;
    private ChangeListener<Boolean> changementActivationEtat = (observableValue, ancienneValeur, nouvelleValeur) -> {
        if (observableValue.getValue()) getCercle().setFill(Color.valueOf("#079423"));
        else getCercle().setFill(Color.valueOf("#037fdb"));
    };
    private ChangeListener<Boolean> changementEstInitial = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(imageViewInitial);
        else getChildren().remove(imageViewInitial);
    };
    private ChangeListener<Boolean> changementEstTerminal = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(imageViewTerminal);
        else getChildren().remove(imageViewTerminal);
    };
    private ChangeListener<Boolean> changementSelection = (observableValue, aBoolean, t1) -> {
        if (aBoolean != t1) {
            if (observableValue.getValue()) {
                vueMachine.getVuesEtatSelectionnes().add(VueEtat.this);
            } else {
                vueMachine.getVuesEtatSelectionnes().remove(VueEtat.this);
            }
        }
    };
    private SetChangeListener<T> miseAJourTransitions = change -> {
        if (change.wasAdded()) {
            T t = change.getElementAdded();
            vueMachine.ajoutVueTransition(t);
        } else if (change.wasRemoved()) {
            T t = change.getElementRemoved();
            vueMachine.supprimerVueTransition(t);
        }
    };

    public VueEtat(Etat<T> etat, VueMachine<T> vueMachine) {
        this.vueMachine = vueMachine;
        this.etat = etat;
        estSelectionne = new SimpleBooleanProperty(false);
        labelNumEtat = new Label();
        imageViewInitial = new ImageView("etat_initial.png");
        imageViewTerminal = new ImageView("etat_terminal.png");
        cercle = new Circle(50, 50, 46, Color.valueOf("#037fdb"));

        getChildren().add(cercle);
        init();
    }

    public ImageView getImageViewInitial() {
        return imageViewInitial;
    }

    public ImageView getImageViewTerminal() {
        return imageViewTerminal;
    }

    private void initListeners() {
        getEtat().estActifProperty().addListener(changementActivationEtat);
        getEtat().estInitialProperty().addListener(changementEstInitial);
        getEtat().estTerminalProperty().addListener(changementEstTerminal);
        estSelectionneProperty().addListener(changementSelection);
        getEtat().transitionsProperty().addListener(miseAJourTransitions);
    }

    public VueMachine<T> getVueMachine() {
        return vueMachine;
    }

    public Etat<T> getEtat() {
        return etat;
    }

    public Label getLabelNumEtat() {
        return labelNumEtat;
    }

    public void setLabelNumEtat(int numEtat) {
        labelNumEtat.setText(String.valueOf(numEtat));
    }

    public int getNumEtat() {
        return Integer.parseInt(labelNumEtat.getText());
    }

    private void initMouseEvents() {
        setOnMousePressed(mouseEvent -> {
            if (!vueMachine.getVuePrincipale().ctrlPresse()) {
                vueMachine.deSelectionnerVues();
            }
            if (estSelectionne()) {
                deSelectionner();
            } else {
                selectionner();
            }

            vueMachine.setPrefSize(vueMachine.getWidth(), vueMachine.getHeight());

            vueMachine.getVuePrincipale().unbindCheckBoxes();

            CheckBox cbEstInitial = vueMachine.getVuePrincipale().getCheckBoxEstInitial();
            CheckBox cbEstTerminal = vueMachine.getVuePrincipale().getCheckBoxEstTerminal();

            cbEstInitial.selectedProperty().bindBidirectional(etat.estInitialProperty());
            cbEstTerminal.selectedProperty().bindBidirectional(etat.estTerminalProperty());

            setCursor(Cursor.MOVE);
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
        });

        setOnMouseReleased(mouseEvent -> {
            setCursor(Cursor.HAND);
            vueMachine.setPrefSize(-1, -1);
        });

        setOnMouseDragged(mouseEvent -> {
            //Permet de deplacer la vueEtat a la souris
            double deltaX = mouseEvent.getSceneX() - mouseX;
            double deltaY = mouseEvent.getSceneY() - mouseY;
            double newXpos = getLayoutX() + deltaX;
            double newYPos = getLayoutY() + deltaY;

            //Permet de faire en sorte que la vue etat ait un X négatif
            if (newXpos >= 0) {
                setLayoutX(newXpos);
                mouseX = mouseEvent.getSceneX();
            }
            //Permet de faire en sorte que la vue etat ait un Y négatif
            if (newYPos >= 0) {
                setLayoutY(newYPos);
                mouseY = mouseEvent.getSceneY();
            }
        });

        setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
    }

    private void initLabelNumEtat() {
        labelNumEtat.setStyle(" -fx-font-size: " + cercle.getRadius() / 2 + ";-fx-font-weight: bold");
        getChildren().add(labelNumEtat);
    }

    private void init() {
        initListeners();
        initMouseEvents();
        initLabelNumEtat();
        initImages();
    }

    private void initImages() {
        imageViewInitial.setFitWidth(15);
        imageViewInitial.setPreserveRatio(true);
        imageViewInitial.setTranslateX(-40);
        imageViewInitial.setTranslateY(-40);
        imageViewTerminal.setFitWidth(15);
        imageViewTerminal.setPreserveRatio(true);
        imageViewTerminal.setTranslateX(40);
        imageViewTerminal.setTranslateY(-40);
        if (etat.estInitial()) getChildren().add(imageViewInitial);
        if (etat.estTerminal()) getChildren().add(imageViewTerminal);

    }

    public Circle getCercle() {
        return cercle;
    }

    public boolean estSelectionne() {
        return estSelectionne.get();
    }

    public BooleanProperty estSelectionneProperty() {
        return estSelectionne;
    }

    public void selectionner() {
        this.estSelectionne.set(true);
    }

    public void deSelectionner() {
        this.estSelectionne.set(false);
    }
}