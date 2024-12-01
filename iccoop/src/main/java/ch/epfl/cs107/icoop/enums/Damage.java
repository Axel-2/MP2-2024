package ch.epfl.cs107.icoop.enums;

public enum Damage {

    EXPLOSION(50),
        ;

    private final int damagePoints;

    Damage(int damagePoints) {
        this.damagePoints = damagePoints;
    }

    public int getDamagePoints() {
        return damagePoints;
    }
}