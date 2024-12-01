package ch.epfl.cs107.icoop.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.KeyBindings;
import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import static ch.epfl.cs107.play.math.Orientation.DOWN;
import static ch.epfl.cs107.play.math.Orientation.LEFT;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;
import static ch.epfl.cs107.play.math.Orientation.UP;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {

  // TO BE COMPLETED

    private final static int MOVE_DURATION = 8;
    private final Sprite sprite;
    private final Element element;
    private final Vector anchor = new Vector(0, 0);
    private final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};
    private final static int ANIMATION_DURATION = 4;

    private OrientedAnimation animation;
    private ICoopPlayerInteractionHandler interactionHandler = new ICoopPlayerInteractionHandler();
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

        // On met uniquement la cellule principale
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
        //area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
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
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        return Collections.singletonList
        (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    
    }

    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction() {
        // Veut systématiquement toutes les intéractions de contact
        return true;
    }

    /**@return (boolean): true if this require view interaction */
    @Override
    public boolean wantsViewInteraction() {

        // On veut les intéractions à distance seulement si le joueur appuie sur la touche useItem
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(playerKeyBindings.useItem()).isPressed();

    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

        // Il faut faire de cette manière pour utiliser le handler
        other.acceptInteraction(interactionHandler, isCellInteraction);

    }

    public Element getElement() {
        return element;
    }

    private boolean isLeaving = false;

    private Door leavingDoor = null;

    public Door getLeavingDoor() {
        return leavingDoor;
    }

    public void setLeaving(Boolean leaving) {
        isLeaving = leaving;
    }
    public boolean isLeaving() {
        return isLeaving;
    }

    private final class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Door other, boolean isCellInteraction) {

            if (other.getSignal().isOn()){
                isLeaving = true;
                leavingDoor = other;
            }
            
        }

        @Override
        public void interactWith(Explosif explo, boolean isCellInteraction){
            // Interaction à distance only, donc si le joueur presse le bouton pour useitem()
            Keyboard keyboard = getOwnerArea().getKeyboard();
            if(keyboard.get(playerKeyBindings.useItem()).isPressed()){
            explo.activate();
            }
                
        }
    }
}




interface ElementalEntity {
    Element element();
}