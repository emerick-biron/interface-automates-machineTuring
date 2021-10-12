package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Etat;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VueEtat extends StackPane {
    private Etat etat;
    private Circle cercle;
    private Label labelNumEtat;
    private VueAutomate vueAutomate;
    private double mouseX;
    private double mouseY;

    public VueEtat(Etat etat, Label labelNumEtat, VueAutomate vueAutomate) {
        this.vueAutomate = vueAutomate;
        this.etat = etat;
        this.labelNumEtat = labelNumEtat;
        cercle = new Circle(50, 50, 50, Color.RED);
        getChildren().add(cercle);
        init();
    }

    public VueEtat(Etat etat, VueAutomate vueAutomate) {
        this.vueAutomate = vueAutomate;
        this.etat = etat;
        labelNumEtat = new Label();
        cercle = new Circle(50, 50, 50, Color.RED);
        getChildren().add(cercle);
        init();
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
            if (newXpos >= 0 && newXpos + getWidth() <= getParent().getBoundsInLocal().getMaxX()) {
                setLayoutX(newXpos);
                mouseX = mouseEvent.getSceneX();
            }
            //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
            if (newYPos >= 0 && newYPos + getHeight() <= getParent().getBoundsInLocal().getMaxY()) {
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
    }
}
