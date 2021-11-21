package machines.automates.gui;

import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.VueMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends
        VueMachine<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt, TransitionAtmt> {
    private Automate automate;
    private VuePrincipaleAtmt vuePrincipale;
    private ObservableList<VueEtatAtmt> vuesEtatSelectionnes = FXCollections.observableArrayList();
    private ObservableList<VueTransitionAtmt> vuesTransitionSelectionnes = FXCollections.observableArrayList();
    private ListChangeListener<EtatAtmt> miseAJourEtats = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (EtatAtmt e : change.getAddedSubList()) {
                    getChildren().add(new VueEtatAtmt(e, VueAutomate.this));
                }
            } else if (change.wasRemoved()) {
                for (EtatAtmt e : change.getRemoved()) {
                    getChildren().remove(getVueEtat(e));
                }
            }

        }
        for (Node n : getChildren()) {
            if (n instanceof VueEtatAtmt) {
                VueEtatAtmt vueEtat = (VueEtatAtmt) n;
                vueEtat.setLabelNumEtat(automate.etatsProperty().indexOf(vueEtat.getEtat()));
            }
        }
    };
    private ListChangeListener<TransitionAtmt> miseAJourTransition = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (TransitionAtmt t : change.getAddedSubList()) {
                    VueTransitionAtmt vueTransition = new VueTransitionAtmt(t, VueAutomate.this);
                    getChildren().add(vueTransition);
                    vueTransition.toBack();
                }
            } else if (change.wasRemoved()) {
                for (TransitionAtmt t : change.getRemoved()) {
                    getChildren().remove(getVueTransition(t));
                }
            }
        }
        for (TransitionAtmt t : automate.getTransitions()) {
            int n = 0;
            for (int i = 0; i < automate.getTransitions().indexOf(t); i++) {
                TransitionAtmt t2 = automate.getTransitions().get(i);
                if (t.getEtatDepart() == t2.getEtatDepart() && t.getEtatArrivee() == t2.getEtatArrivee()) {
                    n++;
                }
            }
            VueTransitionAtmt vueTransition = getVueTransition(t);
            if (n > 0) {
                vueTransition.setFlechesVisible(false);
                vueTransition.toFront();
            } else {
                vueTransition.setFlechesVisible(true);
                vueTransition.toBack();
            }
            vueTransition.positionnerLabelEtiquette(n);
        }
    };
    private ListChangeListener<VueEtatAtmt> miseAJourVuesEtatSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueEtatAtmt vueEtat : change.getAddedSubList()) {
                    vueEtat.getCercle().setStroke(Color.valueOf("#003576"));
                    vueEtat.getCercle().setStrokeType(StrokeType.INSIDE);
                    vueEtat.getCercle().setStrokeWidth(3);
                }
            }
            if (change.wasRemoved()) {
                for (VueEtatAtmt vueEtat : change.getRemoved()) {
                    vueEtat.getCercle().setStroke(null);
                }
            }
        }
        vuePrincipale.getBoutonAjouterTransition()
                .setVisible(vuesEtatSelectionnes.size() <= 2 && vuesEtatSelectionnes.size() >= 1);
        vuePrincipale.getTextFieldEtiquette()
                .setVisible(vuesEtatSelectionnes.size() <= 2 && vuesEtatSelectionnes.size() >= 1);
    };
    private ListChangeListener<VueTransitionAtmt> miseAJourVuesTransitionSelectionnes = change -> {
        while (change.next()) {
            if (change.wasAdded()) {
                for (VueTransitionAtmt vueTransition : change.getAddedSubList()) {
                    vueTransition.setCouleurSelection(true);
                }
            }
            if (change.wasRemoved()) {
                for (VueTransitionAtmt vueTransition : change.getRemoved()) {
                    vueTransition.setCouleurSelection(false);
                }
            }
        }
    };

    public VueAutomate(Automate automate, VuePrincipaleAtmt vuePrincipale) {
        this.vuePrincipale = vuePrincipale;
        this.automate = automate;
        this.automate.etatsProperty().addListener(miseAJourEtats);
        this.automate.transitionsProperty().addListener(miseAJourTransition);
        vuesEtatSelectionnes.addListener(miseAJourVuesEtatSelectionnes);
        vuesTransitionSelectionnes.addListener(miseAJourVuesTransitionSelectionnes);

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getTarget() == this) {
                deSelectionnerVues();
                vuePrincipale.unbindCheckBoxes();
            }
        });
    }

    public ObservableList<VueTransitionAtmt> getVuesTransitionSelectionnes() {
        return vuesTransitionSelectionnes;
    }

    public ObservableList<VueEtatAtmt> getVuesEtatSelectionnes() {
        return vuesEtatSelectionnes;
    }

    public VuePrincipaleAtmt getVuePrincipale() {
        return vuePrincipale;
    }

    public Automate getAutomate() {
        return automate;
    }

    public VueEtatAtmt getVueEtat(EtatAtmt etat) {
        for (Node n : getChildren()) {
            if (n instanceof VueEtatAtmt) {
                VueEtatAtmt vueEtat = (VueEtatAtmt) n;
                if (vueEtat.getEtat().equals(etat)) return vueEtat;
            }
        }
        return null;
    }

    public VueTransitionAtmt getVueTransition(TransitionAtmt transition) {
        for (Node n : getChildren()) {
            if (n instanceof VueTransitionAtmt) {
                VueTransitionAtmt vueTransition = (VueTransitionAtmt) n;
                if (vueTransition.getTransition().equals(transition)) return vueTransition;
            }
        }
        return null;
    }

    public ArrayList<VueTransitionAtmt> getVuesTransition(VueEtatAtmt vueEtat1, VueEtatAtmt vueEtat2) {
        ArrayList<VueTransitionAtmt> res = new ArrayList<>();
        for (TransitionAtmt t : vueEtat1.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat2.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        for (TransitionAtmt t : vueEtat2.getEtat().getListeTransitions()) {
            if (t.getEtatArrivee() == vueEtat1.getEtat()) {
                if (!res.contains(getVueTransition(t))) res.add(getVueTransition(t));
            }
        }
        return res;
    }

    public void clear() {
        vuesEtatSelectionnes.clear();
        vuesTransitionSelectionnes.clear();
        List<EtatAtmt> etats = automate.getEtats();
        if (!etats.isEmpty()) {
            int l = etats.size();
            for (int i = 0; i < l; i++) {
                automate.supprimerEtat(etats.get(0));
            }
        }
    }

    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        automate.chargerFichier(nomFichier);

        FileReader fr = new FileReader(nomFichier);
        BufferedReader bf = new BufferedReader(fr);

        String ligne = bf.readLine();

        while (!(ligne == null || ligne.contains("###"))) {
            ligne = bf.readLine();
        }

        ligne = bf.readLine();

        while (ligne != null) {
            String[] split = ligne.split(" ");

            if (split.length >= 3) {

                int numEtat = Integer.parseInt(split[0]);
                double xPos = Double.parseDouble(split[1]);
                double yPos = Double.parseDouble(split[2]);

                EtatAtmt etat = automate.getEtats().get(numEtat);
                VueEtatAtmt vueEtat = getVueEtat(etat);

                //Permet de faire en sorte que la vue etat ne sorte pas de la vue automate
                double taille = vueEtat.getCercle().getRadius() * 2 + 20;
                if (xPos >= 0 && xPos + taille <= getBoundsInLocal().getMaxX()) {
                    vueEtat.setLayoutX(xPos);
                }
                if (yPos >= 0 && yPos + taille <= getBoundsInLocal().getMaxY() - 50) {
                    vueEtat.setLayoutY(yPos);
                }

            }
            ligne = bf.readLine();
        }


        bf.close();
        fr.close();
    }

    public void sauvegarder(String nomFichier) throws IOException {
        automate.sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        List<EtatAtmt> listEtats = automate.getEtats();

        for (EtatAtmt e : listEtats) {
            VueEtatAtmt vueEtat = getVueEtat(e);
            bufferedWriter.write(listEtats.indexOf(e) + " " + vueEtat.getLayoutX() + " " + vueEtat.getLayoutY());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    public void deSelectionnerVues() {
        deSelectionnerVuesEtat();
        deSelectionnerVuesTransition();
    }

    public void deSelectionnerVuesEtat() {
        for (EtatAtmt e : automate.getEtats()) {
            VueEtatAtmt vueEtat = getVueEtat(e);
            vueEtat.deSelectionner();
        }
    }

    public void deSelectionnerVuesTransition() {
        for (TransitionAtmt t : automate.getTransitions()) {
            VueTransitionAtmt vueTransition = getVueTransition(t);
            vueTransition.deSelectionner();
        }
    }
}




































