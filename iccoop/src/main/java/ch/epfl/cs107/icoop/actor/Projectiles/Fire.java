package ch.epfl.cs107.icoop.actor.Projectiles;

import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Foes.Foe;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

// TODO SUPPRIMER LE PROJECTILE LORSQU'IL ARRIVE DANS UN MUR
// MAIS JSP COMMENT FAIRE

public class Fire extends Unstoppable {

    private final FireInteractionHandler interactionHandler = new FireInteractionHandler();

    private Animation animation = new Animation("icoop/fire", 7, 1, 1, this , 16, 16, 4, true);

    public Fire(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance) {
        super(area, orientation, position, speed, maxDistance);
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

    private final class FireInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Explosif explo, boolean isCellInteraction) {
            // testé et validé
            explo.activate();
            stopUnstoppable();
        }

        // TODO mettre des autres Damages ???

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            // Bizzare de choisir Fire mais bon
            foe.loseHealth(Damage.FIRE);
            stopUnstoppable();
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // testé et validé
            player.loseHealth(Damage.FIRE);
            stopUnstoppable();
        }
    }
}
