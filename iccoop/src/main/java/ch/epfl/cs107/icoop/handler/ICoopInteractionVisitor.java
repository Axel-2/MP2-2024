package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopBehavior.ICoopCell;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable
    /// Les corps de méthodes non nuls seront Overridés dans les interactors eux-mêmes, par exemple dans ICoopPlayer
    

    // Intéraction avec une cellule, par défaut ne fait rien
    default void interactWith(ICoopCell other, boolean isCellInteraction) {
    }

    // Intéraction avec un joueur, par défaut ne fait rien
    default void interactWith(ICoopPlayer other, boolean isCellInteraction) {
    }

    // Intéraction avec une porte, par défaut ne fait rien
    default void interactWith(Door other, boolean isCellInteraction) {
    }


}
