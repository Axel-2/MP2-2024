package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.DialogDoor;
import ch.epfl.cs107.icoop.actor.Collectable.Staff;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Projectiles.Fire;
import ch.epfl.cs107.icoop.actor.Rock;
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
public final class Spawn extends ICoopArea {

    // Position de départs
    static final SpawnPosition spawnPosition = new SpawnPosition(new DiscreteCoordinates(14, 6), new DiscreteCoordinates(13, 6));

    // Gestionnaire de Dialogue
    private final DialogHandler dialogHandler;

    /**
     *  Constructeur de Spawn
     */
    public Spawn(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }


    @Override
    public  DiscreteCoordinates getPlayerSpawnPosition(Element elementType) {
        DiscreteCoordinates coordinates = switch (elementType) {
            case FIRE -> spawnPosition.getFireSpawn();
            case WATER -> spawnPosition.getWaterSpawn();
        };
        return coordinates;
    }

    @Override
    protected void createArea() {

        // Back et Foregrounds
        registerActor(new Background(this));
        registerActor(new Foreground(this));


        // PORTES
        Door toOrbWayDoor = new Door(
            "OrbWay",                                                                // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                           // Toujours open
            Arrays.asList(OrbWay.spawnPosition.getFireSpawn(), OrbWay.spawnPosition.getWaterSpawn()), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                 // Map actuelle, donc Spawn
            new DiscreteCoordinates(19,15),                                                   // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(19,16)                                                    // Autre cellule de la porte (l'autre "case" rouge)
            );

        Door toMazeDoor = new Door(
            "Maze",                                                                // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                           // Toujours open
            Arrays.asList(Maze.spawnPosition.getFireSpawn(), Maze.spawnPosition.getWaterSpawn()), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                 // Map actuelle, donc Spawn
            new DiscreteCoordinates(4,0),                                                   // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(5,0)                                                    // Autre cellule de la porte (l'autre "case" rouge)
            );

            DialogDoor finalDoor = new DialogDoor(this, new DiscreteCoordinates(6, 11), dialogHandler);


        // Register des portes
        registerActor(toOrbWayDoor);
        registerActor(toMazeDoor);
        registerActor(finalDoor);
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }



}