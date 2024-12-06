# MP2-2024

# Conception 2 : 
# element dans mur

# Remarque 1 
# A chaque fois je recrée un keyboard (dans l'interaction du icoop avec l'explosif par exemple), pas sûr que ce soit the way to go

# Remarque 2
# Deux méthodes dans ElementalWall ne sont pas détaillées dans le pdf : get field of view et getcurrentcells
# Pour getcurrentcells je vais copié collé un truc classique, mais pour get field of view j'ai tenté de dire qu'on s'en foutait car 
# les murs n'ont que des intéractions de contacts, donc j'ai retourné un array vide mais y'a l'option de copier coller celui Explosif.java qui est détaillé

# Conception 1
# Dialog Handler dans spawn je pense ? ou init


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
- REPONSE: 
- 
- le Handler du Icoop devient enorme. Normale ? comment arranger ca.
- Je pense qu'il faut créer plus souvent des Handlers directement dans la 
- classe de l'actore que de directement tout gérer dans IcoopPlayer
- les coeurs et orb réaparraisent quand on part et on revient dans la map ->
- CAD EST-ce que faut tout gerer dans Icoop ?
- 
- # ATTENTION NE PAS OUBLIER DE METTRE 
# LE acceptInbteraction dans les nouvelles classes
