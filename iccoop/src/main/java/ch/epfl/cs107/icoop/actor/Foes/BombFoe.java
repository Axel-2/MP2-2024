package ch.epfl.cs107.icoop.actor.Foes;

import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

public class BombFoe extends Foe {

    // Différents états de l'artificier
    private State state;

    private final OrientedAnimation nonProtectedAnimation;
    private final OrientedAnimation protectedAnimation;

    // TODO cette constante est la meme partout faudra ptet centraliser
    private final int ANIMATION_DURATION = 16;

    private static final int EXTENDED_VIEW_DISTANCE = 8;

    private final BombFoeInteractionHandler interactionHandler = new BombFoeInteractionHandler();


    // Moments d'inactions ou il ne fait rien
    private int inactionCounter = 0;
    private static final int MAX_INACTION_STEPS = 24;

    // variables pour le mode attack
    private ICoopPlayer targetedPlayer;


    public BombFoe(Area area, DiscreteCoordinates position) {

        // TODO AJOUTER DOMMAGE PHYSIQUES ???
        // Orienté par défaut vers le bas
        super(area, DOWN, position, new Damage[]{Damage.FIRE, Damage.PHYSICAL});

        // par défaut il est en IDLE
        this.state = State.IDLE;

        // Animations
        Vector anchor = new Vector(-0.5f, 0);
        Orientation[] orders = {DOWN, RIGHT, UP, LEFT};

        this.nonProtectedAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION/3,
                this , anchor, orders, 4, 2, 2, 32, 32,
                true);

        this.protectedAnimation = new OrientedAnimation("icoop/bombFoe.protecting",
                ANIMATION_DURATION/3,this , anchor, orders, 4, 2, 2, 32, 32,
                false);
    }

    @Override
    void drawFoeSprite(Canvas canvas) {

        switch (state) {
            case ATTACK, IDLE -> nonProtectedAnimation.draw(canvas);
            case HIDE -> protectedAnimation.draw(canvas);
        }
    }

    @Override
    int getMaxLife() {
        return 2;
    }

    private enum State {
        IDLE(1),
        ATTACK(2),
        HIDE(0),
        ;

        private final int speedFactor;

        State(int speedFactor) {
            this.speedFactor = speedFactor;
        }

        public int getSpeedFactor() {
            return speedFactor;
        }


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
            // En mode attaque, il ne voit que l'unique cellule en face de lui
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

        // On update les deux animations en permanence
        nonProtectedAnimation.update(deltaTime);
        protectedAnimation.update(deltaTime);

        // Ne fait absolument rien si en mode inactif
         if (inactionCounter < MAX_INACTION_STEPS) {
             inactionCounter += 1;

            // on sort donc directement de update
            return;
        } else {
             switch (state) {
                 // En état IDLE, il ne fait rien d'autre que de se déplacer// de façon aléatoire
                 case IDLE -> {
                     randomMove();
                 }
                 case ATTACK ->
                         targetedMove();
                 case HIDE -> {

                 }
         }
        }



        super.update(deltaTime);
    }


    private void randomMove() {

        if (!isDisplacementOccurs()) {
            double changeOrientationProbability = 0.4;

            // il n y a que 40% de chance de changer l'orientation
            if (RandomGenerator.getInstance().nextDouble() < changeOrientationProbability) {
                int randomIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
                orientate(Orientation.values()[randomIndex]);
            }


            move(ANIMATION_DURATION / state.speedFactor);
            inactionCounter = 0;

        }

    }

    private void targetedMove() {

        int localSpeedFactor = 1;

        // on s'assure qu'il y a bien un player
        // a cibler
        if (targetedPlayer == null) {
            return;
        }

        // vecteur séparant l’artificier de sa cible
        Vector v = targetedPlayer.getPosition().sub(this.getPosition());
        // composantes
        float deltaX = v.x;
        float deltaY = v.y;

        // condition pour la nouvelle orientation vers le player
        Orientation orientation;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            orientation = Orientation.fromVector(new Vector(deltaX, 0));
        } else {
            orientation = Orientation.fromVector(new Vector(0, deltaY));
        }


        // TODO pourquoi le changement d'orientation ne pourrait pas se faire ??
        // si le changement d’orientation n’a pas pu se faire, un pas de déplacement à vitesse
        //rapide aura lieu.
        if (orientation != null) {
            orientate(orientation);
            localSpeedFactor = 1;
        } else {
            // sinon on double la vitesse
            localSpeedFactor = 2;
        }

        // distance entre le player et l'artificier
        float distancePlayerFoe = DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetedPlayer.getCurrentMainCellCoordinates());

        // si la distance entre l'artificier est plus grande que 3 on le fait avancer
        if (distancePlayerFoe > 3) {
            move(localSpeedFactor * ANIMATION_DURATION / state.getSpeedFactor());
        } else {

            // TODO COMMENT SAVOIR SI LA CELL EST LIBRE ??
            // si l'artificier est assez proche il largue une bombre
            DiscreteCoordinates frontCell = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

            // on vérifie si la cell est libre pour poser la bobre
            if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(frontCell))) {
                Explosif explosif = new Explosif(getOwnerArea(), DOWN, frontCell, 2);
                getOwnerArea().registerActor(explosif);

                // la bombe est tout de suite activée
                explosif.activate();

                // l'artificier passe en mode protégé
                state = State.HIDE;
            }

        }



    }


    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {

        // Il n interagit que avec les personnages principaux
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // Si il voit un player dans son champ de vision il se met en mode attaque
            state = State.ATTACK;
            targetedPlayer = player;
        }
    }
}
