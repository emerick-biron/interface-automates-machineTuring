package fr.umontpellier.iut.gui;

import fr.umontpellier.iut.logique.Transition;
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

    public void init(){
        Circle cercleEtatDep = vueAutomate.getVueEtat(transition.getEtatDepart()).getCercle();
        Circle cercleEtatFin = vueAutomate.getVueEtat(transition.getEtatArrivee()).getCercle();

        ligne.startXProperty().bind(cercleEtatDep.centerXProperty());
        ligne.startYProperty().bind(cercleEtatDep.centerYProperty());
        ligne.endXProperty().bind(cercleEtatFin.centerXProperty());
        ligne.endYProperty().bind(cercleEtatFin.centerYProperty());
    }
}
