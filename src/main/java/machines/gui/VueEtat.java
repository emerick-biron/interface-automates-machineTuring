package machines.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

public abstract class VueEtat<VP extends VuePrincipale<VP, VM, VE, VT, M, E, T>, VM extends VueMachine<VP, VM, VE, VT, M, E, T>,
        VE extends VueEtat<VP, VM, VE, VT, M, E, T>, VT extends VueTransition<VP, VM, VE, VT, M, E, T>,
        M extends Machine<E, T>, E extends Etat<E, T>, T extends Transition<T, E>>
        extends StackPane {
    private E etat;
    private Circle cercle;
    private Label labelNumEtat;
    private VM vueMachine;
    private double mouseX;
    private double mouseY;
    private ImageView imageViewInitial;
    private ImageView imageViewTerminal;
    private BooleanProperty estSelectionne;

    public VueEtat(E etat, VM vueMachine) {
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

    public abstract void initListeners();

    public ImageView getImageViewInitial() {
        return imageViewInitial;
    }

    public ImageView getImageViewTerminal() {
        return imageViewTerminal;
    }

    public VM getVueMachine() {
        return vueMachine;
    }

    public E getEtat() {
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































