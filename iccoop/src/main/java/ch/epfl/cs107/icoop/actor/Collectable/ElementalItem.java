package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class ElementalItem extends ICoopCollectable implements Logic, ElementalEntity {

    Element elementalType;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element elementalType) {
        super(area, orientation, position);
        this.elementalType = elementalType;
    }

    @Override
    public boolean isOn() {
        // Retourne On si c'est collecté
        return isCollected();
    }

    @Override
    public boolean isOff() {
        // isOff retourne true si
        // l'élément n'est pas collecté
        return !isCollected();
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {


        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);


    }

}

