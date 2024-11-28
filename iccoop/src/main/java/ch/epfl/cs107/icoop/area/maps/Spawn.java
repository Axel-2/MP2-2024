package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * A class that represent the inital spawn area
 */
public final class Spawn extends ICoopArea {

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(Element elementType) {
        DiscreteCoordinates coordinates = switch (elementType) {
            case WATER -> new DiscreteCoordinates(13, 6);
            case FIRE -> new DiscreteCoordinates(14, 6);
        };

        return coordinates;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));


        Door spawnDoor = new Door(
            "OrbWay",                                                                // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                           // Toujours open
            Arrays.asList(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                 // Map actuelle, donc Spawn
            new DiscreteCoordinates(19,15),                                                   // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(19,16)                                                    // Autre cellule de la porte (l'autre "case" rouge)
            );

        registerActor(spawnDoor);

    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

}