package fr.umontpellier.iut.composants;

import fr.umontpellier.iut.logique.Etat;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class VueEtat extends Circle {
    private Etat etat;
    private Point2D dragAnchor;

    public Etat getEtat() {
        return etat;
    }

    public VueEtat(double v, Etat etat) {
        super(v);
        this.etat = etat;
    }

    public VueEtat(double v, Paint paint, Etat etat) {
        super(v, paint);
        this.etat = etat;
    }

    public VueEtat(Etat etat) {
        this.etat = etat;
        setCenterX(50);
        setCenterY(50);
        setRadius(50);
        setFill(Color.RED);
        init();
    }

    public VueEtat(double v, double v1, double v2, Etat etat) {
        super(v, v1, v2);
        this.etat = etat;
    }

    public VueEtat(double v, double v1, double v2, Paint paint, Etat etat) {
        super(v, v1, v2, paint);
        this.etat = etat;
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
                setCenterX(mouseEvent.getX());
                setCenterY(mouseEvent.getY());
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
