package ch.epfl.cs107.icoop.actor.Collectable;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
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

    private boolean dialogHasBeenStarted;

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

    public Damage getDamage() {
        return orbType.damage;
    }

    @Override
    public void collectBy(ElementalEntity entity) {
        // On a besoin du dialogHandler ici
        DialogHandler dialogHandler = ((ICoopArea) getOwnerArea()).dialogHandler;

        // ATTENTION
        // Les variables dialogHasBeenStarted et isDialogActiv ne se comportent pas de la
        // même façon. dialogHasBeenStarted est un attribut de cette classe elle-même qui nous
        // sert à ne pas relancer le dialogue en boucle. isDialogActiv est une variable de ICoop qui est
        // modifiée dans le fichier principal et qui nous permet d'attendre que le dialogue soit totalement
        // terminé avant de ramasser l'objet. On ne peut donc pas intervertir et simplifier le code ci-dessous.

        // La condition est importante
        // sinon c'est impossible de quitter le dialogue
        if (!dialogHasBeenStarted) {
            // Ajout du dialogue
           dialogHandler.publish(new Dialog(orbType.dialogName));
           // On change cette valeur pour ne pas
            // push le dialogue une seconde fois
           dialogHasBeenStarted = true;
        }

        // On collecte l'objet que lorsque le dialogue est
        // inactif donc lorsqu'il est terminé
        // sinon l'objet disparait au début du dialogue
        if (!dialogHandler.isDialogActiv()){
            super.collectBy(entity);
        }
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
        WATER(0, "orb_water_msg", Element.WATER, Damage.WATER),
        FIRE(64, "orb_fire_msg", Element.FIRE, Damage.FIRE),;

        private final int spriteYDelta;
        private final String dialogName;
        private final Element elementType;
        private final Damage damage;

        OrbType(int spriteYDelta, String dialogName, Element elementType, Damage damage) {
            this.spriteYDelta = spriteYDelta;
            this.dialogName = dialogName;
            this.elementType = elementType;
            this.damage = damage;
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public Element element() {
        return elementalType;
    }
}

