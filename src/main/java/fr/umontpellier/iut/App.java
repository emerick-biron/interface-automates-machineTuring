package fr.umontpellier.iut;

import java.util.*;

public class App {
    public static void main(String[] args) {
        Etat e1 = new Etat("1", true, false);
        Etat e2 = new Etat("2", false, false);
        Etat e3 = new Etat("3", false, false);
        Etat e4 = new Etat("4", false, true);

        Transition t1 = new Transition(Collections.singletonList('b'), e1, e2);
        Transition t2 = new Transition(Collections.singletonList('a'), e1, e3);
        Transition t3 = new Transition(Arrays.asList('a', 'b'), e3, e4);
        Transition t4 = new Transition(Arrays.asList('a', 'b'), e2, e2);

        List<Transition> transitions = Arrays.asList(t1, t2, t3, t4);
        List<Etat> etats = Arrays.asList(e1, e2, e3, e4);

        Mot mot1 = new Mot(Arrays.asList('a', 'b'));
        Mot mot2 = new Mot(Arrays.asList('b', 'a', 'a'));

        Automate automate = new Automate(etats, transitions);
    }
}