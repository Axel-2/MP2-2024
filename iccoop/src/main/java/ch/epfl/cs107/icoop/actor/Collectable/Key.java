package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Key extends ElementalItem implements Logic {

    private Sprite sprite;

    public Key(Area area, Orientation orientation, DiscreteCoordinates position, Element elementalType, boolean isStockable) {
        super(area, orientation, position, elementalType, isStockable);

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

    // Pas besoin de redéfinir getCurrentCells car IcoopCollectable occupe
    // déjà la celle comme voulu





}
