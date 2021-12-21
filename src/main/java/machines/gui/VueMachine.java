package machines.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.logique.Etat;
import machines.logique.Machine;
import machines.logique.Transition;

import java.io.*;
import java.util.ArrayList;

public abstract class VueMachine<T extends Transition<T>> extends Pane {
    private Machine<T> machine;
    private VuePrincipale<T> vuePrincipale;
    private ObservableList<VueEtat<T>> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ObservableList<VueTransition<T>> vuesTransitionSelectionnes = FXCollections.observableArrayList();
    private SetChangeListener<Etat<T>> miseAJourEtats = change -> {
        if (change.wasAdded()) {
            VueEtat<T> vueEtat = new VueEtat<>(change.getElementAdded(), VueMachine.this);
            vueEtat.setLabelNumEtat(machine.getEtats().size() - 1);
            getChildren().add(vueEtat);
        } else if (change.wasRemoved()) {
            VueEtat<T> vueEtatRemoved = getVueEtat(change.getElementRemoved());
            getChildren().remove(vueEtatRemoved);
            for (Node n : getChildren()) {
                if (n instanceof VueEtat) {
                    VueEtat<T> vueEtat = (VueEtat<T>) n;
                    if (vueEtat.getNumEtat() > vueEtatRemoved.getNumEtat()) {
                        vueEtat.setLabelNumEtat((vueEtat.getNumEtat() - 1));
                    }
                }
            }

        }
    };
    private ListChangeListener<VueEtat<T>> miseAJourVuesEtatSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueEtat<T> vueEtat : change.getAddedSubList()) {
                    vueEtat.getCercle().setStroke(Color.valueOf("#003576"));
                    vueEtat.getCercle().setStrokeType(StrokeType.INSIDE);
                    vueEtat.getCercle().setStrokeWidth(3);
                }
            }
            if (change.wasRemoved()) {
                for (VueEtat<T> vueEtat : change.getRemoved()) {
                    vueEtat.getCercle().setStroke(null);
                }
            }
        }
    };
    private ListChangeListener<VueTransition<T>> miseAJourVuesTransitionSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueTransition<T> vueTransition : change.getAddedSubList()) {
                    vueTransition.setCouleurSelection(true);
                }
            }
            if (change.wasRemoved()) {
                for (VueTransition<T> vueTransition : change.getRemoved()) {
                    vueTransition.setCouleurSelection(false);
                }
            }
        }
    };

    public VueMachine(Machine<T> machine, VuePrincipale<T> vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.machine = machine;

        initListeners();

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                deSelectionnerVues();
                vuePrincipale.unbindCheckBoxes();
            }
        });
    }

    private void initListeners() {
        machine.etatsProperty().addListener(miseAJourEtats);
        vuesEtatSelectionnes.addListener(miseAJourVuesEtatSelectionnes);
        vuesTransitionSelectionnes.addListener(miseAJourVuesTransitionSelectionnes);
    }

    /**
     * Ajoute une vueTransition a la vue machine
     *
     * @param transition transition de la vueTransition a ajouter
     */
    public abstract void ajoutVueTransition(T transition);

    /**
     * Supprime une vue transition a la vue machine
     *
     * @param transition transition de la vueTransition a supprimer
     */
    public void supprimerVueTransition(T transition) {
        VueTransition<T> vueTransition = getVueTransition(transition);
        if (vueTransition != null) {
            getChildren().remove(vueTransition);
            ArrayList<VueTransition<T>> vueTransitions =
                    getVuesTransition(vueTransition.getVueEtatDep(), vueTransition.getVueEtatFin());
            for (int i = 0; i < vueTransitions.size(); i++) {
                vueTransitions.get(i).positionnerLabelEtiquette(i);
            }
        }
    }

    public Machine<T> getMachine() {
        return machine;
    }

    public ObservableList<VueTransition<T>> getVuesTransitionSelectionnes() {
        return vuesTransitionSelectionnes;
    }

    public ObservableList<VueEtat<T>> getVuesEtatSelectionnes() {
        return vuesEtatSelectionnes;
    }

    public VuePrincipale<T> getVuePrincipale() {
        return vuePrincipale;
    }

    /**
     * Permet d'obtenir la vueEtat correspondante a l'etat
     *
     * @param etat etat de la vueEtat
     * @return vueEtat correspondante
     */
    public VueEtat<T> getVueEtat(Etat<T> etat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtat) {
                VueEtat<T> vueEtat = (VueEtat<T>) n;
                if (vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    /**
     * Permet d'obtenir la vueTransition correspondante a la transition
     *
     * @param transition transition de la vueTransition
     * @return vueTransition correspondante
     */
    public VueTransition<T> getVueTransition(T transition) {
        for (Node n : getChildren()) {
            if (n instanceof VueTransition) {
                VueTransition<T> vueTransition = (VueTransition<T>) n;
                if (vueTransition.getTransition().equals(transition)) return vueTransition;
            }
        }
        return null;
    }

    /**
     * Peremet d'obtenir la liste de vueTransitions ayant les etats en parametre comme borne
     *
     * @param vueEtat1 etat de de depart
     * @param vueEtat2 etat d'arrivee
     * @return liste des vueTransition ou null si il n'y en a pas
     */
    public ArrayList<VueTransition<T>> getVuesTransition(VueEtat<T> vueEtat1, VueEtat<T> vueEtat2) {
        ArrayList<VueTransition<T>> res = new ArrayList<>();
        for (T t : vueEtat1.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat2.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        for (T t : vueEtat2.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat1.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        return res;
    }

    /**
     * Supprime tous les element de la vueMachine
     */
    public void clear() {
        vuesEtatSelectionnes.clear();
        vuesTransitionSelectionnes.clear();
        machine.clear();
    }

    /**
     * Permet de creer une machine a partir d'un fichier
     *
     * @param nomFichier nom du fichier contenant la machine
     * @throws IOException
     */
    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        machine.chargerFichier(nomFichier);

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            ligne = bf.readLine();
        }

        ligne = bf.readLine();

        ArrayList<Etat<T>> etats = new ArrayList<>(machine.getEtats());

        while (ligne != null) {
            String[] split = ligne.split(" ");

            if (split.length >= 3) {

                int numEtat = Integer.parseInt(split[0]);
                double xPos = Double.parseDouble(split[1]);
                double yPos = Double.parseDouble(split[2]);
                int labelNumEtat = Integer.parseInt(split[3]);

                Etat<T> etat = etats.get(numEtat);
                VueEtat<T> vueEtat = getVueEtat(etat);

                if (vueEtat != null) {
                    //Permet de faire que les coordonnées de la vue etat soient positives
                    double taille = vueEtat.getCercle().getRadius() * 2 + 20;
                    if (xPos >= 0) {
                        vueEtat.setLayoutX(xPos);
                    }
                    if (yPos >= 0) {
                        vueEtat.setLayoutY(yPos);
                    }

                    vueEtat.setLabelNumEtat(labelNumEtat);
                }
            }
            ligne = bf.readLine();
        }


        bf.close();
        fr.close();
    }

    /**
     * Perme de sauvegarder la machine dans un fichier
     *
     * @param nomFichier nom du fichier dans lequel sauvegarder la machine
     * @throws IOException
     */
    public void sauvegarder(String nomFichier) throws IOException {
        machine.sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        ArrayList<Etat<T>> etats = new ArrayList<>(machine.getEtats());

        for (Etat<T> e : etats) {
            VueEtat<T> vueEtat = getVueEtat(e);
            if (vueEtat != null) {
                bufferedWriter.write(etats.indexOf(e) + " " + vueEtat.getLayoutX() + " " +
                        vueEtat.getLayoutY() + " " + vueEtat.getLabelNumEtat().getText());
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    /**
     * Deselectionne toutes les vues etat et les vue transitions
     */
    public void deSelectionnerVues() {
        deSelectionnerVuesEtat();
        deSelectionnerVuesTransition();
    }

    /**
     * Deselectionne toutes les vues etats
     */
    public void deSelectionnerVuesEtat() {
        for (Etat<T> e : machine.getEtats()) {
            VueEtat<T> vueEtat = getVueEtat(e);
            if (vueEtat != null) vueEtat.deSelectionner();
        }
    }

    /**
     * Deselectionne toutes les vue transitions
     */
    public void deSelectionnerVuesTransition() {
        for (T t : machine.getTransitions()) {
            VueTransition<T> vueTransition = getVueTransition(t);
            if (vueTransition != null) vueTransition.deSelectionner();
        }
    }
}