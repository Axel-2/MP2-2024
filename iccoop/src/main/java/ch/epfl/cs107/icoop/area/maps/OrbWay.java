package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

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
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }

}