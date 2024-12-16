#### CONCEPTION ####

Nous sommes restés pour la plus grande partie du temps fidèle
à la conception proposée par l'énoncé. Cependant, certains aspects diffèrent : 

# Elemental Walls
Nous n'avons pas créé deux sous classes "Firewall" et "Waterwall".
Nous avons préferé crée qu'une seule classe Wall, et gérer son élément à
l'aide d'un attribut, qui influencera son sprite et ses droits d'interactions.

Cette même organisation a été utilisée pour les "Staffs"

# Dialogue de début
Il nous a semblé bon de publier le premier dialog à l'initialisation du jeu
dans la méthode initGame de "ICoop.java", et non pas dans le fichier d'une map spécifique, au cas où nous
souhaiterions changer de carte de départ plus tard.