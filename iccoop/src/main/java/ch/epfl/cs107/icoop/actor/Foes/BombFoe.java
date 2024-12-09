package ch.epfl.cs107.icoop.actor.Foes;

import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

public class BombFoe extends Foe {
    
    private State state;

    // Animations
    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};

    private OrientedAnimation nonProtectedAnimation;
    private OrientedAnimation protectedAnimation;

    // TODO cette constante est la meme partout faudra ptet centraliser
    private int ANIMATION_DURATION = 8;

    private static final int EXTENDED_VIEW_DISTANCE = 8;

    private final BombFoeInteractionHandler interactionHandler = new BombFoeInteractionHandler();
    private int inactionCounter = 0;
    private static final int MAX_INACTION_STEPS = 24;


    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {

        // TODO AJOUTER DOMMAGE PHYSIQUES ???
        super(area, orientation, position, new Damage[]{Damage.FIRE});
        this.state = State.IDLE;

        this.nonProtectedAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION/3,
                this , anchor , orders , 4, 2, 2, 32, 32,
                true);

        this.protectedAnimation = new OrientedAnimation("icoop/bombFoe.protecting",
                ANIMATION_DURATION/3,this , anchor , orders , 4, 2, 2, 32, 32,
                false);
    }

    @Override
    void drawFoeSprite(Canvas canvas) {
        protectedAnimation.draw(canvas);
    }

    @Override
    int getMaxLife() {
        return 2;
    }

    private enum State {
        IDLE, ATTACK, HIDE
    }

    @Override
    public boolean wantsCellInteraction() {
        // ne veut pas d'intéraction de contact
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        // veut des intéractions à distance en IDLE ou en ATTACK
        return state.equals(State.IDLE) || state.equals(State.ATTACK);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // ????
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
        DiscreteCoordinates currentPosition = getCurrentMainCellCoordinates();
        Vector orientationVector = getOrientation().toVector();


        if (state.name().equals(State.ATTACK.name())) {
            // En mode attaque il ne voir que l'unique cellule en face de lui
            fieldOfViewCells.add(currentPosition.jump(orientationVector));
        } else {

            // TODO verifier que ca marche bien
            // TODO que en IDLE ???
            // Dans les autres modes, il voit un ensemble constant de cellules en face de lui
            for (int i = 1; i <= EXTENDED_VIEW_DISTANCE; i++) {
                fieldOfViewCells.add(currentPosition.jump(orientationVector.mul(i)));
            }
        }
        return fieldOfViewCells;

    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        
        switch (state) {
            case IDLE -> move(8);
            case ATTACK -> {

            }
            case HIDE -> {

            }
        }

        protectedAnimation.update(deltaTime);

        super.update(deltaTime);
    }


    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            state = State.ATTACK;
        }
    }
}
