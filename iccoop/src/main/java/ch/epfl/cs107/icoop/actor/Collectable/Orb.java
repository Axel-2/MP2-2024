package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends ElementalItem {

    final static int  ANIMATION_DURATION = 24;
    final static int ANIMATION_FRAMES = 6;

    final private OrbType orbType;
    final private Animation animation;
    final Sprite[] sprites;

    public Orb(Area area, Orientation orientation, DiscreteCoordinates position, OrbType orbType) {
        super(area, orientation, position, orbType.elementType);
        this.sprites = new Sprite[ANIMATION_FRAMES];
        this.orbType = orbType;

        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this ,
                    new RegionOfInterest(i * 32, orbType.spriteYDelta , 32, 32));
        }

        this.animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES , sprites);

    }

    @Override
    public void drawCollectable(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {

        // Il ne faut pas oublier d'update l'animation
        animation.update(deltaTime);
        super.update(deltaTime);
    }

    public enum OrbType {
        WATER(0, "orb_water_msg", Element.WATER),
        FIRE(64, "orb_fire_msg", Element.FIRE),;

        private final int spriteYDelta;
        private final String dialogName;
        private final Element elementType;

        OrbType(int spriteYDelta, String dialogName, Element elementType) {
            this.spriteYDelta = spriteYDelta;
            this.dialogName = dialogName;
            this.elementType = elementType;
        }
    }
}

