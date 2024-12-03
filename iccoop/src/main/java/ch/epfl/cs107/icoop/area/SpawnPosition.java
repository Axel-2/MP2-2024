package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;


public class SpawnPosition {

    DiscreteCoordinates fireSpawn;
    DiscreteCoordinates waterSpawn;

    public SpawnPosition(DiscreteCoordinates fireSpawn, DiscreteCoordinates waterSpawn) {
        this.fireSpawn = fireSpawn;
        this.waterSpawn = waterSpawn;
    }

    public DiscreteCoordinates getWaterSpawn() {
        return waterSpawn;
    }

    public DiscreteCoordinates getFireSpawn() {
        return fireSpawn;
    }
}
