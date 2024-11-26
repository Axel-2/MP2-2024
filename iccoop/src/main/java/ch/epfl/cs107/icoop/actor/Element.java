package ch.epfl.cs107.icoop.actor;

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

}