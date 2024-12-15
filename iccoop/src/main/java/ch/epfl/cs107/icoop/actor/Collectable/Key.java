package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
Représente les clés élémentaires
*/
public class Key extends ElementalItem implements Logic, InventoryItem {

    // Image de la clé
    private Sprite sprite;

    /**
     * Constructeur des clés
     * @param area
     * @param orientation
     * @param position
     * @param elementalType
     * @param isStockable
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, Element elementalType, boolean isStockable) {
        super(area, orientation, position, elementalType, isStockable);

        // Le sprite s'adapte à l'élément
        if (elementalType == Element.FIRE) {
            this.sprite = new Sprite("icoop/key_red", 0.6f, 0.6f, this);
        } else {
            this.sprite = new Sprite("icoop/key_blue", 0.6f, 0.6f, this);
        }

    }

    @Override
    public Element getElement() {
        return null;
    }

    @Override
    public void drawCollectable(Canvas canvas) {
        sprite.draw(canvas);
    }
    
    // TODO --------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getPocketId() {
        return 0;
    }

    @Override
    public String getName() {
        return "key";
    }
}
