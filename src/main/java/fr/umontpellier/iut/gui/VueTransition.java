package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Transition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class VueTransition extends Group {
    private Transition transition;
    private VueAutomate vueAutomate;
    private Line ligne;

    public VueTransition(Transition transition, VueAutomate vueAutomate) {
        this.transition = transition;
        this.vueAutomate = vueAutomate;
        ligne = new Line();

        getChildren().add(ligne);

        init();
    }

    public void init() {
        ligne.setStrokeWidth(3);
        initCoordonneLigne();
        this.toBack();
    }

    public void initCoordonneLigne(){
        VueEtat vueEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart());
        VueEtat vueEtatFin = vueAutomate.getVueEtat(transition.getEtatArrivee());

        ligne.startXProperty().bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().getRadius()));
        ligne.startYProperty().bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().getRadius()));
        ligne.endXProperty().bind(vueEtatFin.layoutXProperty().add(vueEtatFin.getCercle().getRadius()));
        ligne.endYProperty().bind(vueEtatFin.layoutYProperty().add(vueEtatFin.getCercle().getRadius()));
    }
}
