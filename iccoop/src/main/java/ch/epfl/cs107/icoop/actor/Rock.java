package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends Obstacle {

    private final static String spriteName = "rock.1";

    private boolean isDestroyed;

    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, spriteName);
    }

    @Override
    public boolean takeCellSpace() {
        return !isDestroyed;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            sprite.draw(canvas);
        }
    }
}
