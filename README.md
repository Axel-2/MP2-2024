# MP2-2024

# Conception 1
# welcome à la création

# Conception 2 
# Element dans mur

# To do 1
# Prendre en considération la phrase suivante du pdf. reglera le probleme du double explo car e fait deux choses
Cette création ne sera possible que si la case en face du personnage permet le
placement d’un acteur

# To do 2
# Rajouter 2 intéractions à l'explosif
L’explosif sera doté d’interactions supplémentaires :
• avec d’autres explosifs en les faisant exploser ;
• avec les murs en les détruisant.

# To do 3
# 4.1.3

# Vérifier qu'il ne manque pas une plaque de pression, avec la version update du pdf

# QUESTION A POSER JEUDI

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

- LEs unstoppbale doivent s'arreter si la Celle est en mode canFly = false ???

- # ATTENTION NE PAS OUBLIER DE METTRE 
# LE acceptInbteraction dans les nouvelles classes
