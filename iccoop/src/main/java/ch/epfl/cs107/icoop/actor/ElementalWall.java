package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactor {
    
    // Indique si le mur est actif
    private Logic isActive;

    // Peut être détruit, (doit disparaître s'il l'est)
    private boolean isDestroyed;

    // L'aire, l'orientation et la position sont passées via super()

    // Image du mur
    private final Sprite sprite;

    // Constructeur classique
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Logic isActive){
        super(owner, orientation, coordinates);
        this.isActive = isActive;
        this.sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.isDestroyed = false;
    }

}
