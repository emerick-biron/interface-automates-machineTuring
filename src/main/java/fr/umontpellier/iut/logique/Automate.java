package fr.umontpellier.iut.logique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automate {
    private ObservableList<Etat> etats;

    public Automate(List<Etat> etats) {
        this.etats =  FXCollections.observableArrayList(etats);
    }

    public Automate(Etat... etats) {
        this.etats = FXCollections.observableArrayList(etats);
    }

    /**
     * Permet de creer un automate a partir d'un fichier
     *
     * @param nomFichier fichier contenant l'automate
     * @throws IOException
     */
    public Automate(String nomFichier) throws IOException {
        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        int nbEtat = Integer.parseInt(bf.readLine());
        Etat[] etats = new Etat[nbEtat];

        for (int i = 0; i < nbEtat; i++) {
            etats[i] = new Etat();
        }

        String ligne = bf.readLine();
        String[] split = ligne.split(" ");
        int m = split.length;

        while (m > 1) {
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

                Transition transition = new Transition(etats[numE1], etats[numE2], lettre);
                etats[numE1].ajoutTransition(transition);
            }

            ligne = bf.readLine();
            if (ligne != null) {
                split = ligne.split(" ");
                m = split.length;
            } else m = 0;
        }

        bf.close();
        fr.close();

        this.etats =  FXCollections.observableArrayList(etats);
    }

    public Automate() {
        etats =  FXCollections.observableArrayList();
    }

    public ObservableList<Etat> etatsProperty() {
        return etats;
    }

    /**
     * Permet d'obtenir l'etat initial (ne fonctionne que pour une automate deterministe)
     *
     * @return l'etat initial de l'automate
     */
    public Etat getEtatInitial() {
        for (Etat etat : etats) {
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
        Etat courant = getEtatInitial();
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
    public boolean reconnAux(Etat etat, String mot) {
        if (mot.length() < 1) {
            return etat.estTerminal();
        } else {
            ArrayList<Transition> transitions = etat.getListeTransitions();
            char lettre = mot.charAt(0);
            int i = 0;
            while (i < transitions.size()) {
                Transition t = transitions.get(i);
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
        for (Etat etat : etats) {
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
    public void ecrit(String nomFichier) throws IOException {
        Writer fileWriter = new FileWriter(nomFichier, false); //overwrites file
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //ecriture nbr etat
        bufferedWriter.write(String.valueOf(etats.size()));
        bufferedWriter.newLine();

        //ecriture etats initiaux
        for (Etat etat : etats) {
            if (etat.estInitial()) {
                bufferedWriter.write("initial " + etats.indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture etats terminaux
        for (Etat etat : etats) {
            if (etat.estTerminal()) {
                bufferedWriter.write("terminal " + etats.indexOf(etat));
                bufferedWriter.newLine();
            }
        }

        //ecriture transitions
        for (Etat etat : etats) {
            ArrayList<Transition> transitions = etat.getListeTransitions();
            for (Transition t : transitions) {
                bufferedWriter.write(etats.indexOf(t.getEtatDepart()) + " " + t.getEtiquette() + " " +
                        etats.indexOf(t.getEtatArrivee()));
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

<<<<<<< HEAD:src/main/java/fr/umontpellier/iut/Automate.java
    /**
     * Permet d'obtenir tous les etats initiaux d'un automate non deterministe
     *
     * @return liste des etats initiaux
     */
=======
>>>>>>> faed03ae50f30d8d0b2f7e275eaa7e55e1a40c4b:src/main/java/fr/umontpellier/iut/logique/Automate.java
    public List<Etat> getEtatsInitiaux() {
        List<Etat> res = new ArrayList<>();
        for (Etat etat : etats) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    public void ajouterEtat(Etat etat) {
        etats.add(etat);
    }

    public void supprimerEtat(Etat etat){
        etats.remove(etat);
    }
}






