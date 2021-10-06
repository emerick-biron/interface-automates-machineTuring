package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Etat;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
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
            double deltaX = mouseEvent.getSceneX() - mouseX;
            double deltaY = mouseEvent.getSceneY() - mouseY;
            relocate(getLayoutX() + deltaX, getLayoutY() + deltaY);
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();

        }); setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
    }
}
