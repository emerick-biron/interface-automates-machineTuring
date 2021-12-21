package machines.gui.automates;

import javafx.beans.binding.DoubleBinding;
import machines.gui.VueTransition;
import machines.logique.automates.TransitionAtmt;

public class VueTransitionAtmt extends VueTransition<TransitionAtmt> {
    private TransitionAtmt transitionAtmt;
    private VueAutomate vueAutomate;

    public VueTransitionAtmt(TransitionAtmt transition, VueAutomate vueAutomate) {
        super(transition, vueAutomate);
        this.vueAutomate = vueAutomate;
        transitionAtmt = transition;
        getLabelEtiquette().setText(String.valueOf(transitionAtmt.getEtiquette()));
    }


    public TransitionAtmt getTransitionAtmt() {
        return transitionAtmt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void positionnerLabelEtiquette(int index) {
        if (getTransition().getEtatDepart() != getTransition().getEtatArrivee()) {
            //coordonnees du centre du cercle de la vue etat de depart
            DoubleBinding xA = getVueEtatDep().layoutXProperty().add(getVueEtatDep().getCercle().getRadius());
            DoubleBinding yA = getVueEtatDep().layoutYProperty().add(getVueEtatDep().getCercle().getRadius());

            //coordonnees du centre du cercle de la vue etat d'arrivee
            DoubleBinding xB = getVueEtatFin().layoutXProperty().add(getVueEtatFin().getCercle().getRadius());
            DoubleBinding yB = getVueEtatFin().layoutYProperty().add(getVueEtatFin().getCercle().getRadius());

            DoubleBinding xPos = xA.add(xB.subtract(xA).multiply(6.5).divide(10)).add(index * 10);
            DoubleBinding yPos = yA.add(yB.subtract(yA).multiply(6.5).divide(10));

            getLabelEtiquette().layoutXProperty().bind(xPos);
            getLabelEtiquette().layoutYProperty().bind(yPos);
        } else {
            getLabelEtiquette().layoutXProperty()
                    .bind(getVueEtatDep().layoutXProperty().add(getVueEtatDep().getCercle().radiusProperty())
                            .add(index * 10));
            getLabelEtiquette().layoutYProperty().bind(getVueEtatDep().layoutYProperty()
                    .add(getVueEtatDep().getCercle().radiusProperty().multiply(2)));
        }
    }

    public VueAutomate getVueAutomate() {
        return vueAutomate;
    }

    @Override
    public String toString() {
        return getVueEtatDep().getLabelNumEtat().getText() + " " + transitionAtmt.getEtiquette() + " " +
                getVueEtatFin().getLabelNumEtat().getText();
    }
}