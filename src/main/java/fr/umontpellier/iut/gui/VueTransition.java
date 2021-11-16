package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class VueTransition extends Group {
    private Transition transition;
    private VueAutomate vueAutomate;
    private Line ligne;
    private Label labelEtiquette;
    private Line ligneHautFleche;
    private Line ligneBasFleche;
    private ImageView imgAutoTransition;
    private VueEtat vueEtatDep;
    private VueEtat vueEtatFin;
    private BooleanProperty estSelectionne;
    private ChangeListener<Boolean> changementSelection = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (aBoolean != t1) {
                if (observableValue.getValue()) {
                    for (VueTransition vueTransition : vueAutomate.getVuesTransition(vueEtatDep, vueEtatFin)) {
                        vueAutomate.getVuesTransitionSelectionnes().add(vueTransition);
                    }
                } else {
                    vueAutomate.getVuesTransitionSelectionnes().remove(VueTransition.this);
                    for (VueTransition vueTransition : vueAutomate.getVuesTransition(vueEtatDep, vueEtatFin)) {
                        vueAutomate.getVuesTransitionSelectionnes().remove(vueTransition);
                    }
                }
            }
        }
    };

    public VueTransition(Transition transition, VueAutomate vueAutomate) {
        this.transition = transition;
        this.vueAutomate = vueAutomate;
        estSelectionne = new SimpleBooleanProperty(false);
        ligne = new Line();
        ligneHautFleche = new Line();
        ligneBasFleche = new Line();
        labelEtiquette = new Label(String.valueOf(transition.getEtiquette()));
        imgAutoTransition = new ImageView("auto_transition.png");
        vueEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart());
        vueEtatFin = vueAutomate.getVueEtat(transition.getEtatArrivee());

        estSelectionne.addListener(changementSelection);

        init();

        getChildren().add(labelEtiquette);
    }

    public Transition getTransition() {
        return transition;
    }

    public void init() {
        initStyleLignes();
        if (transition.getEtatDepart() != transition.getEtatArrivee()) initCoordonnesLignes();
        else initAutoTransition();
        initLabel();
        initMouseEvents();
        this.toBack();
    }

    public void initCoordonnesLignes() {
        //coordonnees du centre du cercle de la vue etat de depart
        DoubleBinding xA = vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().getRadius());
        DoubleBinding yA = vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().getRadius());

        //coordonnees du centre du cercle de la vue etat d'arrivee
        DoubleBinding xB = vueEtatFin.layoutXProperty().add(vueEtatFin.getCercle().getRadius());
        DoubleBinding yB = vueEtatFin.layoutYProperty().add(vueEtatFin.getCercle().getRadius());


        ligne.startXProperty().bind(xA);
        ligne.startYProperty().bind(yA);
        ligne.endXProperty().bind(xB);
        ligne.endYProperty().bind(yB);

        DoubleBinding xPosEnd = xA.add(xB.subtract(xA).multiply(6.5).divide(10));
        DoubleBinding yPosEnd = yA.add(yB.subtract(yA).multiply(6.5).divide(10));

        double l1 = 20;
        double l2 = 10;

        DoubleBinding norme = Bindings.createDoubleBinding(
                () -> Math.sqrt(Math.pow(xB.get() - xA.get(), 2) + Math.pow(yB.get() - yA.get(), 2)), xA, xB, yA, yB);

        DoubleBinding xPosStartTop = xPosEnd.subtract(xB.subtract(xA).divide(norme).multiply(l1))
                .add(yA.subtract(yB).divide(norme).multiply(l2));
        DoubleBinding yPosStartTop = yPosEnd.subtract(yB.subtract(yA).divide(norme).multiply(l1))
                .add(xB.subtract(xA).divide(norme).multiply(l2));

        DoubleBinding xPosStartBot = xPosEnd.subtract(xB.subtract(xA).divide(norme).multiply(l1))
                .subtract(yA.subtract(yB).divide(norme).multiply(l2));
        DoubleBinding yPosStartBot = yPosEnd.subtract(yB.subtract(yA).divide(norme).multiply(l1))
                .subtract(xB.subtract(xA).divide(norme).multiply(l2));

        ligneHautFleche.endXProperty().bind(xPosEnd);
        ligneHautFleche.endYProperty().bind(yPosEnd);
        ligneHautFleche.startXProperty().bind(xPosStartTop);
        ligneHautFleche.startYProperty().bind(yPosStartTop);

        ligneBasFleche.endXProperty().bind(xPosEnd);
        ligneBasFleche.endYProperty().bind(yPosEnd);
        ligneBasFleche.startXProperty().bind(xPosStartBot);
        ligneBasFleche.startYProperty().bind(yPosStartBot);

        getChildren().add(ligneHautFleche);
        getChildren().add(ligneBasFleche);
        getChildren().add(ligne);
    }

    public void initAutoTransition() {
        imgAutoTransition.setFitHeight(25);
        imgAutoTransition.setPreserveRatio(true);
        imgAutoTransition.layoutXProperty()
                .bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().radiusProperty()).subtract(30));
        imgAutoTransition.layoutYProperty()
                .bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().radiusProperty().multiply(2)));

        getChildren().add(imgAutoTransition);
    }

    public void initStyleLignes() {
        ligne.setStrokeWidth(3.8);
        ligne.setStroke(Color.GRAY);
        ligneHautFleche.setStrokeWidth(3.8);
        ligneHautFleche.setStroke(Color.GRAY);
        ligneBasFleche.setStrokeWidth(3.8);
        ligneBasFleche.setStroke(Color.GRAY);
    }

    public void initLabel() {
        positionnerLabelEtiquette(0);

        labelEtiquette.setStyle("-fx-font-weight: bold; -fx-font-size: 17;");
    }

    public Label getLabelEtiquette() {
        return labelEtiquette;
    }

    public void setFlechesVisible(boolean b) {
        ligne.setVisible(b);
        ligneBasFleche.setVisible(b);
        ligneHautFleche.setVisible(b);
    }

    public void setCouleurSelection(boolean estSelectionne){
        if (estSelectionne) {
            ligne.setStroke(Color.valueOf("#003576"));
            ligneBasFleche.setStroke(Color.valueOf("#003576"));
            ligneHautFleche.setStroke(Color.valueOf("#003576"));
            imgAutoTransition.setImage(new Image("auto_transition_select.png"));
        }else {
            ligne.setStroke(Color.GRAY);
            ligneBasFleche.setStroke(Color.GRAY);
            ligneHautFleche.setStroke(Color.GRAY);
            imgAutoTransition.setImage(new Image("auto_transition.png"));

        }
    }

    public void positionnerLabelEtiquette(int index) {
        if (transition.getEtatDepart() != transition.getEtatArrivee()) {
            //coordonnees du centre du cercle de la vue etat de depart
            DoubleBinding xA = vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().getRadius());
            DoubleBinding yA = vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().getRadius());

            //coordonnees du centre du cercle de la vue etat d'arrivee
            DoubleBinding xB = vueEtatFin.layoutXProperty().add(vueEtatFin.getCercle().getRadius());
            DoubleBinding yB = vueEtatFin.layoutYProperty().add(vueEtatFin.getCercle().getRadius());

            DoubleBinding xPos = xA.add(xB.subtract(xA).multiply(6.5).divide(10)).subtract(5).add(index * 10);
            DoubleBinding yPos = yA.add(yB.subtract(yA).multiply(6.5).divide(10));

            labelEtiquette.layoutXProperty().bind(xPos);
            labelEtiquette.layoutYProperty().bind(yPos);
        } else {
            labelEtiquette.layoutXProperty()
                    .bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().radiusProperty()).add(index * 10));
            labelEtiquette.layoutYProperty()
                    .bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().radiusProperty().multiply(2)));
        }
    }

    public void initMouseEvents() {
        setOnMousePressed(mouseEvent -> {
            if (!vueAutomate.getVuePrincipale().ctrlPresse()) {
                vueAutomate.deSelectionnerVues();
            }
            if (estSelectionne()) {
                deSelectionner();
            } else {
                selectionner();
            }
        });
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

    public VueEtat getVueEtatDep() {
        return vueEtatDep;
    }

    public VueEtat getVueEtatFin() {
        return vueEtatFin;
    }
}






























