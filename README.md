# Projet S3 Interface pour automates et machine de Turing

![logo_fac](ressources/logo_fac.png)
![logo_iut](ressources/logo_iut.png)
## Introduction :

**Membres :**

![equipe](ressources/groupe.png)

- Emerick Biron
- Albin Moret
- Alexandre Roussel
- Lénais Desbos

**Tuteur : Matthieu Rosenfeld**

## Manuels :

Lors du lancement de l'application une fenêtre s'ouvrira et vous permettra de choisir de lancer l'application de gestion
d'automates [[1]](#fentre-de-dpart) ou l'application de gestion de machines de Turing [[2]](#fentre-de-dpart)

##### Fenêtre de départ :

![fenetre_depart](ressources/fenetre_depart.png)

### Manuel automates :

#### Les états :

Pour ajouter un état à l'automate, il suffit de cliquer sur le bouton "ajouter état" [[3]](#interface-dun-automate-).
Pour qu'un état soit initial ou final (représenté par les - et les + à côté des états) [[4]](#interface-dun-automate-),
il suffit de cocher les cases "initial" ou "terminal" [[5]](#interface-dun-automate-) avant de créer l'état, ou après
avoir sélectionné l'état que l'on souhaite modifier (si plusieurs états sont sélectionnés, seul le dernier à avoir été
sélectionné sera modifié). Pour déplacer un état, il suffit de le faire glisser avec la souris.

#### Les transitions :

Pour ajouter une transition, il faut d'abord sélection l'état d'arrivée puis l'état de départ (ou seulement un état pour
une auto-transition). Il faut ensuite ajouter la lettre de la transition dans le cadre qui apparait en haut à
droite [[6]](#ajout-dune-transition-) puis cliquer sur le bouton "ajouter transition" [[7]](#ajout-dune-transition-).

#### La sélection :

Pour sélectionner un état ou une transition il suffit de cliquer dessus. Cet élément aura donc un contour bleu. Il faut
maintenir la touche "Ctrl" enfoncé et cliquer sur un autre élément afin de sélectionner plusieurs éléments.

#### La suppression :

Un appui sur le bouton "Supprimer" [[8]](#interface-dun-automate-) supprimera tous les éléments sélectionnés. Dans le
cas où plusieurs transitions entre deux mêmes états seraient sélectionnées [[9]](#suppression-de-multiples-transition-)
une [fenêtre de dialogue](#suppression-de-multiples-transition-) s'ouvrira afin de vous permettre de choisir
spécifiquement les transitions à supprimer.

#### Le lancement d'un automate :

Pour lancer un automate il faut écrire le mot a tester dans la zone prévue à cet effet [[10]](#interface-dun-automate-)
puis appuyer sur le bouton "Lancer" [[11]](#interface-dun-automate-). Vous pouvez
retrouver [ici](#lancement-dun-automate) un exemple.

#### Informations complémentaires :

Il est possible de charger [[12]](#interface-dun-automate-) ou de sauvegarder [[13]](#interface-dun-automate-) un
automate sous la forme de fichiers _.atmt_. Il est aussi possible de supprimer l'ensemble d'un automate en appuyant sur
le bouton "Clear" [[14]](#interface-dun-automate-).

##### Interface d'un automate :

![fenetre_automate](ressources/fenetre_automate.PNG)

##### Ajout d'une transition :

![ajout_trans](ressources/ajout_trans.PNG)

##### Suppression de multiples transition :

![plusieurs_trans_select](ressources/plusieurs_trans_select.PNG)
![dialog_sup_trans](ressources/dialog_sup_trans.PNG)

##### Lancement d'un automate :

![lancement_automate](ressources/lancement_automate.gif)