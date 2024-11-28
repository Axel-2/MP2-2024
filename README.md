# MP2-2024

# Remarque 1
# "la méthode draw de door ne dessine rien par défaut" sous entends que je dois redéfinir une méthode draw qui ne fait rien
# sauf que door est une sous classe d'un truc qui implémente "Actor", et dans Actor y'a une méthode avec le mot clé "default" draw() qui ne fait rien, donc je suppose que c'est bon
# car il ne faut pas redéfinir des méthodes qui ont le default (sauf si on veut changer ce qu'elle font mais ici c'est pas le cas). c'est juste bizarre qu'ils citent cette methode 

# Remarque 2
# J'ai créé mes portes dans "initGame", là où tu crées les personnages, ça me paraît le plus logique

# Proposition 1 
# Je propose qu'on commente en français, pour distinguer notre commentaire de celui fourni

# TODO  1 et 2 :
# Dans l'initialisation des deux portes de OrbWay, je ne sais pas comment, pour le 4ème paramètre, mettre la map OrbWay, car seul spawn a été initialisé (même si on a create les deux)
# J'ai mis des commentaires TODO 1 et TODO 2 dans ICoop.Java ligne 90 et 103 actuellement


# Question 1
# J'ai pas trop compris à quoi sert le deuxième paramètres, dans toutes les méthodes acceptInterraction Interactable other, boolean isCellInteraction

# TODO 4 :
# Ici c'est la vrai première galère, pour définir concretement ce que fait la porte, dans l'interactionHandler de IcoopPlayer (même micha a galéré et pleins de gens sont bloqués la)
# Contexte : PDF dernier paragraphe avant le 2.4.3, donc le haut de la page 13
# J'ai mis des commentaires pour donner plus d'infos à partir de la ligne 201 de ICoop Player

# CHECKPOINT : ME SUIS PAS ARRÊTER A UN ENDROIT PARTICULIER, IL FAUT JUSTE FAIRE LE TODO 1 ET 2, PUIS LE TODO 4. CEST NORMAL SI SPECIFIC INTERACTION EST SPAM, CAR TODO 4 N'ESTPAS FINI