package machines.automates.gui;

import machines.automates.logique.Automate;
import machines.automates.logique.EtatAtmt;
import machines.automates.logique.TransitionAtmt;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import machines.gui.VueMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VueAutomate extends
        VueMachine<VuePrincipaleAtmt, VueAutomate, VueEtatAtmt, VueTransitionAtmt, Automate, EtatAtmt, TransitionAtmt> {
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
                vueEtat.setLabelNumEtat(getAutomate().etatsProperty().indexOf(vueEtat.getEtat()));
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
        for (TransitionAtmt t : getAutomate().getTransitions()) {
            int n = 0;
            for (int i = 0; i < getAutomate().getTransitions().indexOf(t); i++) {
                TransitionAtmt t2 = getAutomate().getTransitions().get(i);
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
        getVuePrincipale().getBoutonAjouterTransition()
                .setVisible(getVuesEtatSelectionnes().size() <= 2 && getVuesEtatSelectionnes().size() >= 1);
        getVuePrincipale().getTextFieldEtiquette()
                .setVisible(getVuesEtatSelectionnes().size() <= 2 && getVuesEtatSelectionnes().size() >= 1);
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
        super(automate, vuePrincipale);
        initListeners();
    }

    @Override
    public void initListeners() {
        getAutomate().etatsProperty().addListener(miseAJourEtats);
        getAutomate().transitionsProperty().addListener(miseAJourTransition);
        getVuesEtatSelectionnes().addListener(miseAJourVuesEtatSelectionnes);
        getVuesTransitionSelectionnes().addListener(miseAJourVuesTransitionSelectionnes);
    }

    public Automate getAutomate() {
        return super.getMachine();
    }

    public void chargerFichier(String nomFichier) throws IOException {
        clear();
        getAutomate().chargerFichier(nomFichier);

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

                EtatAtmt etat = getAutomate().getEtats().get(numEtat);
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
        getAutomate().sauvegarder(nomFichier);

        Writer fileWriter = new FileWriter(nomFichier, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("###");
        bufferedWriter.newLine();

        List<EtatAtmt> listEtats = getAutomate().getEtats();

        for (EtatAtmt e : listEtats) {
            VueEtatAtmt vueEtat = getVueEtat(e);
            bufferedWriter.write(listEtats.indexOf(e) + " " + vueEtat.getLayoutX() + " " + vueEtat.getLayoutY());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }
}




































