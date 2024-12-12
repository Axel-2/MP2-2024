package ch.epfl.cs107.icoop.actor.Foes;

import ch.epfl.cs107.icoop.actor.Health;
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

 //Un ennemi est capable de se déplacer sur une grille
// il hérite donc de MovableAreaEntity
public abstract class Foe extends MovableAreaEntity implements Interactor {

    private boolean isAlive;

    // Barre de vie
    private final Health health = new Health(this , Transform.I.translated(0, 1.75f), getMaxLife() , true);

    // duplication de Player faudra trouver une solution plus tard
     private final static float IMMUNITY_TIME = 24;
     private boolean isImmunityTime;
     private float immunityTimer;

    // Vulnérabilité
    private Damage[] vulnerabilityList;

    // Animations
    private static final int ANIMATION_DURATION = 24;
    private final Animation deathAnimation;

    // compteur utile pour unregister l'actor à la fin de la deathAnimation
    private double animationCounter = ANIMATION_DURATION;


    public Foe(Area area, Orientation orientation, DiscreteCoordinates position, Damage[] vulnerabilityList) {
        super(area, orientation, position);
        this.vulnerabilityList = vulnerabilityList;
        this.deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this , 32, 32, new
                Vector(-0.5f, 0f), ANIMATION_DURATION/7, false);
        this.isAlive = true;
    }

    abstract int getMaxLife();

    protected boolean isImmunityTime() {
        return isImmunityTime;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        // On ne retoutne que la cell principale
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        // On peut marcher dessus que si le mob est mort
        return isAlive;
    }

    @Override
    public boolean isCellInteractable() {
        // Il est possible d'avoir par
        // défaut des intéractions de contact


        return true;
    }

    @Override
    public boolean isViewInteractable() {
        // il est possible par défaut d'avoir
        // des intéractions à distance
        return true;
    }

    @Override
    public boolean wantsCellInteraction() {
        // Par défaut une Foe demande des intéractions
        // ils ne veulent pas que les subirs
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        // Par défaut une Foe demande des intéractions
        // ils ne veulent pas que les subirs
        return true;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Si les pv sont = 0 ou < 0
        if (health.isOff() || health.getIntensity() < 0) {
            isAlive = false;
            animationCounter -= deltaTime;

            // Peut etre qu'on verra pas la première image faudra tester
            deathAnimation.update(deltaTime);
        }

        if (animationCounter < 0) {
            // A la fin de l'animation
            // On unregister l'actor
            getOwnerArea().unregisterActor(this);
        }


        super.update(deltaTime);
    }

    abstract void drawFoeSprite(Canvas canvas);

    @Override
    public void draw(Canvas canvas) {

        // TODO legere duplication de code de player

        // Lorsqu'il y a état d'immunité
        // l'ennemi "clignote"
        // sinon l'affichage est normale
        if (isImmunityTime) {
            if (immunityTimer % 2 == 0) {
                drawFoeSprite(canvas);
            }
        }

        // TODO PAS SUR ENFT POUR LA BARRE DE VIE
        // Il faut aussi dessiner la barre de vie
        // health.draw(canvas);

        if (!isAlive) {
            // Lorqu'il est mort on draw l'animation
            // jusqu'à qu'il soit unregister
            deathAnimation.draw(canvas);
        } else {
            drawFoeSprite(canvas);
        }

        super.draw(canvas);
    }


     // Duplication de Player faudra trouver une solution ???
     public void loseHealth(Damage damage) {

        // TODO duplication de code player

         for (Damage damageEl : vulnerabilityList) {
             if (damageEl.name().equals(damage.name()) || (!isImmunityTime)) {
                 health.decrease(damage.getDamagePoints());
                 isImmunityTime = true;
                 immunityTimer = IMMUNITY_TIME;
             }
         }
     }
 }


