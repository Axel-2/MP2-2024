package ch.epfl.cs107.icoop.actor.Foes;

import ch.epfl.cs107.icoop.actor.Projectiles.Fire;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class HellSkull extends Foe {

    private float deltaFireTime;


    private static float MIN_FIRE_TIME = 0.5f;
    private static float MAX_FIRE_TIME = 2.f;

    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, new Damage[]{Damage.FIRE, Damage.WATER});
        this.deltaFireTime = RandomGenerator.getInstance().nextFloat(MIN_FIRE_TIME , MAX_FIRE_TIME);
    }

    @Override
    int getMaxLife() {
        return 1;
    }

    @Override
    public boolean wantsCellInteraction() {
        // SI IL N'est pas mort return true
        // TODO

        return true;
    }

    @Override
    public boolean takeCellSpace() {
        // On peut lui marcher dessus
        return true;
    }

    @Override
    void drawFoeSprite(Canvas canvas) {

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

    }

    @Override
    public void update(float deltaTime) {

        if (deltaFireTime <= 0) {
            // TODO tester si l'emplacement
            // est accessible mais je sais pas trop comment faire
            Fire fire = new Fire(
                    getOwnerArea(),
                    getOrientation(),
                    getCurrentMainCellCoordinates().jump(getOrientation().toVector()),
                    1, 100
            );
            getOwnerArea().registerActor(fire);

            // On remet un deltaFireTime au hasard pour
            // la prochaine flamme
            deltaFireTime = RandomGenerator.getInstance().nextFloat(MIN_FIRE_TIME , MAX_FIRE_TIME);
        } else {
            // si le timer n'est pas terminé
            // on decrémente
            deltaFireTime -= deltaTime;
        }



    }
}
