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

# TODO 3 :
# La méthode wantviewInterraction dans IcoopPlayer, il faut qu'elle retourne true seulement si le joueur appuie sur la touche "use Item"
# C'est le paragraphe du milieu de la page 12, section 2.4.2. je sais pas trop comment faire, j'ai pas demandé à chat gpt encore mais peut être que tu sais car t'as déjà utiliser
# les keybindings