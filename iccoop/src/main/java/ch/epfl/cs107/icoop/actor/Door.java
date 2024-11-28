package ch.epfl.cs107.icoop.actor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;


/**
Acteur qui permet de transiter vers une aire de destination

*/
public class Door extends AreaEntity {

    // Nom de l'aire vers laquelle la porte permet de transiter
    private String goToAreaName;

    // Positions d'arrivées dans la nouvelle aire, des deux personnages (deux positions différentes)
    private List<DiscreteCoordinates> futurePositions;

    // Variable qui permet de modéliser les conditions d'ouverture de la porte
    private Logic signal;

    // Coordonnées des autres cells occupées 
    private List<DiscreteCoordinates> otherCellsCoordoniates;


    // Constructeur principal
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions, Area ownerArea, DiscreteCoordinates mainCellPosition){
        // Il fallait un paramètre Orientation, j'ai mis Down un peu au pif
        super(ownerArea, Orientation.DOWN, mainCellPosition);
        this.goToAreaName = goToAreaName;
        this.futurePositions = futurePositions;
        this.signal = signal;
        this.otherCellsCoordoniates = null;

    }

    // Autre constructeur avec l'option d'établir les positions des autres cellules que la porte occupent. Le paramètre supplémentaire est : otherCellsPosition
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions,
     Area ownerArea, DiscreteCoordinates mainCellPosition, DiscreteCoordinates... otherCellsPosition){
                    super(ownerArea, Orientation.DOWN, mainCellPosition);
                    this.goToAreaName = goToAreaName;
                    this.futurePositions = futurePositions;
                    this.signal = signal;
                    this.otherCellsCoordoniates = Arrays.asList(otherCellsPosition);

                                     }
                    

     /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */

     // Donne une liste composée des coordonnées de la cell principale et des autres cells
    public List<DiscreteCoordinates> getCurrentCells(){
        DiscreteCoordinates mainCellCoords = super.getCurrentMainCellCoordinates();
        List<DiscreteCoordinates> occupiedCellsCoords = new ArrayList<>();
        occupiedCellsCoords.add(mainCellCoords);
        for (DiscreteCoordinates coords : otherCellsCoordoniates){
            occupiedCellsCoords.add(coords);
        }
        return occupiedCellsCoords;
    }
    /**
     * Indicate if the current Interactable take the whole cell space or not
     * i.e. only one Interactable which takeCellSpace can be in a cell
     * (how many Interactable which don't takeCellSpace can also be in the same cell)
     * @return (boolean)
     */
    public boolean takeCellSpace(){
        return false;
    }

    /**@return (boolean): true if this is able to have cell interactions*/
    public boolean isCellInteractable(){
        return true;
    }

    /**@return (boolean): true if this is able to have view interactions*/
    public boolean isViewInteractable(){
        return false;
    }

    // Pas besoin de définir un draw qui ne fait rien comme demandé dans le pdf, car Door est une sous classe en Entity
    // qui implémente Actor et qui fournit une méthode par défaut qui ne draw rien

    
}
