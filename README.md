# MP2-2024

# Conception 1 ---- Note des trucs à mettre dans le fichier conception
# Welcome à la création
# Element dans mur
# pas comment les overides

# To do yoann :
# Pas commenter les paramètre sencore
# Review stockable
# Je crois que mes elementalitems ont plusieurs elements


décomposer la fonction update de Icooplayer en sous-méthode

# A faire important 
- lancer des boules si y a un rocher devant très important pour l'aire Arena
- quand une boule est lancée parfois le player change de direction
- relire tous les checks du dossier et rien oublier
- clés collectés par le mauvais perso: Modif faite mais à vérifier

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
