package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.text.JTextComponent;

import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {

  // TO BE COMPLETED

    private final static int MOVE_DURATION = 8;
    private final Sprite sprite;
    private Element element;

    private final Vector anchor = new Vector(0, 0);
    private final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};
    private final static int ANIMATION_DURATION = 4;
    private OrientedAnimation animation;

    private KeyBindings.PlayerKeyBindings playerKeyBindings;

    /**
     * @param owner (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param spriteName (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element) {
        super(owner, orientation, coordinates);
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.element = element;
        this.animation = new OrientedAnimation(element.getSpriteName(), ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32, true);
        switch (element) {
            case FIRE -> playerKeyBindings = RED_PLAYER_KEY_BINDINGS;
            case WATER -> playerKeyBindings = BLUE_PLAYER_KEY_BINDINGS;
        }
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(playerKeyBindings.left()));
        moveIfPressed(Orientation.UP, keyboard.get(playerKeyBindings.up()));
        moveIfPressed(Orientation.RIGHT, keyboard.get(playerKeyBindings.right()));
        moveIfPressed(Orientation.DOWN, keyboard.get(playerKeyBindings.down()));
        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            animation.reset();
        }
        super.update(deltaTime);
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * makes the player entering a given area
     * @param area     (Area):  the area to be entered, not null
     * @param position (DiscreteCoordinates): initial position in the entered area, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }


    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * Retourne l'élément du ICoopPlayer (Feu ou eau)
     */
    @Override
    public Element element() {
        return this.element;
    }

     /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
    */
     @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Get this Interactor's current field of view cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList
        (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    
    }

    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction(){ return true;}

    /**@return (boolean): true if this require view interaction */
    // @Override
    // public boolean wantsViewInteraction(){ 
    //     // TO DO 3 : Return true seulement si la touche pour "use item" est pressée. c'est le paragraphe du milieu de la page 12, section 2.4.2
    // }

}

interface ElementalEntity {
    Element element();
}