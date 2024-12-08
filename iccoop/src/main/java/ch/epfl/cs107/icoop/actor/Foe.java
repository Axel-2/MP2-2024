package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

// Un ennemi est capable de se déplacer sur une grille
//// il hérite donc de MovableAreaEntity
//public abstract class Foe extends MovableAreaEntity implements Interactor {
//
//    private boolean isAlive;
//
//    // Barre de vie
//    private static final int MAX_LIFE = 100;
//    private final Health health = new Health(this , Transform.I.translated(0, 1.75f), MAX_LIFE , true);
//
//    // Vulnérabilité
//    private Damage[] vulnerabilityList;
//
//    // Animations
//    private static final int ANIMATION_DURATION = 24;
//    private static final Animation deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this , 32, 32, new
//            Vector(-0.5f, 0f), ANIMATION_DURATION/7, false);
//
//    // compteur utile pour unregister l'actor à la fin de la deathAnimation
//    private double counter = ANIMATION_DURATION;
//
//
//    public Foe(Area area, Orientation orientation, DiscreteCoordinates position) {
//        super(area, orientation, position);
//    }
//
//    @Override
//    public List<DiscreteCoordinates> getCurrentCells() {
//        // On ne retoutne que la cell principale
//        return Collections.singletonList(getCurrentMainCellCoordinates());
//    }
//
//    @Override
//    public boolean takeCellSpace() {
//        // On peut marcher dessus que si le mob est mort
//        return isAlive;
//    }
//
//    @Override
//    public boolean isCellInteractable() {
//        // Il est possible d'avoir par
//        // défaut des intéractions de contact
//        return true;
//    }
//
//    @Override
//    public boolean isViewInteractable() {
//        // il est possible par défaut d'avoir
//        // des intéractions à distance
//        return true;
//    }
//
//    @Override
//    public boolean wantsCellInteraction() {
//        // Par défaut une Foe demande des intéractions
//        // ils ne veulent pas que les subirs
//        return true;
//    }
//
//    @Override
//    public boolean wantsViewInteraction() {
//        // Par défaut une Foe demande des intéractions
//        // ils ne veulent pas que les subirs
//        return true;
//    }
//
//
//    @Override
//    public void update(float deltaTime) {
//
//        // Si les pv sont = 0 ou < 0
//        if (health.isOff() || health.getIntensity() < 0) {
//            isAlive = false;
//            counter -= deltaTime;
//
//            // Peut etre qu'on verra pas la première image faudra tester
//            deathAnimation.update(deltaTime);
//        }
//
//        if (counter < 0) {
//            // A la fin de l'animation
//            // On unregister l'actor
//            getOwnerArea().unregisterActor(this);
//        }
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//
//
//        // Lorsqu'il y a état d'immunité
//        // le personnage "clignote"
//        // sinon l'affichage est normale
//        if (isImmunityTime) {
//            if (immunityTimer % 2 == 0) {
//                animation.draw(canvas);
//            }
//        }
//
//        // Il faut aussi dessiner la barre de vie
//        health.draw(canvas);
//
//        if (!isAlive) {
//            // Lorqu'il est mort on draw l'animation
//            // jusqu'à qu'il soit unregister
//            deathAnimation.draw(canvas);
//        }
//
//        super.draw(canvas);
//    }
//}
//
//
