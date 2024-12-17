package ch.epfl.cs107.icoop.actor.Foes;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.actor.Health;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/*
 * Représente de manière abstraite les ennemis, se déplaçant sur une grille et pouvant intéragir et recevoir des intéractions
*/
public abstract class Foe extends MovableAreaEntity implements Interactor {

    // Indique si l'ennemi est en vie
    private boolean isAlive;

    // Barre de vie
    private final Health health = new Health(this , Transform.I.translated(0, 1.75f), getMaxLife() , false);

    // Immunités
     private final static float IMMUNITY_TIME = 24;
     private boolean isImmunityTime;
     private float immunityTimer;

    // Vulnérabilité
    private final Damage[] vulnerabilityList;

    // Animations
    private static final int ANIMATION_DURATION = 24;
    private final Animation deathAnimation;
    private double animationCounter = ANIMATION_DURATION;

    /**
     * Constructeur d'ennemi
     * @param area
     * @param orientation
     * @param position
     * @param vulnerabilityList
     */
    public Foe(Area area, Orientation orientation, DiscreteCoordinates position, Damage[] vulnerabilityList) {
        super(area, orientation, position);
        this.vulnerabilityList = vulnerabilityList;
        this.deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this , 32, 32, new
                Vector(-0.5f, 0f), ANIMATION_DURATION/7, false);
        this.isAlive = true;
    }


    /**
     * Getter de la vie maximum, utiles aux sous-classes
     * @return
     */
    abstract int getMaxLife();

    /**
     * Getter de la variable indiquant si l'ennemi est en périiode d'immunité
     * @return
     */
    protected boolean isImmunityTime() {
        return isImmunityTime;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        // Prend seulement de la place vivant
        return isAlive;
    }

    @Override
    public boolean isCellInteractable() {
        // Accepte les intéractions de contact par défaut
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        // Accepte les intéractions à distance par défaut
        return true;
    }

    @Override
    public boolean wantsCellInteraction() {
        // Demande des intéractions de contact par défaut
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        // Demande des intéractions à distance par défaut
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    @Override
    public void update(float deltaTime) {

        if (isImmunityTime) {
            immunityTimer -= 1;
            
            if (immunityTimer < 0) {
                isImmunityTime = false;
            }
        }

        // Si les pv sont = 0 ou < 0
        if (health.isOff() || health.getIntensity() < 0) {
            isAlive = false;
            animationCounter -= 1.5;
            deathAnimation.update(deltaTime);
        }

        if (animationCounter < 0) {
            // A la fin de l'animation
            // On unregister l'actor
            getOwnerArea().unregisterActor(this);
        }

        super.update(deltaTime);
    }

    // Méthode utile pour HellSkull
    protected boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Dessinera le sprite des ennemis
     * @param canvas
     */
    abstract void drawFoeSprite(Canvas canvas);

    @Override
    public void draw(Canvas canvas) {

        // Fait clignoter l'ennemi si il est en état d'immunité
        if (isImmunityTime && isAlive) {
            if (immunityTimer % 2 == 0) {
                drawFoeSprite(canvas);
            }
        }
        
        // Dessine la barre de vie
        health.draw(canvas);

        // Tant qu'il n'est pas mort, l'animation se dessine
        if (!isAlive) {
            deathAnimation.draw(canvas);
        } else if (!isImmunityTime) {
            drawFoeSprite(canvas);
        }

        super.draw(canvas);
    }

    /**
     * Fonction gérant la perte de vie suite à un dégat
     * @param damage
     */
     public void loseHealth(Damage damage) {

        // TODO duplication de code player ------------------------------------------------------------------------------- Axel ------------------
         for (Damage damageEl : vulnerabilityList) {
             if (damageEl.name().equals(damage.name()) || (!isImmunityTime)) {
                 health.decrease(damage.getDamagePoints());
                 isImmunityTime = true;
                 immunityTimer = IMMUNITY_TIME;
             }
         }
     }
 }


