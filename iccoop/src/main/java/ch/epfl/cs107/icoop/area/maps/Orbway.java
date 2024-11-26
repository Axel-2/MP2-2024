package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

/**
 * A class that represent the inital spawn area
 */
public final class Orbway extends ICoopArea {

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {

        // A FAIRE
        // Changer les coordonées en fonction du personnage

        return new DiscreteCoordinates(5, 15);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public String getTitle() {
        return "ICoop/OrbWay";
    }

}