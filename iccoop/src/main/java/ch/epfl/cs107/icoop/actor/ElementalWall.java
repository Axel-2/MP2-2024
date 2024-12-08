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

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactor, Logic {

    private boolean isActive;
    private boolean isAlwaysActive;

    @Override
    public boolean isOn() {
        if (isAlwaysActive) {
            return true;
        } else {
            // Si la plaque est off le mur est activée
            return pressurePlate.isOff();
        }
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }

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

    // TEST
    private final Sprite[] wallSprites;

    private PressurePlate pressurePlate;

    // Constructeur classique toujours actif
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element elem){
        super(owner, orientation, coordinates);
        this.isAlwaysActive = true;
        this.isActive = true;
        this.element = elem;
        if (elem.equals(Element.FIRE)){
            spriteName = "fire_wall";
        }else{
            spriteName = "water_wall";
        }
        this.sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.isDestroyed = false;
        this.wallSprites = RPGSprite.extractSprites(spriteName,
                4, 1, 1, this , Vector.ZERO , 256, 256);
        this.pressurePlate = null;
    }

    // Constructeur avec plaque de pression
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element elem, PressurePlate pressurePlate) {
        this(owner, orientation, coordinates, elem);
        this.pressurePlate = pressurePlate;
        this.isAlwaysActive = false;
    }

    /* Retourne l'élément de l'entité */
    @Override
    public Element getElement(){
        return element;
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

    @Override
    public void draw(Canvas canvas){

        if (isOn()){
            wallSprites[getOrientation().ordinal()].draw(canvas);
            // super.draw(canvas); pas sûr de cette ligne je commente pour l'instant
        }
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

    /**
     * Do this Interactor interact with the given Interactable
     * The interaction is implemented on the interactor side !
     *
     * @param other             (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction(){
        return true;
    }

    /**@return (boolean): true if this require view interaction */
    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    /**
     * Call directly the interaction on this if accepted
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // Fonction par défaut pout le modèle visiteur
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }

    /**
     * Pas besoin de lui donner un corps spécifique étant donné qu'aucune intéraciton à distance n'intéresse cet objet
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return new ArrayList<>();
    }

    /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // ----- Handler -----
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