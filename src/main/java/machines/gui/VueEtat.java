package machines.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import machines.logique.Etat;

public class VueEtat extends StackPane {
    private Etat etat;
    private Circle cercle;
    private Label labelNumEtat;
    private VueMachine vueMachine;
    private double mouseX;
    private double mouseY;
    private ImageView imageViewInitial;
    private ImageView imageViewTerminal;
    private BooleanProperty estSelectionne;
    private ChangeListener<Boolean> changementActivationEtat = (observableValue, ancienneValeur, nouvelleValeur) -> {
        if (observableValue.getValue()) getCercle().setFill(Color.GREEN);
        else getCercle().setFill(Color.RED);
    };
    private ChangeListener<Boolean> changementEstInitial = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(getImageViewInitial());
        else getChildren().remove(getImageViewInitial());
    };
    private ChangeListener<Boolean> changementEstTerminal = (observableValue, aBoolean, t1) -> {
        if (observableValue.getValue()) getChildren().add(getImageViewTerminal());
        else getChildren().remove(getImageViewTerminal());
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

    public VueEtat(Etat etat, VueMachine vueMachine) {
        this.vueMachine = vueMachine;
        this.etat = etat;
        estSelectionne = new SimpleBooleanProperty(false);
        labelNumEtat = new Label();
        imageViewInitial = new ImageView("etat_initial.png");
        imageViewTerminal = new ImageView("etat_terminal.png");
        cercle = new Circle(50, 50, 46, Color.RED);

        getChildren().add(cercle);
        init();
    }

    public ImageView getImageViewInitial() {
        return imageViewInitial;
    }

    public ImageView getImageViewTerminal() {
        return imageViewTerminal;
    }

    public void initListeners() {
        getEtat().estActifProperty().addListener(changementActivationEtat);
        getEtat().estInitialProperty().addListener(changementEstInitial);
        getEtat().estTerminalProperty().addListener(changementEstTerminal);
        estSelectionneProperty().addListener(changementSelection);
    }

    public VueMachine getVueMachine() {
        return vueMachine;
    }

    public Etat getEtat() {
        return etat;
    }

    public Label getLabelNumEtat() {
        return labelNumEtat;
    }

    public void setLabelNumEtat(int numEtat) {
        labelNumEtat.setText(String.valueOf(numEtat));
    }

    public void initMouseEvents() {
        setOnMousePressed(mouseEvent -> {
            if (!vueMachine.getVuePrincipale().ctrlPresse()) {
                vueMachine.deSelectionnerVues();
            }
            if (estSelectionne()) {
                deSelectionner();
            } else {
                selectionner();
            }

            vueMachine.getVuePrincipale().unbindCheckBoxes();

            CheckBox cbEstInitial = vueMachine.getVuePrincipale().getCheckBoxEstInitial();
            CheckBox cbEstTerminal = vueMachine.getVuePrincipale().getCheckBoxEstTerminal();

            cbEstInitial.selectedProperty().bindBidirectional(etat.estInitialProperty());
            cbEstTerminal.selectedProperty().bindBidirectional(etat.estTerminalProperty());

            setCursor(Cursor.MOVE);
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
        });

        setOnMouseReleased(mouseEvent -> setCursor(Cursor.HAND));

        setOnMouseDragged(mouseEvent -> {
            //Permet de deplacer la vueEtat a la souris
            double deltaX = mouseEvent.getSceneX() - mouseX;
            double deltaY = mouseEvent.getSceneY() - mouseY;
            double newXpos = getLayoutX() + deltaX;
            double newYPos = getLayoutY() + deltaY;

            //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
            if (newXpos >= 0 && newXpos + getWidth() <= vueMachine.getBoundsInLocal().getMaxX()) {
                setLayoutX(newXpos);
                mouseX = mouseEvent.getSceneX();
            }
            //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
            if (newYPos >= 0 && newYPos + getHeight() <= vueMachine.getBoundsInLocal().getMaxY() - 25) {
                setLayoutY(newYPos);
                mouseY = mouseEvent.getSceneY();
            }
        });

        setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
    }

    public void initLabelNumEtat() {
        labelNumEtat.setStyle(" -fx-font-size: " + cercle.getRadius() / 2 + ";-fx-font-weight: bold");
        getChildren().add(labelNumEtat);
    }

    public void init() {
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































