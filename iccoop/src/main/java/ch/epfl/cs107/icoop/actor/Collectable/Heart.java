package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Heart extends ICoopCollectable {

    final static int  ANIMATION_DURATION = 24;

    final private Animation animation;

    public Heart(Area area, DiscreteCoordinates position) {
        // On force Orientation.DOWN
        super(area, Orientation.DOWN, position, false);
        this.animation =  new Animation("icoop/heart", 4, 1, 1, this , 16, 16,
                ANIMATION_DURATION/4, true);
    }

    @Override
    public void drawCollectable(Canvas canvas) {
        animation.draw(canvas);
    }

    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }
}
