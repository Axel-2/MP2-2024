package ch.epfl.cs107.icoop.enums;

// Ceci n'est pas un actor mais c'est une enum qui représente le type du joueur
// soit FIRE soit WATER
// Cette enum est utilisée un peu partout dans le reste du code
public enum Element {
    FIRE("icoop/player"),
    WATER("icoop/player2"),
    ;

    private final String spriteName;

    Element(String spriteName) {
        this.spriteName = spriteName;
    }

    // Méthode pour récupérer la valeur associée
    public String getSpriteName() {
        return spriteName;
    }

    // Méthode pour récupérer le Damage associé
    public Damage toDamage(){
        return switch(this){
            case FIRE -> Damage.FIRE;
            case WATER -> Damage.WATER;
        };
    }
}