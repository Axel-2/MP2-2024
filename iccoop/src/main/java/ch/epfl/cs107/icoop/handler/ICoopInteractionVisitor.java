package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.Collectable.ElementalItem;
import ch.epfl.cs107.icoop.actor.Collectable.Heart;
import ch.epfl.cs107.icoop.actor.Collectable.ICoopCollectable;
import ch.epfl.cs107.icoop.actor.Collectable.Orb;
import ch.epfl.cs107.icoop.actor.Foes.Foe;
import ch.epfl.cs107.icoop.area.ICoopBehavior.ICoopCell;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable
    /// Les corps de méthodes non nuls seront Overridés dans les interactors eux-mêmes, par exemple dans ICoopPlayer
    

    // Intéraction avec une cellule, par défaut ne fait rien
    default void interactWith(ICoopCell cell, boolean isCellInteraction) {
    }

    // Intéraction avec un joueur, par défaut ne fait rien
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    // Intéraction avec une porte, par défaut ne fait rien
    default void interactWith(Door other, boolean isCellInteraction) {
    }

    // Intéraction avec un Obstacle, par défaut ne fait rien
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {
    }

    // Intéraction avec un Rock, par défaut ne fait rien
    default void interactWith(Rock rock, boolean isCellInteraction) {
    }

    // Intéraction avec un Explosif par défaut ne fait rien
    default void interactWith(Explosif explosif, boolean isCellInteraction) {
    }

    default void interactWith(ICoopCollectable collectable, boolean isCellInteraction) {
    }

    default void interactWith(ElementalItem elemItem, boolean isCellInteraction) {
    }

    default void interactWith(Orb orb, boolean isCellInteraction) {
    }

    default void interactWith(Heart heart, boolean isCellInteraction) {
    }

   default void interactWith(Foe foe, boolean isCellInteraction) {
    }



}
