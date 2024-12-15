package ch.epfl.cs107.icoop.area.maps;

import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.Collectable.Key;
import ch.epfl.cs107.icoop.actor.Teleporter;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.SpawnPosition;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;


/**
 * Représente la map finale : l'arène !
 */
public class Arena extends ICoopArea {

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        // TODO  mieux gérer les paramètres ici ----------------------------------------------------------------------------------------------------------------------

        // Clés
        Key fireKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 16), Element.FIRE, true);
        Key waterKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 4), Element.WATER, true);

        registerActor(fireKey);
        registerActor(waterKey);

        // Portail
        Teleporter teleporter = new Teleporter(
                "Spawn", Logic.TRUE, Arrays.asList(Spawn.SPAWN_POSITION.getFireSpawn(), Spawn.SPAWN_POSITION.getWaterSpawn()), this,
                new DiscreteCoordinates(19, 6),
                new DiscreteCoordinates(19, 7)
        );
        registerActor(teleporter);

    }

    public final static SpawnPosition SPAWN_POSITION = new SpawnPosition(
            new DiscreteCoordinates(4, 5),
            new DiscreteCoordinates(14, 15)
    );

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(Element element) {
        if (element == Element.FIRE) {
            return SPAWN_POSITION.getFireSpawn();
        }
        return SPAWN_POSITION.getWaterSpawn();
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
