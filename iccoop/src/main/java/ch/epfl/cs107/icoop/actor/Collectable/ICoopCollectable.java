package ch.epfl.cs107.icoop.actor.Collectable;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    private boolean isVisible;
    private final boolean isStockable;


    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position, boolean isStockable) {
        // L'object n'est pas collecté par défaut
        super(area, orientation, position);
        this.isStockable = isStockable;
        
    }

    public boolean isStockable(){
        return isStockable;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        // La seule Cell est la principale
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()) {
            drawCollectable(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {

        // Ceci est important car sinon les collectables restent
        // restent dans l'aire mais
        // ne sont simplement plus visibles
        if (isCollected() ) {
            getOwnerArea().unregisterActor(this);
        }
    }

    public abstract void  drawCollectable(Canvas canvas);

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
