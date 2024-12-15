package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Représente les murs élémentaires
 */
public class ElementalWall extends AreaEntity implements ElementalEntity, Interactor, Logic {

    // Indique si c'est un mur activé en permanence
    private boolean isAlwaysActive;

    // Si il n'est pas activé en permanence, cette variable indique si il est actuellement actif
    private boolean isActive;
    
    // Peut être détruit, (doit disparaître s'il l'est)
    private boolean isDestroyed;

    // L'aire, l'orientation et la position sont passées via super()

    // Element du mur
    private Element element;

    // Image du mur
    private Sprite sprite;

    // Nom du sprite
    private String spriteName;

    // Gestionnaire d'intéraction
    private final WallInteractionHandler interactionHandler = new WallInteractionHandler();

    // Images
    private final Sprite[] wallSprites;

    // Plaque de pression associée
    private PressurePlate pressurePlate;

    /**
     * Constructeur d'un mur toujours actif, sans plaque de pression associée
     * @param owner
     * @param orientation
     * @param coordinates
     * @param elem
     */
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element elem){
        super(owner, orientation, coordinates);
        this.isAlwaysActive = true;
        this.isActive = true;
        this.element = elem;

        // Met le sprite name correspondant à l'élément
        spriteName = elem.equals(Element.FIRE) ? "fire_wall" : "water_wall";
        this.sprite = new Sprite(spriteName, 1.f, 1.f, this);
        
        // n'est pas détruit à la construction
        this.isDestroyed = false;

        this.wallSprites = RPGSprite.extractSprites(spriteName,
                4, 1, 1, this , Vector.ZERO , 256, 256);
        this.pressurePlate = null;
    }

    /**
     * Constructeur d'un mur pouvant être désactivée par une plaque de pression 
     * @param owner
     * @param orientation
     * @param coordinates
     * @param elem
     * @param pressurePlate
     */
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element elem, PressurePlate pressurePlate) {
        this(owner, orientation, coordinates, elem);
        this.pressurePlate = pressurePlate;
        this.isAlwaysActive = false;
    }

    @Override
    public boolean isOn() {
        // Si il est toujours activ, renvoie forcément true
        if (isAlwaysActive) {
            return true;
        } else {
            // Sinon, pour que ça return true, il faut que la plaque de pression associée ne soit pas appuyée et que le mur ne soit pas détruit
            return pressurePlate.isOff() && !isDestroyed;
        }
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }


    /* Retourne l'élément de l'entité */
    @Override
    public Element getElement(){
        return element;
    }

    /*
     * Détruit le mur (causé par un explosif par exemple)
     */
    public void destroy(){
        this.isDestroyed = true;
    }

    @Override
    public boolean takeCellSpace(){
        return false;
    }

    @Override
    public void draw(Canvas canvas){

        // Dessine seulement si actif
        if (isOn()){
            wallSprites[getOrientation().ordinal()].draw(canvas);
        }
    }

    public boolean isCellInteractable(){
        return true;
    }

    public boolean isViewInteractable(){
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    @Override
    public boolean wantsCellInteraction(){
        return true;
    }

    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // Fonction par défaut pout le modèle visiteur
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        // Ne souhaitant pas d'intéractions à distance, nous pouvons retourner une liste vide
        return new ArrayList<>();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gestionnaire d'intéraction du mur
     */
    private final class WallInteractionHandler implements ICoopInteractionVisitor {

        /*
         * Avec un joueur, lui fera subir des dommages
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {

            // Seulement si le mur est actif
            if (ElementalWall.this.isOn()) {
                player.loseHealth(getElement().toDamage());
            }
        }
    }
}