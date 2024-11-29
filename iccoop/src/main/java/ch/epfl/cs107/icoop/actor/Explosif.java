package ch.epfl.cs107.icoop.actor;

import java.util.List;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

//public class Explosif extends AreaEntity implements Interactor {
//
//    private boolean isActivated;
//    private int counter;
//    private boolean isExploding;
//
//    // Constructeur ici
//    public Explosif(Area area, Orientation orientation, DiscreteCoordinates position, int counter){
//        super(area, orientation, position);
//        this.isActivated = false;
//        this.isExploding = false;
//        this.counter = counter;
//    }
//
//    public void update(float deltaTime){
//        super.update(deltaTime);
//        if (isActivated){
//        counter -= 1;
//        }
//
//        if(counter <= 0){
//            isExploding = true;
//            explosion
//        }
//
//    }
//
//
//        /**
//     * Get this Interactor's current occupying cells coordinates
//     * @return (List of DiscreteCoordinates). May be empty but not null
//     */
//    @Override
//    List<DiscreteCoordinates> getCurrentCells(){
//
//    }
//
//
//    /**
//     * Get this Interactor's current field of view cells coordinates
//     * @return (List of DiscreteCoordinates). May be empty but not null
//     */
//    @Override
//    List<DiscreteCoordinates> getFieldOfViewCells(){
//
//    }
//
//
//    /**@return (boolean): true if this require cell interaction */
//    @Override
//    boolean wantsCellInteraction(){
//
//    }
//    @Override
//    /**@return (boolean): true if this require view interaction */
//    boolean wantsViewInteraction(){
//
//    }
//
//    /**
//     * Do this Interactor interact with the given Interactable
//     * The interaction is implemented on the interactor side !
//     * @param other (Interactable). Not null
//     * @param isCellInteraction True if this is a cell interaction
//     */
//    @Override
//    void interactWith(Interactable other, boolean isCellInteraction){
//
//    }
//
//        /**
//     * Indicate if the current Interactable take the whole cell space or not
//     * i.e. only one Interactable which takeCellSpace can be in a cell
//     * (how many Interactable which don't takeCellSpace can also be in the same cell)
//     * @return (boolean)
//     */
//    @Override
//    public boolean takeCellSpace(){
//        return false;
//    }
//
//
//    /**@return (boolean): true if this is able to have cell interactions*/
//    @Override
//    public boolean isCellInteractable(){
//
//    }
//
//    /**@return (boolean): true if this is able to have view interactions*/
//    @Override
//    boolean isViewInteractable(){
//
//    }
//
//    /** Call directly the interaction on this if accepted
//     * @param v (AreaInteractionVisitor) : the visitor
//     * */
//    @Override
//    void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
//                ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
//
//    }
//}
