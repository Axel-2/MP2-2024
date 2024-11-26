package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

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
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

}