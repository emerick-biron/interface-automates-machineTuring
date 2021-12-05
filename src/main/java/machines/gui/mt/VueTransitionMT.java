package machines.gui.mt;

import javafx.beans.binding.DoubleBinding;
import machines.gui.VueMachine;
import machines.gui.VueTransition;
import machines.logique.mt.TransitionMT;

public class VueTransitionMT extends VueTransition<TransitionMT> {
    private TransitionMT transitionMT;
    private VueMT vueMT;

    public VueTransitionMT(TransitionMT transition, VueMT vueMachine) {
        super(transition, vueMachine);
        this.transitionMT = transition;
        this.vueMT = vueMachine;
        char lettreMvmt = getLettreMvmt();
        getLabelEtiquette()
                .setText(transitionMT.getEtiquette() + ";" + transitionMT.getNouvelleLettre() + ";" + lettreMvmt);
    }

    private char getLettreMvmt() {
        switch (transitionMT.getMouvement()) {
            case DROITE:
                return 'D';
            case GAUCHE:
                return 'G';
            default:
                return ' ';
        }
    }

    public VueMT getVueMT() {
        return vueMT;
    }

    public TransitionMT getTransitionMT() {
        return transitionMT;
    }

    public void positionnerLabelEtiquette(int index) {
        if (getTransition().getEtatDepart() != getTransition().getEtatArrivee()) {
            //coordonnees du centre du cercle de la vue etat de depart
            DoubleBinding xA = getVueEtatDep().layoutXProperty().add(getVueEtatDep().getCercle().getRadius());
            DoubleBinding yA = getVueEtatDep().layoutYProperty().add(getVueEtatDep().getCercle().getRadius());

            //coordonnees du centre du cercle de la vue etat d'arrivee
            DoubleBinding xB = getVueEtatFin().layoutXProperty().add(getVueEtatFin().getCercle().getRadius());
            DoubleBinding yB = getVueEtatFin().layoutYProperty().add(getVueEtatFin().getCercle().getRadius());

            DoubleBinding xPos = xA.add(xB.subtract(xA).multiply(6.5).divide(10));
            DoubleBinding yPos = yA.add(yB.subtract(yA).multiply(6.5).divide(10)).add(index * 17);

            getLabelEtiquette().layoutXProperty().bind(xPos);
            getLabelEtiquette().layoutYProperty().bind(yPos);
        } else {
            getLabelEtiquette().layoutXProperty()
                    .bind(getVueEtatDep().layoutXProperty().add(getVueEtatDep().getCercle().radiusProperty()));
            getLabelEtiquette().layoutYProperty().bind(getVueEtatDep().layoutYProperty()
                    .add(getVueEtatDep().getCercle().radiusProperty().multiply(2)).add(index * 17));
        }
    }

    @Override
    public String toString() {
        return getVueEtatDep().getLabelNumEtat().getText() + " " + transitionMT.getEtiquette() + " " +
                getVueEtatFin().getLabelNumEtat().getText() + " "+transitionMT.getNouvelleLettre()+ " "+transitionMT.getMouvement();
    }
}
