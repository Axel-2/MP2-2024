package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Collectable.Heart;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.SpawnPosition;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

/**
 * A class that represent the inital spawn area
 */
public final class Maze extends ICoopArea {

    // Spawn positions
    static final SpawnPosition spawnPosition = new SpawnPosition(
            // FIRE
            new DiscreteCoordinates(2, 39),
            // WATER
            new DiscreteCoordinates(3, 39)
    );



    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(Element elementType) {
        DiscreteCoordinates coordinates = switch (elementType) {
            case FIRE -> spawnPosition.getFireSpawn();
            case WATER -> spawnPosition.getWaterSpawn();
        };

        return coordinates;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        //DiscreteCoordinates fireSpawnReturnCoord = new DiscreteCoordinates(18, 16); c'est quoi ça ? 
        //DiscreteCoordinates waterSpawnReturnCoords = new DiscreteCoordinates(18, 15);

        // ----------------- DOORS ------------------

        // ----------------- ORBS ------------------

        // ----------------- WALLS ------------------
        // Attention aux paramètres
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,35), "water_wall", null));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,36), "water_wall", null));

        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,35), "fire_wall", null));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7,36), "fire_wall", null));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2,34), "fire_wall", null));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(3,34), "fire_wall", null));        

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5,24), "water_wall", null));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(6,24), "water_wall", null));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,21), "fire_wall", null)); 
        
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,4), "water_wall", null));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13,4), "water_wall", null));
        // ----------------- COEURS ------------------
        DiscreteCoordinates[] heartPositions = {
                new DiscreteCoordinates(15, 18),
                new DiscreteCoordinates(16, 19),
                new DiscreteCoordinates(14, 19),
                new DiscreteCoordinates(14, 17),
        };

        for (DiscreteCoordinates heartPosition : heartPositions) {
            registerActor(new Heart(this, heartPosition));
        }
        // ----------------- Explosifs ---------------
        registerActor(new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(6, 25), 50));
        // ----------------- PRESSURE PLATES ------------------
        registerActor(new PressurePlate(this, new DiscreteCoordinates(6, 33)));
        registerActor(new PressurePlate(this, new DiscreteCoordinates(9, 25)));

    }

    @Override
    public String getTitle() {
        return "Maze";
    }

}