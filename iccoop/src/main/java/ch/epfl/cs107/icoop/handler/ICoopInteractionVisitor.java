package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Collectable.*;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Foes.BombFoe;
import ch.epfl.cs107.icoop.actor.Foes.Foe;
import ch.epfl.cs107.icoop.actor.Foes.HellSkull;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.area.ICoopBehavior.ICoopCell;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */
public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    // Définition par défaut de toutes les intéractions qui seront implémentées

    // Interaction avec une cellule, par défaut ne fait rien
    default void interactWith(ICoopCell cell, boolean isCellInteraction) {
    }

    // Interaction avec un joueur, par défaut ne fait rien
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    // Interaction avec une porte, par défaut ne fait rien
    default void interactWith(Door other, boolean isCellInteraction) {
    }

    // Interaction avec un Obstacle, par défaut ne fait rien
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {
    }

    // Interaction avec un Rock, par défaut ne fait rien
    default void interactWith(Rock rock, boolean isCellInteraction) {
    }

    // Interaction avec un Explosif par défaut ne fait rien
    default void interactWith(Explosif explosif, boolean isCellInteraction) {
    }

    // Interaction avec un collectable par défaut ne fait rien
    default void interactWith(ICoopCollectable collectable, boolean isCellInteraction) {
    }

    // Interaction avec un item élémentaire, par défaut ne fait rien
    default void interactWith(ElementalItem elemItem, boolean isCellInteraction) {
    }

    // Interaction avec une Orbe par défaut ne fait rien
    default void interactWith(Orb orb, boolean isCellInteraction) {
    }

    // Interaction avec un Coeur par défaut ne fait rien
    default void interactWith(Heart heart, boolean isCellInteraction) {
    }

    // Interaction avec un ennemi par défaut ne fait rien
    default void interactWith(Foe foe, boolean isCellInteraction) {
    }

    // Interaction avec un Mur élémentaire par défaut ne fait rien
    default void interactWith(ElementalWall wall, boolean isCellInteraction) {  
    }

    // Interaction avec un bâton par défaut ne fait rien
    default void interactWith(Staff staff, boolean isCellInteraction){  
    }

    // Interaction avec un Artificier par défaut ne fait rien
    default void interactWith(BombFoe foe, boolean isCellInteraction){
    }

    // Interaction avec un crâne par défaut ne fait rien
    default void interactWith(HellSkull foe, boolean isCellInteraction){
    }

    default void interactWith(Key key, boolean isCellInteraction) {
    }



}
