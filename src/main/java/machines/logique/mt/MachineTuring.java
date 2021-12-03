package machines.logique.mt;

import javafx.concurrent.Task;
import machines.logique.Etat;
import machines.logique.Machine;

import java.io.IOException;
import java.util.Set;

public class MachineTuring extends Machine<TransitionMT> {
    private char[] ruban;
    private int teteDeLecture;

    public MachineTuring(Set<Etat<TransitionMT>> etats) {
        super(etats);
        initRuban(10000);
    }

    public MachineTuring() {
        super();
        initRuban(10000);
    }

    @Override
    public void chargerFichier(String nomFichier) throws IOException {

    }

    @Override
    public void sauvegarder(String nomFichier) throws IOException {

    }

    @Override
    public Task<Integer> getTaskLancer(String mot, long dellayMillis) {
        return new Task<>() {
            @Override
            protected Integer call() throws Exception {
                getEtats().forEach(Etat::desactive);
                getEtatInitial().active();
                for (int i = 0; i < mot.length(); i++) {
                    ruban[teteDeLecture + i] = mot.charAt(i);
                }
                while (getEtatActif() != null) {
                    step(ruban[teteDeLecture]);
                    if (getEtatActif() != null && getEtatActif().estTerminal()) updateValue(teteDeLecture);
                }
                return teteDeLecture;
            }
        };
    }

    public Etat<TransitionMT> getEtatActif() {
        for (Etat<TransitionMT> etat : getEtats()) {
            if (etat.estActif()) return etat;
        }
        return null;
    }

    @Override
    public void step(char lettre) {
        Etat<TransitionMT> etatActif = getEtatActif();
        if (etatActif != null) {
            TransitionMT transitionEtape = null;
            for (TransitionMT trans : etatActif.getListeTransitions()) {
                if (trans.getEtiquette() == lettre) transitionEtape = trans;
            }
            if (transitionEtape != null) {
                ruban[teteDeLecture] = transitionEtape.getNouvelleLettre();
                switch (transitionEtape.getMouvement()) {
                    case DROITE:
                        teteDeLecture++;
                        break;
                    case GAUCHE:
                        teteDeLecture--;
                        break;
                }
                transitionEtape.getEtatDepart().desactive();
                transitionEtape.getEtatArrivee().active();
            }
            etatActif.desactive();
        }
    }

    @Override
    public boolean motReconnu() {
        return getEtatActif().estTerminal();
    }

    public Etat<TransitionMT> getEtatInitial() {
        for (Etat<TransitionMT> etat : getEtats()) {
            if (etat.estInitial()) return etat;
        }
        return null;
    }

    private void initRuban(int tailleRuban) {
        ruban = new char[tailleRuban];
        for (int i = 0; i < tailleRuban; i++) {
            ruban[i] = '#';
        }
        teteDeLecture = tailleRuban / 2;
    }
}






















