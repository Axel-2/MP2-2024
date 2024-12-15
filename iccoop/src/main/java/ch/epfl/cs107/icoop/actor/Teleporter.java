package ch.epfl.cs107.icoop.actor;

import java.util.List;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Représente les téléporteurs, une spécification des portes
 */
public class Teleporter extends Door implements Logic {

    // Image
    private final RPGSprite sprite;

    // TODO comment faire pour ne pas déclarer deux fois le sprite ????

    /**
     * Constructeur d'un téléporteur présent sur une seule cellule principale
     * @param goToAreaName
     * @param signal
     * @param futurePositions
     * @param ownerArea
     * @param mainCellPosition
     */
    public Teleporter(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions, Area ownerArea, DiscreteCoordinates mainCellPosition) {
        super(goToAreaName, signal, futurePositions, ownerArea, mainCellPosition);
        this.sprite = new RPGSprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32,
                32));
    }

    /**
     * Constructeur d'un téléporteur présent sur plusieurs cases
     * @param goToAreaName
     * @param signal
     * @param futurePositions
     * @param ownerArea
     * @param mainCellPosition
     * @param otherCellsPosition
     */
    public Teleporter(String goToAreaName, Logic signal, List<DiscreteCoordinates> futurePositions, Area ownerArea, DiscreteCoordinates mainCellPosition, DiscreteCoordinates... otherCellsPosition) {
        super(goToAreaName, signal, futurePositions, ownerArea, mainCellPosition, otherCellsPosition);
        this.sprite = new RPGSprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32,
                32));
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOn()) {
            sprite.draw(canvas);
            super.draw(canvas);
        }
    }

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isOff() {
        return false;
    }
}
