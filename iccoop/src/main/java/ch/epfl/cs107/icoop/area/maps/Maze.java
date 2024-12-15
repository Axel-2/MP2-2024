package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Collectable.Heart;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Foes.BombFoe;
import ch.epfl.cs107.icoop.actor.Foes.HellSkull;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.SpawnPosition;
import ch.epfl.cs107.icoop.enums.Element;
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


//            // TODO seulement pour debug enlever après
//            new DiscreteCoordinates(3, 10),
//            new DiscreteCoordinates(4, 10)


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

        // ----------------- PRESSURE PLATES ------------------
        PressurePlate firstPP = new PressurePlate(this, new DiscreteCoordinates(6, 33));
        registerActor(firstPP);
        PressurePlate secondPP = new PressurePlate(this, new DiscreteCoordinates(9, 25));
        registerActor(secondPP);

        // ----------------- WALLS ------------------
        // Attention aux paramètres
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,35), Element.WATER));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4,36), Element.WATER));

        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6,35), Element.FIRE, firstPP)); //pression 4
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7,36), Element.FIRE, firstPP)); //pression4

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2,34), Element.FIRE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(3,34), Element.FIRE));        

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5,24), Element.WATER));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(6,24), Element.WATER));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,21), Element.FIRE, secondPP)); //composant7
        
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8,4), Element.WATER));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13,4), Element.WATER));


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

        // ----------------- Skulls ---------------

        //(12,33), (12,31), (12,29), (12,27), (12,25),
        //(10,33), (10,32), (10,30), (10,28) et (10,26).
        DiscreteCoordinates[] skullCoordinates =  {
                new DiscreteCoordinates(12, 33),
                new DiscreteCoordinates(12, 31),
                new DiscreteCoordinates(12, 29),
                new DiscreteCoordinates(12, 27),
                new DiscreteCoordinates(12,25),
                new DiscreteCoordinates(10, 33),
                new DiscreteCoordinates(10, 32),
                new DiscreteCoordinates(10, 30),
                new DiscreteCoordinates(10, 28),
                new DiscreteCoordinates(10, 26)
        };

        for (DiscreteCoordinates skullCoordinate : skullCoordinates) {
            registerActor(new HellSkull(this, Orientation.RIGHT, skullCoordinate));
        }

        // ----------------- Artificiers ---------------
        DiscreteCoordinates[] bombFoesCoordinates =  {
                new DiscreteCoordinates(5, 15),
                new DiscreteCoordinates(6, 17),
                new DiscreteCoordinates(10, 17),
                new DiscreteCoordinates(5, 14),
        };

        for (DiscreteCoordinates bombFoeCoord : bombFoesCoordinates) {
            registerActor(new BombFoe(this, bombFoeCoord));
        }



    }

    @Override
    public String getTitle() {
        return "Maze";
    }

}