package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.Collectable.Heart;
import ch.epfl.cs107.icoop.actor.Collectable.Orb;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.SpawnPosition;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * A class that represent the inital spawn area
 */
public final class OrbWay extends ICoopArea {

    public OrbWay(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    // On a besoin d'une variable static car des fois,
    // on veut le spawn sans initialiser l'objet donc
    // le getter ci-dessous ne suffit pas
    static final SpawnPosition spawnPosition = new SpawnPosition(
            // FIRE
            new DiscreteCoordinates(1, 12),
            // WATER
            new DiscreteCoordinates(1, 5)
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

        DiscreteCoordinates fireSpawnReturnCoord = new DiscreteCoordinates(18, 16);
        DiscreteCoordinates waterSpawnReturnCoords = new DiscreteCoordinates(18, 15);

        // ----------------- DOORS ------------------
        Door orbWayDoor1 = new Door(
            "Spawn",                                                                    // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                              // Toujours open
            Arrays.asList(waterSpawnReturnCoords, fireSpawnReturnCoord), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                    // Map actuelle, donc Spawn
            new DiscreteCoordinates(0,14),                                                       // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(0,13),
            new DiscreteCoordinates(0,12),                                                       // Autres cellules occupées par les portes
            new DiscreteCoordinates(0,11),      
            new DiscreteCoordinates(0,10)                                                    
            );



        Door orbWayDoor2 = new Door(
            "Spawn",                                                                    // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                              // Toujours open
            Arrays.asList(waterSpawnReturnCoords, fireSpawnReturnCoord), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                    // Map actuelle, donc Spawn
            new DiscreteCoordinates(0,8),                                                        // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(0,7),
            new DiscreteCoordinates(0,6),                                                        // Autres cellules occupées par les portes
            new DiscreteCoordinates(0,5),      
            new DiscreteCoordinates(0,4)                                                    
            );
        
        registerActor(orbWayDoor1);
        registerActor(orbWayDoor2);

        // ----------------- ORBS ------------------
        DiscreteCoordinates fireOrbCoord = new DiscreteCoordinates(17, 12);
        DiscreteCoordinates waterOrbCoord = new DiscreteCoordinates(17, 6);
        Orb fireOrb = new Orb(this, fireOrbCoord, Orb.OrbType.FIRE);
        Orb waterOrb = new Orb(this, waterOrbCoord, Orb.OrbType.WATER);

        registerActor(fireOrb);
        registerActor(waterOrb);

        // ----------------- WALLS ------------------
        for (int i = 0; i < 5; i++) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10+i), "fire_wall", Logic.TRUE));
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4+i), "water_wall", Logic.TRUE));
        }

        // Deux murs de tests
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6), "fire_wall", Logic.TRUE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), "water_wall", Logic.TRUE));
        
        // ----------------- COEURS ------------------
        DiscreteCoordinates[] heartPositions = {
                new DiscreteCoordinates(8, 4),
                new DiscreteCoordinates(10, 6),
                new DiscreteCoordinates(5, 13),
                new DiscreteCoordinates(10, 11),
        };

        for (DiscreteCoordinates heartPosition : heartPositions) {
            registerActor(new Heart(this, heartPosition));
        }

        // ----------------- PRESSURE PLATES ------------------
        registerActor(new PressurePlate(this, new DiscreteCoordinates(5, 7)));
        registerActor(new PressurePlate(this, new DiscreteCoordinates(5, 10)));



    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }



    

}