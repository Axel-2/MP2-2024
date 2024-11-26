package ch.epfl.cs107.icoop.actor;

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

    // Constructeur principal
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions, Area ownerArea, DiscreteCoordinates mainCellPosition){
        // Il fallait un paramètre Orientation, j'ai mis Down un peu au pif  
        super(ownerArea, Orientation.DOWN, mainCellPosition);
        this.goToAreaName = goToAreaName;
        this.futurePositions = futurePositions;
        this.signal = signal;
    }

    // Autre constructeur avec l'option d'établir les positions des autres cellules que la porte occupent. Le paramètre supplémentaire est : otherCellsPosition
    // TO DO : Que faire de ce paramètre supplémentaire ? le constructeur est le même actuellement, mais peut ê
    public Door(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions,
     Area ownerArea, DiscreteCoordinates mainCellPosition, DiscreteCoordinates... otherCellsPosition){
                    super(ownerArea, Orientation.DOWN, mainCellPosition);
                    this.goToAreaName = goToAreaName;
                    this.futurePositions = futurePositions;
                    this.signal = signal;
                                     }

     /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */

     // POUR REPRENDRE OU JE ME SUIS STOP; IL FAUT DECOMMENTER CES TROIS PROCHAINES LIGNES ----------------------- CHECKPOINT -------------------
    //public List<DiscreteCoordinates> getCurrentCells(){
    //    return super.getCurrentMainCellCoordinates();
    //}
                                     
}
