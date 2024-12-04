# MP2-2024

# Remarque 1 
# A chaque fois je recrée un keyboard (dans l'interaction du icoop avec l'explosif par exemple), pas sûr que ce soit the way to go

# Remarque 2
# J'ai modifié la méthode can Leave pour résoudre un bug. mais par contre bizarre car sa version fonctionnelle n'utilise pas le paramètre entity.
# J'ai essayé de faire une version avec mais ça marchait pas

# Remarque 3
# Deux méthodes dans ElementalWall ne sont pas détaillées dans le pdf : get field of view et getcurrentcells
# Pour getcurrentcells je vais copié collé un truc classique, mais pour get field of view j'ai tenté de dire qu'on s'en foutait car 
# les murs n'ont que des intéractions de contacts, donc j'ai retourné un array vide mais y'a l'option de copier coller celui Explosif.java qui est détaillé