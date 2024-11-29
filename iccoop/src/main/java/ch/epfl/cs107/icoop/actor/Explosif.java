package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.List;

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

    private boolean isActivated;
    private int counter;
    private boolean isExploding;
    private final static int ANIMATION_DURATION = 7;
    private ExplosifInteractionHandler interactionHandler = new ExplosifInteractionHandler();
    private Animation normalAnimation;
    private Animation explosionAnimation;


    // Constructeur ici
    public Explosif(Area area, Orientation orientation, DiscreteCoordinates position, int counter){
        super(area, orientation, position);
        this.isActivated = false;
        this.isExploding = false;
        this.counter = counter;
        
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if (isActivated){
        counter -= 1;
        }

        if(counter <= 0){
            isExploding = true;
            getOwnerArea().unregisterActor(this);
        }

    }


    public void animateExplosion(Canvas canva){
        
        Animation explosionAnimation = new Animation("icoop/explosion", 7, 1, 1, this , 32, 32,
        Explosif.ANIMATION_DURATION/7, false);
        explosionAnimation.draw(canva);
        
    }

    @Override
    public void draw(Canvas canva){
        if (isExploding){
            animateExplosion(canva);
        }
    
    }
    /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells(){

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
    public boolean wantsCellInteraction(){
        return(isExploding);
    }
    @Override
    /**@return (boolean): true if this require view interaction */
    public boolean wantsViewInteraction(){
        return(isExploding);
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
        return false;
    }


    /**@return (boolean): true if this is able to have cell interactions*/
    @Override
    public boolean isCellInteractable(){
        return(!isExploding && isActivated);
    }

    /**@return (boolean): true if this is able to have view interactions*/
    @Override
    public boolean isViewInteractable(){
        return (!isExploding);
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
            getOwnerArea().unregisterActor(rock);
        }
    }
}
