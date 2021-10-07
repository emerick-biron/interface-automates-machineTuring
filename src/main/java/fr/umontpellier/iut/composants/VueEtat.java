package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Etat;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class VueEtat extends StackPane {
    private Etat etat;
    private Circle cercle;
    private double mouseX;
    private double mouseY;

    public Etat getEtat() {
        return etat;
    }


    public VueEtat(Etat etat) {
        this.etat = etat;
        cercle = new Circle(50, 50, 50, Color.RED);
        getChildren().add(cercle);
        getChildren().add(new Label("10"));
        init();
    }

    public void init() {
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
}
