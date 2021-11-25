package machines.logique.automates;

import machines.logique.Etat;
import machines.logique.Transition;

public class TransitionAtmt extends Transition {
    public TransitionAtmt(Etat etatDepart, Etat etatArrivee, char etiquette) {
        super(etatDepart, etatArrivee, etiquette);
    }


}
