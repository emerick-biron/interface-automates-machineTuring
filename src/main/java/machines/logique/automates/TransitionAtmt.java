package machines.logique.automates;

import machines.logique.Etat;
import machines.logique.Transition;

public class TransitionAtmt extends Transition<TransitionAtmt> {
    public TransitionAtmt(Etat<TransitionAtmt> etatDepart, Etat<TransitionAtmt> etatArrivee, char etiquette) {
        super(etatDepart, etatArrivee, etiquette);
    }
}
