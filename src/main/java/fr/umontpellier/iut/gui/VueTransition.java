package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Transition;
import javafx.beans.binding.DoubleBinding;
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

    public VueTransition(Transition transition, VueAutomate vueAutomate) {
        this.transition = transition;
        this.vueAutomate = vueAutomate;
        ligne = new Line();
        ligneHautFleche = new Line();
        ligneBasFleche = new Line();
        labelEtiquette = new Label(String.valueOf(transition.getEtiquette()));
        imgAutoTransition = new ImageView("imgAutoTransition.png");

        getChildren().add(labelEtiquette);

        init();
    }

    public Transition getTransition() {
        return transition;
    }

    public void init() {
        initStyleLignes();
        if (transition.getEtatDepart() != transition.getEtatArrivee()) initCoordonnesLignes();
        else initAutoTransition();
        initLabel();
        this.toBack();
    }

    public void initCoordonnesLignes() {
        VueEtat vueEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart());
        VueEtat vueEtatFin = vueAutomate.getVueEtat(transition.getEtatArrivee());

        ligne.startXProperty().bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().getRadius()));
        ligne.startYProperty().bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().getRadius()));
        ligne.endXProperty().bind(vueEtatFin.layoutXProperty().add(vueEtatFin.getCercle().getRadius()));
        ligne.endYProperty().bind(vueEtatFin.layoutYProperty().add(vueEtatFin.getCercle().getRadius()));


        DoubleBinding xPosMiddleBinding = ligne.endXProperty().add(ligne.startXProperty()).divide(2);
        DoubleBinding yPosMidlleBinding = ligne.endYProperty().add(ligne.startYProperty()).divide(2);

        ligneHautFleche.endXProperty().bind(xPosMiddleBinding);
        ligneHautFleche.endYProperty().bind(yPosMidlleBinding);
        ligneHautFleche.startXProperty().bind(xPosMiddleBinding.subtract(20));
        ligneHautFleche.startYProperty().bind(yPosMidlleBinding.subtract(10));

        ligneBasFleche.endXProperty().bind(xPosMiddleBinding);
        ligneBasFleche.endYProperty().bind(yPosMidlleBinding);
        ligneBasFleche.startXProperty().bind(xPosMiddleBinding.subtract(20));
        ligneBasFleche.startYProperty().bind(yPosMidlleBinding.add(10));

        getChildren().add(ligneHautFleche);
        getChildren().add(ligneBasFleche);
        getChildren().add(ligne);
    }

    public void initAutoTransition() {
        VueEtat vueEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart());

        imgAutoTransition.setFitHeight(25);
        imgAutoTransition.setPreserveRatio(true);
        imgAutoTransition.layoutXProperty()
                .bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().radiusProperty())
                        .subtract(30));
        imgAutoTransition.layoutYProperty()
                .bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().radiusProperty().multiply(2)));

        getChildren().add(imgAutoTransition);
    }

    public void initStyleLignes() {
        ligne.setStrokeWidth(3);
        ligne.setStroke(Color.GRAY);
        ligneHautFleche.setStrokeWidth(3);
        ligneHautFleche.setStroke(Color.RED);
        ligneBasFleche.setStrokeWidth(3);
        ligneBasFleche.setStroke(Color.BLUE);
    }

    public void initLabel() {
        if (transition.getEtatDepart() != transition.getEtatArrivee()) {
            DoubleBinding xPosMiddleBinding = ligne.endXProperty().add(ligne.startXProperty()).divide(2);
            DoubleBinding yPosMidlleBinding = ligne.endYProperty().add(ligne.startYProperty()).divide(2);

            labelEtiquette.layoutXProperty().bind(xPosMiddleBinding);
            labelEtiquette.layoutYProperty().bind(yPosMidlleBinding);
        } else {
            VueEtat vueEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart());

            labelEtiquette.layoutXProperty()
                    .bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().radiusProperty()));
            labelEtiquette.layoutYProperty()
                    .bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().radiusProperty().multiply(2)));
        }

        labelEtiquette.setStyle("-fx-font-weight: bold; -fx-font-size: 17;");
    }

    private Label getLabelEtiquette() {
        return labelEtiquette;
    }
}
