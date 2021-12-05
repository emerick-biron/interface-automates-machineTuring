package machines.logique.mt;

import javafx.concurrent.Task;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.automates.TransitionAtmt;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class MachineTuring extends Machine<TransitionMT> {
    private Ruban ruban;

    public MachineTuring(Set<Etat<TransitionMT>> etats) {
        super(etats);
        ruban = new Ruban();
    }

    public MachineTuring() {
        super();
        ruban = new Ruban();
    }

    @Override
    public void chargerFichier(String nomFichier) throws IOException {
        getEtats().clear();

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        int nbEtat = Integer.parseInt(bf.readLine());

        for (int i = 0; i < nbEtat; i++) {
            getEtats().add(new Etat<>());
        }
        ArrayList<Etat<TransitionMT>> etats = new ArrayList<>(getEtats());
        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            String[] split = ligne.split(" ");
            int m = split.length;

            if (m > 1) {

                if (split[0].equals("initial")) for (int i = 1; i < m; i++) {
                    int numEtatInit = Integer.parseInt(split[i]);
                    etats.get(numEtatInit).setEstInitial(true);
                }
                else if (split[0].equals("terminal")) for (int i = 1; i < m; i++) {
                    int numEtatTerm = Integer.parseInt(split[i]);
                    etats.get(numEtatTerm).setEstTerminal(true);
                }
                else {
                    int numE1 = Integer.parseInt(split[0]);
                    int numE2 = Integer.parseInt(split[2]);
                    char lettre = split[1].charAt(0);
                    char nouvelleLettre = split[3].charAt(0);
                    Mouvement mvmt;
                    if (split[4].equals("DROITE")) mvmt = Mouvement.DROITE;
                    else mvmt = Mouvement.GAUCHE;

                    etats.get(numE1).ajoutTransition(
                            new TransitionMT(etats.get(numE1), etats.get(numE2), lettre, nouvelleLettre, mvmt));
                }
            }
            ligne = bf.readLine();
        }

        bf.close();
        fr.close();
    }

    @Override
    public void sauvegarder(String nomFichier) throws IOException {
        Writer fileWriter = new FileWriter(nomFichier, false); //overwrites file
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //ecriture nbr etat
        bufferedWriter.write(String.valueOf(getEtats().size()));
        bufferedWriter.newLine();

        ArrayList<Etat<TransitionMT>> etats = new ArrayList<>(getEtats());

        //ecriture etat initial
        if (getEtatInitial() != null) bufferedWriter.write("initial " + etats.indexOf(getEtatInitial()));
        else bufferedWriter.write("initial");
        bufferedWriter.newLine();

        //ecriture etats terminaux
        for (Etat<TransitionMT> etat : etats) {
            if (etat.estTerminal()) {
                bufferedWriter.write("terminal " + etats.indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture transitions
        for (Etat<TransitionMT> etat : etats) {
            Set<TransitionMT> transitions = etat.getListeTransitions();
            for (TransitionMT t : transitions) {
                bufferedWriter.write(etats.indexOf(t.getEtatDepart()) + " " + t.getEtiquette() + " " +
                        etats.indexOf(t.getEtatArrivee()) + " " + t.getNouvelleLettre() + " " + t.getMouvement());
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    @Override
    public Task<Integer> getTaskLancer(String mot, long dellayMillis) {
        return new Task<>() {
            @Override
            protected Integer call() throws Exception {
                getEtats().forEach(Etat::desactive);
                getEtatInitial().active();
                ruban.setRuban(mot);
                while (getEtatActif() != null) {
                    updateValue(ruban.getTeteLecture());
                    Thread.sleep(dellayMillis);
                    step(ruban.lire());
                }
                return ruban.getTeteLecture();
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
            TransitionMT transEtape = null;
            for (TransitionMT trans : etatActif.getListeTransitions()) {
                if (trans.getEtiquette() == lettre) transEtape = trans;
            }
            if (transEtape != null) {
                ruban.ecrire(transEtape.getNouvelleLettre(), transEtape.getMouvement());
                transEtape.getEtatDepart().desactive();
                transEtape.getEtatArrivee().active();
            } else {
                etatActif.desactive();
            }
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

    public Ruban getRuban() {
        return ruban;
    }
}






















