# MP2-2024

# Conception 1 ---- Note des trucs à mettre dans le fichier conception
# Welcome à la création
# Element dans mur


# remaque:
quand on pose une bombe elle reste affiché dans l'inventaire
firestaff et waterstaff décomposer avec une fonction

# Remarque 1 :
j'ai passé les murs en isViewInteractable true pour que l'explo puisse se poser une case à côté

# Remarque 2 :
J'avais créé un système de isStockable mais au final j'ai trouvé plus simple, mais peut -être moins bien. donc je laisse la variable stockable en attendant
là je gère le +1 de quantité dans l'intéraction

# A voir : 
Supprimer les doubles checks dans manageuseitem (explo et baton copier collé)

# Remarque 3 :
dans le mangeuseitem j'ai ctrl c ctrl v deux fois, y'a surement moyen de faire mieux ( en plus de "A voir" au dessus)

# Question 1 : 
Pour le 4.3.3, le perso va aussi devoir avoir un state, est-ce que je fais le même principe que ton private enum du bombfoe , ou est-ce quon fait 1 enum pour tous ? 

# TODO YOANN :
4.3.3 + debug ou clean des trucs, notamment le manageuseitem

# TO DO 
regler probleme caillou disparait alors qu'on a deja enlevé la bombe
le pb et que la flamme active la bombe alors qu'on la deja collect
il faut donc en qq sort l'unregister ??


# Vérifier qu'il ne manque pas une plaque de pression, avec la version update du pdf

# QUESTION A POSER VENDREDI

- DialogHandler a qui le passer attribut publique etc...
- REPONSE: ON LE MET A CHAQUE FOIS EN ATTRIBUT SI LE SOUS OBJET A BESOIN
- 
- bomb.collect() comment evite de repeter
- REPONSE: PAS D'OPTION FAUT LE FAIRE A CHAQUE FOIS

- le acceptIbteraction on le remet partout ???
- REPONSE: 
- 
- faut mettre a chaque fois chaque fonction dans le handler ???
- REPONSE: 
- 
- Demander pour les bords du jeu c'est moche de fou
- REPONSE: ON S'ENFOUT
- 
- le Handler du Icoop devient enorme. Normale ? comment arranger ca.
- Je pense qu'il faut créer plus souvent des Handlers directement dans la 
- classe de l'actore que de directement tout gérer dans IcoopPlayer

- les coeurs et orb réaparraisent quand on part et on revient dans la map ->
- REPONSE forebegin en false

- Demander à JAMILA pour les valeurs par défauts non statiques si c'est mieux de les initialiser 
- dans le constructeur ou pas

# TO DO
- evtl si ca marche changer les counters avec le deltaTime mais je suis pas sur
- que ca marche encore
- UPDATE: J'AI CHANGE LE COUNTER DE BOMBE ET CA MARCHE


- LEs unstoppbale doivent s'arreter si la Celle est en mode canFly = false ???


- # ATTENTION NE PAS OUBLIER DE METTRE 
# LE acceptInbteraction dans les nouvelles classes
