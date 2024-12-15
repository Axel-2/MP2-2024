package ch.epfl.cs107.icoop.actor.Projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Foes.BombFoe;
import ch.epfl.cs107.icoop.actor.Foes.HellSkull;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

// TODO : "ELLES SERONT STOPPEES DANS LEUR COURSE EN LES TOUCHANT"
// MAIS JSP COMMENT FAIRE

public class StaffBall extends Unstoppable {

    private final StaffBallInteractionHandler interactionHandler = new StaffBallInteractionHandler();

    private final Animation animation;
    final static int ANIMATION_DURATION = 12;

    private final Element element;

    public StaffBall(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance, Element elem) {
        super(area, orientation, position, speed, maxDistance);

        this.element = elem;

        String name = elem == Element.FIRE ? "icoop/magicFireProjectile" : "icoop/magicWaterProjectile";
        this.animation = new Animation(name , 4, 1, 1, this , 32, 32,
        ANIMATION_DURATION/4, true);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        // y a pas d'intéraction à distance donc on
        // peut laisser une liste vide
        return List.of();
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
        super.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {

        // on update en permanence l'animation
        animation.update(deltaTime);

        // Cette condition teste si la flamme peut continuer sa course
        // en regardant si la prochaine cellule droit devant elle peut
        // etre traversée
        if (!getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
            // si elle ne peut plus avancer on la stop
            stopUnstoppable();
        }

        super.update(deltaTime);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

        // Il faut faire de cette manière pour utiliser le handler
        other.acceptInteraction(interactionHandler, isCellInteraction);

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // Il n'accepte pas d'intéraction donc on laisse ca vide
    }

    private final class StaffBallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Explosif explo, boolean isCellInteraction) {
            // testé et validé
            explo.activate(1);
            stopUnstoppable();
        }

        // TODO mettre des autres Damages ???

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            // testé et validé
            rock.destroy();
            stopUnstoppable();
        }
        @Override
        public void interactWith(BombFoe foe, boolean isCellInteraction) {
            foe.loseHealth(element.toDamage());
            stopUnstoppable();
        }

        @Override
        public void interactWith(HellSkull skull, boolean isCellInteraction) {
            if (element.equals(Element.WATER)){
                skull.loseHealth(element.toDamage());
                stopUnstoppable();
            }

        }

    }
}
