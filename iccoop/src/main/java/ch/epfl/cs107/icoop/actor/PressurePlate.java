package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class PressurePlate extends AreaEntity implements Logic, Interactor {

    private final static String spriteName = "GroundPlateOff";
    private Sprite sprite;

    private boolean isPressed;
    private ICoopPlayer currentPlayer;
    private ElementalWall currentWall;

    private final PressurePlateInteractionHandler interactionHandler = new PressurePlateInteractionHandler();


    public PressurePlate(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        this.isPressed = false;

        // On est pas guidé sur les paramètres donc j'ai mis 1f comme d'hab mais jsp
        this.sprite = new Sprite(spriteName, 1f, 1f, this);

        // Par défaut il n y a pas de player sur la plaque de pression.
        this.currentPlayer = null;

        this.currentWall = null;
    }

    @Override
    public boolean isOn() {
        // La plaque est activée
        // si elle est pressée
        return isPressed;
    }

    @Override
    public boolean isOff() {
        // La plaque n'est pas activée si elle n'est pas
        // pressée
        return !isPressed;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        // Il y a seulement la cell principale
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        // Le joueur doit pouvoir marcher dessus
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        // La plaque s'active si le joueur est directement dessus
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        // Pas d'intéraction à distance à priori
        // comme il faut un contact direct
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction(){
        return true;
    }

    /**@return (boolean): true if this require view interaction */
    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {

        // Si le player en memoire n'est plus sur la position ou si il est null on remet le isPressed sur false
        if (currentPlayer == null) {
            isPressed = false;
        } else if (!currentPlayer.getCurrentCells().getFirst().equals(this.getCurrentCells())) {
            isPressed = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    // ----- Handler -----
    private final class PressurePlateInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // Seulement si le mur est actif
            PressurePlate.this.currentPlayer = player;
            isPressed = true;
        }
        }
}
