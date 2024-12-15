package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.Collectable.Key;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Foes.Foe;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.Collections;
import java.util.List;

public class DialogDoor extends AreaEntity implements Interactor {

    private DialogHandler dialogHandler;
    private DialogDoorHandler dialogDoorHandler;

    private DialogDoorHandler interactionHandler = new DialogDoorHandler();

    private boolean isOpen;

    private boolean dialogHasBeenStarted;

    public DialogDoor(Area area, DiscreteCoordinates position, DialogHandler dialogHandler) {
        super(area, Orientation.DOWN, position);
        this.dialogHandler = dialogHandler;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    // il faut pouvoir aller sur la porte
    @Override
    public boolean takeCellSpace() {
        return false;
    }



    @Override
    public boolean isCellInteractable() {
        return true;
    }

    // Vrai car on a besoin des intéractions à distance aussi
    // voir ci-dessous
    @Override
    public boolean isViewInteractable() {
        return true;
    }


    // La porte a besoin de la celle sous elle pour voir
    // si un player arrive ou sort
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(Orientation.DOWN.toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    // La porte veut des intéractions à distance pour
    // permettre de savoir si un play arrive
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }



    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

        // Il faut faire de cette manière pour utiliser le handler
        other.acceptInteraction(interactionHandler, isCellInteraction);

    }

    /**
     * Call directly the interaction on this if accepted
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // Fonction par défaut pout le modèle visiteur
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    private final class DialogDoorHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {


            // Si la porte voit partir le player ou le voit arriver
            // elle met le dialogue en mode pas commencé
            if (!isCellInteraction) {
                if (player.isMoving()) {
                    dialogHasBeenStarted = false;
                }
            }

            // Si le dialogue n'a pas encore commencé et que le player
            // est sur la porte on peut lancer le dialogue
            if (!dialogHasBeenStarted && isCellInteraction) {
                dialogHasBeenStarted = true;
                Dialog dialog = new Dialog("key_required");

                dialogHandler.publish(new Dialog("key_required"));
            }
        }
    }
}
