package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Etat;
import fr.umontpellier.iut.logique.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private ChangeListener<Boolean> changementActivationEtat = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean ancienneValeur,
                            Boolean nouvelleValeur) {
            if (observableValue.getValue()) cercle.setFill(Color.GREEN);
            else cercle.setFill(Color.RED);
        }
    };

    public VueEtat(Etat etat, VueAutomate vueAutomate) {
        this.vueAutomate = vueAutomate;
        this.etat = etat;
        this.etat.estActifProperty().addListener(changementActivationEtat);
        labelNumEtat = new Label();
        cercle = new Circle(50, 50, 50, Color.RED);
        getChildren().add(cercle);
        init();
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
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
            ActionSouris actionSouris = vueAutomate.getVuePrincipale().getActionsSouris();
            if (actionSouris == ActionSouris.DEPLACER_ETAT) {
                setCursor(Cursor.MOVE);
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
            } else if (actionSouris == ActionSouris.SUPPRIMER_ETAT) {
                //Permet de supprimer un etat
                vueAutomate.getAutomate().supprimerEtat(etat);
                vueAutomate.getVuePrincipale().setDefaultActionSouris();
            } else if (actionSouris == ActionSouris.AJOUTER_TRANSITION) {
                VueEtat vueEtatSelectionne = vueAutomate.getVueEtatSelectionne();
                if (vueEtatSelectionne == null) {
                    vueAutomate.setVueEtatSelectionne(this);
                } else {
                    String etiquette = vueAutomate.getVuePrincipale().getTextFieldEtiquette().getText();
                    if (etiquette.length() >= 1) {
                        vueAutomate.getAutomate().ajoutTransition(new Transition(vueEtatSelectionne.getEtat(), etat, etiquette.charAt(0)));
                        vueAutomate.setVueEtatSelectionne(null);
                        vueAutomate.getVuePrincipale().setDefaultActionSouris();
                    }
                }
            }
        });

        setOnMouseReleased(mouseEvent -> setCursor(Cursor.HAND));

        setOnMouseDragged(mouseEvent -> {
            ActionSouris actionSouris = vueAutomate.getVuePrincipale().getActionsSouris();
            if (actionSouris == ActionSouris.DEPLACER_ETAT) {
                //Permet de deplacer la vueEtat a la souris
                double deltaX = mouseEvent.getSceneX() - mouseX;
                double deltaY = mouseEvent.getSceneY() - mouseY;
                double newXpos = getLayoutX() + deltaX;
                double newYPos = getLayoutY() + deltaY;

                //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
                if (newXpos >= 0 && newXpos + getWidth() <= vueAutomate.getBoundsInLocal().getMaxX()) {
                    setLayoutX(newXpos);
                    mouseX = mouseEvent.getSceneX();
                }
                //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
                if (newYPos >= 0 && newYPos + getHeight() <= vueAutomate.getBoundsInLocal().getMaxY()) {
                    setLayoutY(newYPos);
                    mouseY = mouseEvent.getSceneY();
                }
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

    public Circle getCercle() {
        return cercle;
    }
}
