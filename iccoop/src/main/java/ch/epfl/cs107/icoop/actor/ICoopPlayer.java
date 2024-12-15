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
import ch.epfl.cs107.icoop.actor.Collectable.Staff;
import ch.epfl.cs107.icoop.actor.Foes.Foe;
import ch.epfl.cs107.icoop.actor.Projectiles.StaffBall;
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
    private OrientedAnimation currentAnimation;

    private final static int MOVE_DURATION = 8;
    private final Orientation[] orders = {DOWN , RIGHT, UP, LEFT};
    private final Orientation[] itemOrders = {DOWN , UP, RIGHT, LEFT}; // Pour les animations du staff et de l'épée, l'ordre d'en haut n'est pas correct

    private final static int ANIMATION_DURATION = 4;
    private final OrientedAnimation defaultAnimation;

    private final Vector swordAnchor = new Vector(-.5f, 0);
    private final static int SWORD_ANIMATION_DURATION = 2;
    private final OrientedAnimation swordAnimation;

    private final Vector staffAnchor = new Vector(-.5f, -.20f);
    private final static int STAFF_ANIMATION_DURATION = 2;
    private final OrientedAnimation staffAnimation;


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
    private static final int MAX_LIFE = 100;
    private final Health health = new Health(this , Transform.I.translated(0, 1.75f), MAX_LIFE , true);

    // Dégats

    private boolean isInvulnerableTemporary;
    private Damage invulnerableDamageType;
    private int invulnerableDuration;
    private final static float IMMUNITY_TIME = 24;
    private float immunityTimer;
    private boolean isImmunityTime;

    // Etats

    // par défaut en IDLE
    private  PlayerState playerState = PlayerState.IDLE;

    /**
     * @param owner (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param spriteName (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element, boolean flipped) {
        super(owner, orientation, coordinates);


        this.element = element;

        // Création de l'inventaire
        this.inventory = new ICoopInventory();


        inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);
        inventory.addPocketItem(ICoopItem.SWORD,1);
        updateCurrentItem();
 
        // barre d'état
        this.statusGui = new ICoopPlayerStatusGUI(this, flipped);

        // Animations
        defaultAnimation = new OrientedAnimation(element.getSpriteName(), ANIMATION_DURATION, this, anchor, orders,
        4, 1, 2, 16, 32, true);

        swordAnimation =  new OrientedAnimation(element.getSpriteName()+".sword",
                SWORD_ANIMATION_DURATION , this ,
                swordAnchor , itemOrders , 4, 2, 2, 32, 32);

        // Conditions pour séléctionner la bonne sprite pour staffAnimation
        String staffAnimationName = (element.equals(Element.FIRE)) ? "icoop/player.staff_fire" : "icoop/player2.staff_water";
        staffAnimation = new OrientedAnimation(staffAnimationName , STAFF_ANIMATION_DURATION , this ,
                staffAnchor , itemOrders , 4, 2, 2, 32, 32);


        // Les touches sont différentes selon l'élément
        switch (element) {
            case FIRE -> playerKeyBindings = RED_PLAYER_KEY_BINDINGS;
            case WATER -> playerKeyBindings = BLUE_PLAYER_KEY_BINDINGS;
        }

        currentAnimation = defaultAnimation;
    }

    private enum PlayerState {
        IDLE,
        SWORD,
        STAFF;
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {

        // Gestion du mouvement
        Keyboard keyboard = getOwnerArea().getKeyboard();
       
        if(playerState.equals(PlayerState.IDLE)){

            moveIfPressed(Orientation.LEFT, keyboard.get(playerKeyBindings.left()));
            moveIfPressed(Orientation.UP, keyboard.get(playerKeyBindings.up()));
            moveIfPressed(Orientation.RIGHT, keyboard.get(playerKeyBindings.right()));
            moveIfPressed(Orientation.DOWN, keyboard.get(playerKeyBindings.down()));

            if (isDisplacementOccurs()) {
                currentAnimation.update(deltaTime);
            } else {
                currentAnimation.reset();
            }
        }

        if (!playerState.equals(PlayerState.IDLE)) {
            if (!currentAnimation.isCompleted()) {
                currentAnimation.update(deltaTime);
            } else {

                // Check la touche est encore appuyée
                if (keyboard.get(playerKeyBindings.useItem()).isDown()) {

                    // Recommence l'animation de l'épée si la touche est encore appuyée
                    currentAnimation.reset();
                } else {
                    // Retour à l'état IDLE seulement si aucune action n'est en cours
                    currentAnimation = defaultAnimation;
                    playerState = PlayerState.IDLE;
                }
            }
        
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
        int currentIndex = 0;
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
        return (item != null && inventory.contains(item));
    }

    /**
     * S'occupe de gérer l'utilisation des items
     * @param kbd
     */
    public void manageUseItem(Keyboard kbd){
        
        // Si la touche est pressée 
        if (kbd.get(playerKeyBindings.useItem()).isPressed()){

            // On récupère la case de devant qui sera utile pour chacun des cas
            DiscreteCoordinates frontCellPosition = getCurrentMainCellCoordinates().jump(getOrientation().toVector());

            // En fonction de l'item actuel 
            switch (currentItem){
                case null:
                    break;

                case EXPLOSIVE :
                    // Pose la bombe devant le joueur

                    if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(frontCellPosition))) {
                        Explosif explo = new Explosif(getOwnerArea(), getOrientation(), frontCellPosition, 3);
                        this.getOwnerArea().registerActor(explo);
                        inventory.removePocketItem(currentItem, 1);
                    }
                    break;

                case SWORD :
                    playerState = PlayerState.SWORD;
                    currentAnimation = swordAnimation;
                    break;


                case WATERKEY:
                    // ne fait rien pour l'instant

                case FIREKEY:
                    // ne fait rien pour l'instant

                case WATERSTAFF:
                    // Lance une boule d'eau
                    playerState = PlayerState.STAFF;
                    currentAnimation = staffAnimation;
                    if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(frontCellPosition))) {
                        launchBall(frontCellPosition, Element.WATER);
                    }
                    break;

                case FIRESTAFF:
                    // Lance une boule de feu
                    playerState = PlayerState.STAFF;
                    currentAnimation = staffAnimation;
                    if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(frontCellPosition))) {
                        launchBall(frontCellPosition, Element.FIRE);
                    }
                    break;

            }

        }
        updateCurrentItem();
    }
        /*
     * Met à jour le current item
     * Utile lors d'une collecte, ou pour la disparition d'un objet utilisé
     */
    private void updateCurrentItem(){
        if (currentItem == null || !inventory.contains(currentItem)){
            for (ICoopItem item : ICoopItem.values()){
                if (inventory.contains(item)){
                    currentItem = item;
                    return;
                }
            }
            currentItem = null;
        }

    }

    // Lance une boule de feu ou d'eau
    public void launchBall(DiscreteCoordinates position, Element elem){
        playerState = PlayerState.STAFF;
        StaffBall Ball = new StaffBall(getOwnerArea(), getOrientation(), position, 3, 200, elem);
        this.getOwnerArea().registerActor(Ball);
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
                currentAnimation.draw(canvas);
            }
        } else {
            currentAnimation.draw(canvas);
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

        // On veut les intéractions à distance seulement si le joueur est entrain de donné un coup d'épée
        return playerState.equals(PlayerState.SWORD);

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
                    // ajoute à l'inventaire ( il y a peut être mieux que de le faire là )
                    inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);
                    updateCurrentItem();

                }


            } else {
                // Si c'est à distance on active la bombe
                if (keyboard.get(playerKeyBindings.useItem()).isPressed()) {
                    explo.activate(1);
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
        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {

            // On récupère le baton !
            if (isCellInteraction && element == staff.getElement()){
                if (!staff.isCollected()) {
                    staff.collect();
                    ICoopItem itemToAdd = staff.getElement() == Element.FIRE ? ICoopItem.FIRESTAFF : ICoopItem.WATERSTAFF;
                    inventory.addPocketItem(itemToAdd, 1);
                    updateCurrentItem();
                }
            }
        }
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            foe.loseHealth(Damage.PHYSICAL);
            System.out.println("Damage dealt to Foe. Remaining health: " + foe.getHealthIntensity());
            
        }
    }
}