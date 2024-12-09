package ch.epfl.cs107.icoop.actor.Projectiles;

import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;


public abstract class Unstoppable extends MovableAreaEntity implements Interactor {

    private final int speed;
    private int distanceLeft;
    private boolean isTravelling;


    // TODO je suis pas sur du 8
    private final int MOVE_DURATION = 8;

    public Unstoppable(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance) {
        super(area, orientation, position);
        this.speed = speed;
        this.distanceLeft = maxDistance;
        this.isTravelling = true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        // On peut marcher dessus
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        // par défaut n'accepte rien
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        // par défaut n'accepte rien
        return false;
    }


    @Override
    public boolean wantsCellInteraction() {
        // interaction seulement si il est en vol
        return isTravelling;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }


    public void stopUnstoppable() {
        isTravelling = false;
    }


    @Override
    public void update(float deltaTime) {

        if (distanceLeft <= 0 || !isTravelling) {
            // Disparait lorsqu'il a fini sa course
            getOwnerArea().unregisterActor(this);
        } else  {
            move(MOVE_DURATION / speed);

            // a chaque cycle on diminue d'une unité la distance parcourue
            distanceLeft -= 1;
        }

        super.update(deltaTime);
    }
}
