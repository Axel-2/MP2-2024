package ch.epfl.cs107.icoop.actor.Foes;

import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class RockFoe extends MovableAreaEntity {
    public RockFoe(Area area, Orientation orientation, DiscreteCoordinates position, Damage[] vulnerabilityList) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (!isDisplacementOccurs()) {
            orientate(Orientation.DOWN);
            move(8);
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        new Sprite("rock.2", 1f, 1f, this).draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }
}
