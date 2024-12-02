package ch.epfl.cs107.icoop.Collectable;

import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    private boolean isVisible;

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        // L'object n'est pas collecté par défaut
        super(area, orientation, position, false);
    }

    @Override
    public void draw(Canvas canvas) {
        // On dessine l'item que si il n'est pas collected
        if (!isCollected()) {
            super.draw(canvas);
        }

    }

    // Par défaut un Collectable est traversable.
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    // Que des intéractions de contact à ce niveau
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    // Que des intéractions de contact à ce niveau
    @Override
    public boolean isViewInteractable() {
        return false;
    }
}
