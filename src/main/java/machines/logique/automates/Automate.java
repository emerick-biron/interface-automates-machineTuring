package machines.logique.automates;

import javafx.concurrent.Task;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

import java.io.*;
import java.util.*;

public class Automate extends Machine<TransitionAtmt> {

    public Automate(Set<Etat<TransitionAtmt>> etats) {
        super(etats);
    }

    public Automate() {
        super();
    }

    /**
     * Permet de creer un automate a partir d'un fichier
     *
     * @param nomFichier nom du fichier contenant l'automate
     * @throws IOException
     */
    public void chargerFichier(String nomFichier) throws IOException {
        getEtats().clear();

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        int nbEtat = Integer.parseInt(bf.readLine());

        for (int i = 0; i < nbEtat; i++) {
            getEtats().add(new Etat<>());
        }
        ArrayList<Etat<TransitionAtmt>> etats = new ArrayList<>(getEtats());
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

                    etats.get(numE1).ajoutTransition(new TransitionAtmt(etats.get(numE1), etats.get(numE2), lettre));
                }
            }
            ligne = bf.readLine();
        }

        bf.close();
        fr.close();
    }

    /**
     * Permet de sauvegarder l'automate dans un fichier
     *
     * @param nomFichier nom du fichier dans lequel sauvegarder l'automate
     * @throws IOException
     */
    public void sauvegarder(String nomFichier) throws IOException {
        Writer fileWriter = new FileWriter(nomFichier, false); //overwrites file
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //ecriture nbr etat
        bufferedWriter.write(String.valueOf(getEtats().size()));
        bufferedWriter.newLine();

        ArrayList<Etat<TransitionAtmt>> etats = new ArrayList<>(getEtats());

        //ecriture etats initiaux
        for (Etat<TransitionAtmt> etat : etats) {
            if (etat.estInitial()) {
                bufferedWriter.write("initial " + etats.indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture etats terminaux
        for (Etat<TransitionAtmt> etat : etats) {
            if (etat.estTerminal()) {
                bufferedWriter.write("terminal " +etats.indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture transitions
        for (Etat<TransitionAtmt> etat : etats) {
            Set<TransitionAtmt> transitions = etat.getListeTransitions();
            for (TransitionAtmt t : transitions) {
                bufferedWriter.write(etats.indexOf(t.getEtatDepart()) + " " + t.getEtiquette() + " " +
                        etats.indexOf(t.getEtatArrivee()));
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    @Override
    public Task<Boolean> getTaskLancer(String mot, long dellayMillis) {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                for (Etat<TransitionAtmt> e : getEtatsActifs()) {
                    e.desactive();
                }
                getEtatsActifs().clear();
                getEtatsActifs().addAll(getEtatsInitiaux());
                for (Etat<TransitionAtmt> e : getEtatsActifs()) {
                    e.active();
                }
                for (int i = 0; i < mot.length(); i++) {
                    updateProgress(i, mot.length());
                    Thread.sleep(dellayMillis);
                    char lettre = mot.charAt(i);
                    step(lettre);
                    if (i == mot.length() - 1) {
                        for (Etat<TransitionAtmt> etat : getEtats()) {
                            if (etat.estTerminal() && etat.estActif()) return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    private void step(char lettre) {
        List<Etat<TransitionAtmt>> nouveauxActifs = new ArrayList<>();
        for (Etat<TransitionAtmt> e : getEtatsActifs()) {
            for (Etat<TransitionAtmt> etatCible : e.cible(lettre)) {
                if (!nouveauxActifs.contains(etatCible)) {
                    nouveauxActifs.add(etatCible);
                }
            }
        }

        for (Etat<TransitionAtmt> e : getEtatsActifs()) {
            e.desactive();
        }

        getEtatsActifs().clear();
        getEtatsActifs().addAll(nouveauxActifs);

        for (Etat<TransitionAtmt> e : getEtatsActifs()) {
            e.active();
        }
    }

}




































