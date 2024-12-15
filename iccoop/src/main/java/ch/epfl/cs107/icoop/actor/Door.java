package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;


/**
Acteur qui permet de transiter vers une aire de destination

*/
public class Door extends AreaEntity {

    // Nom de l'aire vers laquelle la porte permet de transiter
    private final String goToAreaName;

    // Positions d'arrivées dans la nouvelle aire, des deux personnages (deux positions différentes)
    private final List<DiscreteCoordinates> futurePositions;


    // Variable qui permet de modéliser les conditions d'ouverture de la porte
    private Logic signal;


    // Coordonnées des autres cells occupées
    private List<DiscreteCoordinates> otherCellsCoordinates;


    // Constructeur principal
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions, Area ownerArea, DiscreteCoordinates mainCellPosition){
        // Il fallait un paramètre Orientation, j'ai mis Down un peu au pif
        super(ownerArea, Orientation.DOWN, mainCellPosition);
        this.goToAreaName = goToAreaName;
        this.futurePositions = futurePositions;
        this.signal = signal;

        // TODO ce constructeur retourne une errer mais en soit il est jamais appelé
        // TODO mais faudra trouver une solution si on a le temps
        // A revoir par la suite:
        // Est-ce qu'on laisse null comme ça ?
        this.otherCellsCoordinates = null;

    }

    // Autre constructeur avec l'option d'établir les positions des autres cellules que la porte occupent. Le paramètre supplémentaire est : otherCellsPosition
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions,
     Area ownerArea, DiscreteCoordinates mainCellPosition, DiscreteCoordinates... otherCellsPosition){
                    this(goToAreaName, signal, futurePositions, ownerArea, mainCellPosition);
                    this.otherCellsCoordinates = Arrays.asList(otherCellsPosition);
    }
                    

     /**
     * Donne une liste composée des coordonnées de la cell principale et des autres cells
     * @return (List of DiscreteCoordinates). Peut être vide mais pas null
     */

    public List<DiscreteCoordinates> getCurrentCells(){
        DiscreteCoordinates mainCellCoords = super.getCurrentMainCellCoordinates();
        List<DiscreteCoordinates> occupiedCellsCoords = new ArrayList<>();
        occupiedCellsCoords.add(mainCellCoords);

        if (otherCellsCoordinates != null) {
            for (DiscreteCoordinates coords : otherCellsCoordinates){
                occupiedCellsCoords.add(coords);
            }
        }
        return occupiedCellsCoords;
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
        return true;
    }

    
    /**@return (boolean): true if this is able to have view interactions*/
    @Override
    public boolean isViewInteractable(){
        return false;
    }


    // Pas besoin de définir un draw qui ne fait rien comme demandé dans le pdf, car Door est une sous classe en Entity
    // qui implémente Actor et qui fournit une méthode par défaut qui ne draw rien


    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     */
     @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    // On a besoin de ce getter dans ICoop
    public String getDestinationArea() {
        return goToAreaName;
    }

    // On a aussi besoin de futurePositons dans ICoop
    public List<DiscreteCoordinates> getFuturePositions() {
        return futurePositions;
    }

    // getter pour obtenir le signal dans IcoopPlayer
    public Logic getSignal() {
        return signal;
    }

    
}
