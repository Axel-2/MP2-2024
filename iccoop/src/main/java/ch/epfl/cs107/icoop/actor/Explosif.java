package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Explosif extends AreaEntity implements Interactor{

    private final static int ANIMATION_DURATION = 24;

    private Animation normalAnimation;
    private Animation explosionAnimation;

    private boolean isActivated;
    private int counter;
    private boolean isExploding;

    private ExplosifInteractionHandler interactionHandler = new ExplosifInteractionHandler();


    // Constructeur ici
    public Explosif(Area area, Orientation orientation, DiscreteCoordinates position, int counter){
        super(area, orientation, position);

        // Désactivé par défaut
        this.isActivated = false;
        this.isExploding = false;
        this.counter = counter;

        // Animation lorsque l'explosif n'a pas encore explosé
        this.normalAnimation = new Animation("icoop/explosive", 2, 1, 1, this , 16, 16,
        ANIMATION_DURATION/2, true);

        // Animation d'explosion
        this.explosionAnimation = new Animation("icoop/explosion", 7, 1, 1, this , 32, 32,
        ANIMATION_DURATION/7, false);

    }

    /*
     * Active l'explosif
     */
    public void activate() {
        isActivated = true;
    }

    
    @Override
    public void update(float deltaTime){

        // Update de l'animation de base
        normalAnimation.update(deltaTime);


        if (isActivated) {
            counter -= 1;
        }

        // Si le counter est négatife on change l'attribut
        // et on commence l'animation d'explosion
        if (counter <= 0) {
            isExploding = true;
            explosionAnimation.update(deltaTime);

        }

        // Lorsque l'animation a eu le temps de s'afficher
        // on peut finalement unregister l'actor
        if (counter <= -20) {
            getOwnerArea().unregisterActor(this);
        }

        super.update(deltaTime);

    }
        

    @Override
    public void draw(Canvas canva){

        if (!isExploding){
            normalAnimation.draw(canva);        
        }

        else{
            explosionAnimation.draw(canva);
        }
    
    }
    /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    /**
     * Get this Interactor's current field of view cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        List<DiscreteCoordinates> neighbourCells = new ArrayList<>();
        for (Orientation orientation : Orientation.values()) {
            neighbourCells.add(getCurrentMainCellCoordinates().jump(orientation.toVector()));
        }
        return neighbourCells;
        }


    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction() {
        // La bombe n'interagit avec les obstacles
        // que si elle explose
        return isExploding;
    }

    @Override
    /**@return (boolean): true if this require view interaction */
    public boolean wantsViewInteraction(){
        return isExploding;
    }

    /**
     * Do this Interactor interact with the given Interactable
     * The interaction is implemented on the interactor side !
     * @param other (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction){
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

        /**
     * Indicate if the current Interactable take the whole cell space or not
     * i.e. only one Interactable which takeCellSpace can be in a cell
     * (how many Interactable which don't takeCellSpace can also be in the same cell)
     * @return (boolean)
     */
    @Override
    public boolean takeCellSpace(){
        // Un personnage peut traverser la bombe
        return false;
    }


    /**@return (boolean): true if this is able to have cell interactions*/
    @Override
    public boolean isCellInteractable() {
        return (!isExploding && isActivated);
    }

    /**@return (boolean): true if this is able to have view interactions*/
    @Override
    public boolean isViewInteractable() {
        // Lorsque la bombre n'a pas encore explosée
        // on peut l'activer à distance
        return !isExploding;
    }

    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     * */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){

        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }

    private final class ExplosifInteractionHandler implements ICoopInteractionVisitor {
        
        // Intéraction avec un rocher : le fait disparaitre 
        @Override 
        public void interactWith(Rock rock, boolean isCellInteraction) {
            rock.destroy();

            // Est-ce que faut vraiment unregister ???
            // Je pense que non sinon le rock.destroy()
            // ne sert à rien
            //getOwnerArea().unregisterActor(rock);
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // Si la bombe explose à coté d'un player il perd
            // des points de vie
            player.loseHealth(Damage.EXPLOSION);
        }
    }
}
