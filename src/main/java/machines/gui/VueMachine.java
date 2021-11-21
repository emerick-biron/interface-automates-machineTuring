package machines.gui;

import javafx.scene.layout.Pane;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

public abstract class VueMachine<VP extends VuePrincipale, VM extends VueMachine<VP, VM, VE, VT, M, E, T>,
        VE extends VueEtat<VP, VM, VE, VT, M, E, T>, VT extends VueTransition<VP, VM, VE, VT, M, E, T>, M extends Machine<E, T>,
        E extends Etat<E, T>, T extends Transition<T, E>>
        extends Pane {
}

































