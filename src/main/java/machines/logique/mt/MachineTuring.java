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
    }

    public MachineTuring() {
        super();
    }

    @Override
    public void chargerFichier(String nomFichier) throws IOException {

    }

    @Override
    public void sauvegarder(String nomFichier) throws IOException {

    }

    @Override
    public Task<Boolean> getTaskLancer(String mot, long dellayMillis) {
        return null;
    }

    private void initRuban(){
        ruban = new char[1000];
        for (int i = 0; i < 1000; i++) {
            ruban[i] = '#';
        }
        teteDeLecture = 500;
    }
}
