package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.KeyBindings;
import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;
import ch.epfl.cs107.icoop.actor.Collectable.Heart;
import ch.epfl.cs107.icoop.actor.Collectable.Orb;
import ch.epfl.cs107.icoop.enums.Damage;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import static ch.epfl.cs107.play.math.Orientation.DOWN;
import static ch.epfl.cs107.play.math.Orientation.LEFT;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;
import static ch.epfl.cs107.play.math.Orientation.UP;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Inventory.Holder {

    // Type (Feu ou eau)
    private final Element element;

    private final Vector anchor = new Vector(0, 0);

    // Attributs d'animation
    private final static int MOVE_DURATION = 8;
    private final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};
    private final static int ANIMATION_DURATION = 4;
    private final OrientedAnimation animation;
    private final ICoopPlayerInteractionHandler interactionHandler = new ICoopPlayerInteractionHandler();
    private final ICoopPlayerStatusGUI statusGui;

    private ICoopInventory inventory;
    private ICoopItem currentItem;

    // Touches
    private KeyBindings.PlayerKeyBindings playerKeyBindings;

    // Gestion des portes et passages
    private boolean isLeaving = false;
    private Door leavingDoor = null;

    // Barre de vie
    private static final int MAX_LIFE = 1000;
    private final Health health = new Health(this , Transform.I.translated(0, 1.75f), MAX_LIFE , true);

    // Dégats

    private boolean isInvulnerableTemporary;
    private Damage invulnerableDamageType;
    private int invulnerableDuration;
    private final static float IMMUNITY_TIME = 24;
    private float immunityTimer;
    private boolean isImmunityTime;

    /**
     * @param owner (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param spriteName (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element, boolean flipped) {
        super(owner, orientation, coordinates);


        this.element = element;

        this.inventory = new ICoopInventory();
        inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);
        currentItem = ICoopItem.EXPLOSIVE;
        // inventory.addPocketItem(ICoopItem.SWORD, 1);
 

        this.statusGui = new ICoopPlayerStatusGUI(this, flipped);

        this.animation = new OrientedAnimation(element.getSpriteName(), ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32, true);

        // Les touches sont différentes selon l'élément
        switch (element) {
            case FIRE -> playerKeyBindings = RED_PLAYER_KEY_BINDINGS;
            case WATER -> playerKeyBindings = BLUE_PLAYER_KEY_BINDINGS;
        }
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {

        // Gestion du mouvement
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(playerKeyBindings.left()));
        moveIfPressed(Orientation.UP, keyboard.get(playerKeyBindings.up()));
        moveIfPressed(Orientation.RIGHT, keyboard.get(playerKeyBindings.right()));
        moveIfPressed(Orientation.DOWN, keyboard.get(playerKeyBindings.down()));

        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            animation.reset();
        }


        // Update de la période d'immunité
        // du personnage
        if (isImmunityTime && immunityTimer > 0) {
            // Si on est dans une période d'immunité
            // on baisse le timer
            immunityTimer -= 1;
        } else {
            // Lorsque la période d'immunité est finie
            // on arrête l'immunité
            isImmunityTime = false;
        }

        // Gestion des items
        if (keyboard.get(playerKeyBindings.switchItem()).isPressed()) {
            SwitchItem();
        }
        manageUseItem(keyboard);


        super.update(deltaTime);
    }


    /*
     *  S'occupe de gérer le currentitem en fonction de ce qui est disponible dans l'inventaire
     */
    public void SwitchItem(){

        // Pour implémenter le concept de circularité, nous crééons un tableau des items (sûrement plus simple mais ça me paraissait naturel)
        List<ICoopItem> itemList = new ArrayList<>();
        for (ICoopItem i : ICoopItem.values()){
            itemList.add(i);
        }

        // On établit l'index de l'item actuel
        int currentIndex = -1;
        for (int i = 0; i < itemList.size(); i++){
            if (itemList.get(i).equals(currentItem)){
                currentIndex = i;
                break;
            }
        }

        // On cherche l'index du prochain item qu'il y a de disponible dans l'inventaire
        int nextIndex = (currentIndex + 1) % itemList.size();
        while(!inventory.contains(itemList.get(nextIndex))){
            nextIndex = (nextIndex + 1) % itemList.size();
            // Pour éviter la infinite loop qui a causé un bug
            if (nextIndex == currentIndex) {
                currentItem = null;
                return;
            }
        }

        // On update 
        currentItem = itemList.get(nextIndex);
            

    
    }

    @Override 
    public boolean possess(InventoryItem item) {
        return (item!= null && inventory.contains(item));
    }

    /**
     * S'occupe de gérer l'utilisation des items
     * @param kbd
     */
    public void manageUseItem(Keyboard kbd){
        if (kbd.get(playerKeyBindings.useItem()).isPressed() && possess(currentItem)){
            switch (currentItem){

                case EXPLOSIVE :
                    // Pose la bombe devant le joueur
                    DiscreteCoordinates position = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
                    Explosif explo = new Explosif(getOwnerArea(), getOrientation(), position, 50);
                    if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(position))){
                        this.getOwnerArea().registerActor(explo);
                        inventory.removePocketItem(ICoopItem.EXPLOSIVE, 1);
                    }

                    break;

                case SWORD :
                    // ne fait rien pour l'instant

                case WATERKEY:
                    // ne fait rien pour l'instant

                case FIREKEY:
                    // ne fait rien pour l'instant

                case WATERSTAFF:
                    // ne fait rien pour l'instant

                case FIRESTAFF:
                    // ne fait rien pour l'instant

            }
            if (!inventory.contains(currentItem)){
                SwitchItem();
            }

        }
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {

        // Lorsqu'il y a état d'immunité
        // le personnage "clignote"
        // sinon l'affichage est normale
        if (isImmunityTime) {
            if (immunityTimer % 2 == 0) {
                animation.draw(canvas);
            }
        } else {
            animation.draw(canvas);
        }

        // Il faut aussi dessiner la barre de vie
        health.draw(canvas);

        // Dessin des items
        statusGui.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {

        // On met uniquement la cellule principale
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * makes the player entering a given area
     * @param area     (Area):  the area to be entered, not null
     * @param position (DiscreteCoordinates): initial position in the entered area, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        //area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }


    /**
     * Retourne l'élément du ICoopPlayer (Feu ou eau)
     */
    @Override
    public Element getElement() {
        return this.element;
    }

     /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
    */
     @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Get this Interactor's curr
     *
     * ent field of view cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        return Collections.singletonList
        (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    
    }

    /**@return (boolean): true if this require cell interaction */
    @Override
    public boolean wantsCellInteraction() {
        // Veut systématiquement toutes les intéractions de contact
        return true;
    }

    /**@return (boolean): true if this require view interaction */
    @Override
    public boolean wantsViewInteraction() {

        // On veut les intéractions à distance seulement si le joueur appuie sur la touche useItem
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(playerKeyBindings.useItem()).isPressed();

    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

        // Il faut faire de cette manière pour utiliser le handler
        other.acceptInteraction(interactionHandler, isCellInteraction);

    }

    // PARTIe QUI CONCERNE LES PORTES ET TRANSFERTS

    public Door getLeavingDoor() {
        return leavingDoor;
    }

    public void setLeaving(Boolean leaving) {
        isLeaving = leaving;
    }

    // Getter pour Icoop.java
    public boolean isLeaving() {
        return isLeaving;
    }

    // PARTIE QUI CONCERNE LES DEGATS


    // Fonction qui permet de devenir inbulnerable à
    // une certaine attaque pendant un certain temps
    public void becomeInvulnerable(Damage damage, boolean isTemporary, int duration) {
        this.isInvulnerableTemporary = isTemporary;
        this.invulnerableDamageType = damage;
        this.invulnerableDuration = duration;
    }

    public void loseHealth(Damage damage) {

        // Si le personnage est invulnérable ou immune rien ne se passe
        if (damage.equals(invulnerableDamageType) || isImmunityTime) {
            // Rien ne se passe
        } else {
            health.decrease(damage.getDamagePoints());
            isImmunityTime = true;
            immunityTimer = IMMUNITY_TIME;
        }

    }

    // ON a besoin de ce getter dans ICoop
    public void resetHealth() {
        health.resetHealth();
    }

    // On a besoin de ce getter dans ICoop
    public boolean isAlive() {
        return health.isOn();
    }



    public ICoopItem getCurrentItem(){
        return currentItem;
    }

    private final class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Door door, boolean isCellInteraction) {

            // Vérification des accès
            if (door.getSignal().isOn()) {

                // On update pes attributs pour que
                // le fichier principal puisse faire
                // transiter le joueur
                isLeaving = true;
                leavingDoor = door;
            }
            
        }

        @Override
        public void interactWith(Explosif explo, boolean isCellInteraction){
            // Interaction à distance only, donc si le joueur presse le bouton pour useitem()
            Keyboard keyboard = getOwnerArea().getKeyboard();

            // Si c'est une intéraction de contact, on prend l'objet
            if (isCellInteraction) {
                
                // On ne veut pas qu'il se collecte plusieurs fois
                if (!explo.isCollected()){
                    explo.collect();
                    if(currentItem == null){
                        currentItem = ICoopItem.EXPLOSIVE;
                    }

                    // ajoute à l'inventaire ( il y a peut être mieux que de le faire là )
                    inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);

                }


            } else {
                // Si c'est à distance on active la bombe
                if (keyboard.get(playerKeyBindings.useItem()).isPressed()) {
                    explo.activate();
                }
            }
        }


        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (isCellInteraction){

                // On collecte
                if (isCellInteraction) {
                    orb.collectBy(ICoopPlayer.this);
                }

                // on le rend résistant aux murs
                invulnerableDamageType = orb.getDamage();
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {

            // On augmente la vie seulement si c'est pas encore collecté
            if (!heart.isCollected()) {
                // Un point seulement dans la doc mais sinon on voit rien
                // A CHANGER PLUS TARD
                health.increase(10);
                heart.collect();
            }


        }
    }
}