package fr.umontpellier.iut.vues;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class VueEtat extends Circle {
    public VueEtat(double v, Paint paint) {
        super(v, paint);
        init();
    }

    public VueEtat() {
        setCenterX(50);
        setCenterY(50);
        setRadius(50);
        setFill(Color.RED);
        init();
    }

    public VueEtat(double v, double v1, double v2) {
        super(v, v1, v2);
        init();
    }

    public VueEtat(double v, double v1, double v2, Paint paint) {
        super(v, v1, v2, paint);
        init();
    }

    public void init() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCursor(Cursor.MOVE);
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCursor(Cursor.HAND);
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCenterX(mouseEvent.getSceneX());
                setCenterY(mouseEvent.getSceneY());
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCursor(Cursor.HAND);
            }
        });
    }
}
