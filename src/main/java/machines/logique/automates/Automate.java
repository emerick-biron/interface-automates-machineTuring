package machines.logique.automates;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import machines.logique.Etat;
import machines.logique.Machine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Automate extends Machine<TransitionAtmt> {
    private DoubleProperty progress;
    private ChangeListener<Integer> listenerValueTaskLancer;
    private Task<Integer> taskLancer;
    private String mot;

    public Automate(Set<Etat<TransitionAtmt>> etats) {
        super(etats);
        progress = new SimpleDoubleProperty();
    }

    public Automate() {
        super();
        progress = new SimpleDoubleProperty();
    }

    public Set<Etat<TransitionAtmt>> getEtatsActifs() {
        Set<Etat<TransitionAtmt>> etatsActifs = new HashSet<>();
        for (Etat<TransitionAtmt> etat : getEtats()) {
            if (etat.estActif()) etatsActifs.add(etat);
        }
        return etatsActifs;
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
        for (Etat<TransitionAtmt> etat : getEtatsInitiaux()) {
            bufferedWriter.write("initial " + etats.indexOf(etat));
            bufferedWriter.newLine();
        }

        //ecriture etats terminaux
        for (Etat<TransitionAtmt> etat : etats) {
            if (etat.estTerminal()) {
                bufferedWriter.write("terminal " + etats.indexOf(etat));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancer(String mot) {
        lancer(mot, 0);
    }

    /**
     * Lance l'automate
     *
     * @param mot          mot a donner a l'automate
     * @param dellayMillis temps d'attente entre chauqe etape
     */
    public void lancer(String mot, long dellayMillis) {
        this.mot = mot;
        if (taskLancer == null) {
            initTaskLancer(mot, dellayMillis);
        } else if (taskLancer.isRunning()) {
            taskLancer.cancel();
            initTaskLancer(mot, dellayMillis);
        } else {
            initTaskLancer(mot, dellayMillis);
        }
        taskLancer.setOnCancelled(getOnCancelled());
        taskLancer.setOnRunning(getOnRunning());
        taskLancer.setOnSucceeded(getOnSucceeded());
        if (listenerValueTaskLancer != null) taskLancer.valueProperty().addListener(listenerValueTaskLancer);
        new Thread(taskLancer).start();
    }

    private void initTaskLancer(String mot, long dellayMillis) {
        taskLancer = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                getEtats().forEach(Etat::desactive);
                getEtatsInitiaux().forEach(Etat::active);
                progress.set(0);
                int i = 0;
                while (getEtatsActifs().size() > 0 && i < mot.length()) {
                    Thread.sleep(dellayMillis);
                    char lettre = mot.charAt(i);
                    step(lettre);
                    updateValue(i);
                    progress.set((double) (i + 1) / mot.length());
                    i++;
                }
                return i;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean motReconnu() {
        for (Etat<TransitionAtmt> etat : getEtats()) {
            if (etat.estTerminal() && etat.estActif()) return true;
        }
        return false;
    }

    public Set<Etat<TransitionAtmt>> getEtatsInitiaux() {
        Set<Etat<TransitionAtmt>> res = new HashSet<>();
        for (Etat<TransitionAtmt> etat : getEtats()) {
            if (etat.estInitial()) res.add(etat);
        }
        return res;
    }

    private void step(char lettre) {
        Set<Etat<TransitionAtmt>> nouveauxActifs = new HashSet<>();
        getEtatsActifs().forEach(e -> nouveauxActifs.addAll(e.cible(lettre)));
        getEtatsActifs().forEach(Etat::desactive);
        nouveauxActifs.forEach(Etat::active);
    }

    /**
     * Arrete l'execution de l'automate
     */
    public void arreter() {
        if (taskLancer != null && taskLancer.isRunning()) taskLancer.cancel();
    }

    public double getProgress() {
        return progress.get();
    }

    public ReadOnlyDoubleProperty progressProperty() {
        return progress;
    }

    public String getMot() {
        return mot;
    }

    public void setListenerValueTaskLancer(ChangeListener<Integer> listenerValueTaskLancer) {
        this.listenerValueTaskLancer = listenerValueTaskLancer;
    }
}