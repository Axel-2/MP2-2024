package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * A class that represent the inital spawn area
 */
public final class OrbWay extends ICoopArea {

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(Element elementType) {
        DiscreteCoordinates coordinates = switch (elementType) {
            case WATER -> new DiscreteCoordinates(1, 12);
            case FIRE -> new DiscreteCoordinates(1, 5);
        };

        return coordinates;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));


        // Créations des deux portes de OrbWay qui permettront de revenir à Spawn
        Door orbWayDoor1 = new Door(
            "Spawn",                                                                    // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                              // Toujours open
            Arrays.asList(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
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
            Arrays.asList(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            this,                                                                                    // Map actuelle, donc Spawn
            new DiscreteCoordinates(0,8),                                                        // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(0,7),
            new DiscreteCoordinates(0,6),                                                        // Autres cellules occupées par les portes
            new DiscreteCoordinates(0,5),      
            new DiscreteCoordinates(0,4)                                                    
            );
        
        registerActor(orbWayDoor1);
        registerActor(orbWayDoor2);



    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }



    

}