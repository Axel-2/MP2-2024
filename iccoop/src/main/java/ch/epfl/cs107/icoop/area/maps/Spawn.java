package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.Collectable.Staff;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Projectiles.Fire;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.SpawnPosition;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * A class that represent the inital spawn area
 */
public final class Spawn extends ICoopArea {

    // On a besoin d'une variable static car des fois,
    // on veut le spawn sans initialiser l'objet donc
    // le getter ci-dessous ne suffit pas
    static final SpawnPosition spawnPosition = new SpawnPosition(new DiscreteCoordinates(14, 6), new DiscreteCoordinates(13, 6));

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
        registerActor(new Background(this));
        registerActor(new Foreground(this));

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
        

        registerActor(toOrbWayDoor);
        registerActor(toMazeDoor);

        // DEBUG projectiles
        Fire fire = new Fire(this, Orientation.RIGHT, new DiscreteCoordinates(0, 10), 1, 200);
        registerActor(fire);
        Fire fire1 = new Fire(this, Orientation.UP, new DiscreteCoordinates(7, 5), 3, 200);
        registerActor(fire1);

        registerActor(new Staff(this, new DiscreteCoordinates(15, 4), Element.FIRE));
        registerActor(new Staff(this, new DiscreteCoordinates(12, 4), Element.WATER));


        // Création du rock et de l'explo
        Rock rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(11, 9 ) );
        Rock rock2 = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(3, 7 ) );
        Explosif explo = new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(11, 10), 3);

        registerActor(rock);
        registerActor(rock2);
        registerActor(explo);

    }

    @Override
    public String getTitle() {
        return "Spawn";
    }



}