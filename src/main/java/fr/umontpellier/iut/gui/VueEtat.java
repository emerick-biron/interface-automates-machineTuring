package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Etat;
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

public class VueEtat extends StackPane {
    private Etat etat;
    private Circle cercle;
    private Label labelNumEtat;
    private VueAutomate vueAutomate;
    private double mouseX;
    private double mouseY;
    private ImageView imageViewInitial;
    private ImageView imageViewTerminal;
    private BooleanProperty estSelectionne;
    private ChangeListener<Boolean> changementActivationEtat = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean ancienneValeur,
                            Boolean nouvelleValeur) {
            if (observableValue.getValue()) cercle.setFill(Color.GREEN);
            else cercle.setFill(Color.RED);
        }
    };
    private ChangeListener<Boolean> changementEstInitial = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (observableValue.getValue()) getChildren().add(imageViewInitial);
            else getChildren().remove(imageViewInitial);
        }
    };
    private ChangeListener<Boolean> changementEstTerminal = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (observableValue.getValue()) getChildren().add(imageViewTerminal);
            else getChildren().remove(imageViewTerminal);
        }
    };
    private ChangeListener<Boolean> changementSelection = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (aBoolean != t1) {
                if (observableValue.getValue()) {
                    vueAutomate.getVuesEtatSelectionnes().add(VueEtat.this);
                } else {
                    vueAutomate.getVuesEtatSelectionnes().remove(VueEtat.this);
                }
            }
        }
    };

    public VueEtat(Etat etat, VueAutomate vueAutomate) {
        this.vueAutomate = vueAutomate;
        this.etat = etat;
        estSelectionne = new SimpleBooleanProperty(false);
        labelNumEtat = new Label();
        imageViewInitial = new ImageView("imgEtatInitial.png");
        imageViewTerminal = new ImageView("imgEtatTerminal.png");
        cercle = new Circle(50, 50, 46, Color.RED);

        this.etat.estActifProperty().addListener(changementActivationEtat);
        this.etat.estInitialProperty().addListener(changementEstInitial);
        this.etat.estTerminalProperty().addListener(changementEstTerminal);
        estSelectionne.addListener(changementSelection);

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
            if (!vueAutomate.getVuePrincipale().ctrlPresse()) {
                vueAutomate.deSelectionnerVuesEtat();
            }
            if (estSelectionne()) {
                deSelectionner();
            } else {
                selectionner();
            }

            vueAutomate.getVuePrincipale().unbindCheckBoxes();

            CheckBox cbEstInitial = vueAutomate.getVuePrincipale().getCheckBoxEstInitial();
            CheckBox cbEstTerminal = vueAutomate.getVuePrincipale().getCheckBoxEstTerminal();

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
            if (newXpos >= 0 && newXpos + getWidth() <= vueAutomate.getBoundsInLocal().getMaxX()) {
                setLayoutX(newXpos);
                mouseX = mouseEvent.getSceneX();
            }
            //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
            if (newYPos >= 0 && newYPos + getHeight() <= vueAutomate.getBoundsInLocal().getMaxY() - 25) {
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
