package machines.automates.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import machines.logique.Machine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automate extends Machine<EtatAtmt, TransitionAtmt> {

    public Automate(List<EtatAtmt> etats) {
        super(etats);
    }

    public Automate(EtatAtmt... etats) {
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
        EtatAtmt[] etats = new EtatAtmt[nbEtat];

        for (int i = 0; i < nbEtat; i++) {
            etats[i] = new EtatAtmt();
        }

        ArrayList<TransitionAtmt> transitionArrayList = new ArrayList<>();

        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            String[] split = ligne.split(" ");
            int m = split.length;

            if (m > 1) {

                if (split[0].equals("initial")) for (int i = 1; i < m; i++) {
                    int numEtatInit = Integer.parseInt(split[i]);
                    etats[numEtatInit].setEstInitial(true);
                }
                else if (split[0].equals("terminal")) for (int i = 1; i < m; i++) {
                    int numEtatTerm = Integer.parseInt(split[i]);
                    etats[numEtatTerm].setEstTerminal(true);
                }
                else {
                    int numE1 = Integer.parseInt(split[0]);
                    int numE2 = Integer.parseInt(split[2]);
                    char lettre = split[1].charAt(0);

                    transitionArrayList.add(new TransitionAtmt(etats[numE1], etats[numE2], lettre));
                }
            }
            ligne = bf.readLine();
        }

        bf.close();
        fr.close();

        getEtats().addAll(Arrays.asList(etats));
        ajoutTransition(transitionArrayList);
    }

    /**
     * Permet d'obtenir l'etat initial (ne fonctionne que pour une automate deterministe)
     *
     * @return l'etat initial de l'automate
     */
    public EtatAtmt getEtatInitial() {
        for (EtatAtmt etat : getEtats()) {
            if (etat.estInitial()) return etat;
        }
        return null;
    }

    /**
     * Permet de savoir si l'automate reconnait un mot (ne fonctionne que pour un automate deterministe)
     *
     * @param mot mot a tester
     * @return true si l'automate reconnait le mot sinon false
     */
    public boolean reconnait(String mot) {
        EtatAtmt courant = getEtatInitial();
        char lettre;
        for (int i = 0; i < mot.length(); i++) {
            lettre = mot.charAt(i);
            if (!courant.existeTrans(lettre)) return false;
            courant = courant.cible(lettre);
        }
        return courant.estTerminal();
    }

    /**
     * Permet de savoir si un mot peut etre reconnu a partir d'un certain etat
     *
     * @param etat etat de depart
     * @param mot  mot a tester
     * @return true si le mot est reconnu sinon false
     */
    public boolean reconnAux(EtatAtmt etat, String mot) {
        if (mot.length() < 1) {
            return etat.estTerminal();
        } else {
            ArrayList<TransitionAtmt> transitions = etat.getListeTransitions();
            char lettre = mot.charAt(0);
            int i = 0;
            while (i < transitions.size()) {
                TransitionAtmt t = transitions.get(i);
                if (t.getEtiquette() == lettre) {
                    if (reconnAux(t.getEtatArrivee(), mot.substring(1, mot.length()))) return true;
                }
                i++;
            }
        }
        return false;
    }

    /**
     * Permet de savoir si un automate non deterministe reconnait un certain mot
     *
     * @param mot mot a tester
     * @return true si l'automate reconnait le mot sinon false
     */
    public boolean reconnaitND(String mot) {
        for (EtatAtmt etat : getEtats()) {
            if (etat.estInitial()) {
                if (reconnAux(etat, mot)) return true;
            }
        }
        return false;
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

        //ecriture etats initiaux
        for (EtatAtmt etat : getEtats()) {
            if (etat.estInitial()) {
                bufferedWriter.write("initial " + getEtats().indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture etats terminaux
        for (EtatAtmt etat : getEtats()) {
            if (etat.estTerminal()) {
                bufferedWriter.write("terminal " + getEtats().indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture transitions
        for (EtatAtmt etat : getEtats()) {
            ArrayList<TransitionAtmt> transitions = etat.getListeTransitions();
            for (TransitionAtmt t : transitions) {
                bufferedWriter.write(getEtats().indexOf(t.getEtatDepart()) + " " + t.getEtiquette() + " " +
                        getEtats().indexOf(t.getEtatArrivee()));
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    public Task<Void> getTaskLancer(String mot, long dellayMillis) {
        Task<Void> taskLancer = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (EtatAtmt e : getEtatsActifs()) {
                    e.desactive();
                }
                getEtatsActifs().clear();
                getEtatsActifs().addAll(getEtatsInitiaux());
                for (EtatAtmt e : getEtatsActifs()) {
                    e.active();
                }
                for (int i = 0; i < mot.length(); i++) {
                    Thread.sleep(dellayMillis);
                    char lettre = mot.charAt(i);
                    step(lettre);
                }
                return null;
            }
        };
        return taskLancer;
    }

    public void lancer(Task<Void> taskLancer) throws InterruptedException {
        new Thread(taskLancer).start();
    }

    public void step(char lettre) {
        List<EtatAtmt> nouveauxActifs = new ArrayList<>();
        for (EtatAtmt e : getEtatsActifs()) {
            for (EtatAtmt etatCible : e.cibleND(lettre)) {
                if (!nouveauxActifs.contains(etatCible)) {
                    nouveauxActifs.add(etatCible);
                }
            }
        }

        for (EtatAtmt e : getEtatsActifs()) {
            e.desactive();
        }

        getEtatsActifs().clear();
        getEtatsActifs().addAll(nouveauxActifs);

        for (EtatAtmt e : getEtatsActifs()) {
            e.active();
        }
    }

}




































