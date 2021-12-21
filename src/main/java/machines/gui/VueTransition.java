package machines.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import machines.gui.automates.VueTransitionAtmt;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

public abstract class VueTransition<T extends Transition<T>> extends Group {
    private T transition;
    private VueMachine<T> vueMachine;
    private Line ligne;
    private Label labelEtiquette;
    private Line ligneHautFleche;
    private Line ligneBasFleche;
    private ImageView imgAutoTransition;
    private VueEtat<T> vueEtatDep;
    private VueEtat<T> vueEtatFin;
    private BooleanProperty estSelectionne;
    private ChangeListener<Boolean> changementSelection = (observableValue, aBoolean, t1) -> {
        if (aBoolean != t1) {
            if (observableValue.getValue()) {
                for (VueTransition<T> vueTransition : vueMachine.getVuesTransition(getVueEtatDep(), getVueEtatFin())) {
                    vueMachine.getVuesTransitionSelectionnes().add(vueTransition);
                }
            } else {
                vueMachine.getVuesTransitionSelectionnes().remove(VueTransition.this);
                for (VueTransition<T> vueTransition : vueMachine.getVuesTransition(getVueEtatDep(), getVueEtatFin())) {
                    vueMachine.getVuesTransitionSelectionnes().remove(vueTransition);
                }
            }
        }
    };

    public VueTransition(T transition, VueMachine<T> vueMachine) {
        this.transition = transition;
        this.vueMachine = vueMachine;
        estSelectionne = new SimpleBooleanProperty(false);
        ligne = new Line();
        ligneHautFleche = new Line();
        ligneBasFleche = new Line();
        labelEtiquette = new Label("");
        imgAutoTransition = new ImageView("auto_transition.png");
        vueEtatDep = vueMachine.getVueEtat(transition.getEtatDepart());
        vueEtatFin = vueMachine.getVueEtat(transition.getEtatArrivee());

        init();

        getChildren().add(labelEtiquette);
    }

    private void initListeners() {
        estSelectionneProperty().addListener(changementSelection);
    }

    public VueMachine<T> getVueMachine() {
        return vueMachine;
    }

    public T getTransition() {
        return transition;
    }

    private void init() {
        initListeners();
        initStyleLignes();
        if (transition.getEtatDepart() != transition.getEtatArrivee()) initCoordonnesLignes();
        else initAutoTransition();
        initLabel();
        initMouseEvents();
        this.toBack();
    }

    private void initCoordonnesLignes() {
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

    private void initAutoTransition() {
        imgAutoTransition.setFitHeight(25);
        imgAutoTransition.setPreserveRatio(true);
        imgAutoTransition.layoutXProperty()
                .bind(vueEtatDep.layoutXProperty().add(vueEtatDep.getCercle().radiusProperty()).subtract(30));
        imgAutoTransition.layoutYProperty()
                .bind(vueEtatDep.layoutYProperty().add(vueEtatDep.getCercle().radiusProperty().multiply(2)));

        getChildren().add(imgAutoTransition);
    }

    private void initStyleLignes() {
        ligne.setStrokeWidth(3.8);
        ligne.setStroke(Color.GRAY);
        ligneHautFleche.setStrokeWidth(3.8);
        ligneHautFleche.setStroke(Color.GRAY);
        ligneBasFleche.setStrokeWidth(3.8);
        ligneBasFleche.setStroke(Color.GRAY);
    }

    private void initLabel() {
        positionnerLabelEtiquette(0);
        labelEtiquette.setStyle("-fx-font-weight: bold; -fx-font-size: 17;");
    }

    public Label getLabelEtiquette() {
        return labelEtiquette;
    }

    /**
     * Permet de rendre les fleches de la vue transition visible ou non
     *
     * @param b true si on veut qu'elles soient visibles sinon false
     */
    public void setFlechesVisible(boolean b) {
        ligne.setVisible(b);
        ligneBasFleche.setVisible(b);
        ligneHautFleche.setVisible(b);
    }

    /**
     * Permet changer la couleur des fleches en fonction de si la vue transition est selectionne
     *
     * @param estSelectionne true si la vue est selectionne sinon false
     */
    public void setCouleurSelection(boolean estSelectionne) {
        if (estSelectionne) {
            ligne.setStroke(Color.valueOf("#003576"));
            ligneBasFleche.setStroke(Color.valueOf("#003576"));
            ligneHautFleche.setStroke(Color.valueOf("#003576"));
            imgAutoTransition.setImage(new Image("auto_transition_select.png"));
        } else {
            ligne.setStroke(Color.GRAY);
            ligneBasFleche.setStroke(Color.GRAY);
            ligneHautFleche.setStroke(Color.GRAY);
            imgAutoTransition.setImage(new Image("auto_transition.png"));

        }
    }

    /**
     * Permet de positionner le label de l'etiquette de la transition en fonction du nombre de transition entre deux memes etats
     *
     * @param index index de la vue transition par rapport aux autres vues transitions ayant les meme etats aux bornes
     */
    public abstract void positionnerLabelEtiquette(int index);

    private void initMouseEvents() {
        setOnMousePressed(mouseEvent -> {
            if (!vueMachine.getVuePrincipale().ctrlPresse()) vueMachine.deSelectionnerVues();
            if (estSelectionne()) deSelectionner();
            else selectionner();

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

    public VueEtat<T> getVueEtatDep() {
        return vueEtatDep;
    }

    public VueEtat<T> getVueEtatFin() {
        return vueEtatFin;
    }
}