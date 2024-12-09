package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.Projectiles.Unstoppable;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public final class ICoopBehavior extends AreaBehavior {
    /**
     * Default ICoopBehavior Constructor
     *
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */

    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }


    public enum ICoopCellType {

        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, false),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true, true);

        final int type;
        final boolean isWalkable;
        final boolean canFly;

        ICoopCellType(int type, boolean isWalkable, boolean canFly) {
            this.type = type;
            this.isWalkable = isWalkable;
            this.canFly = canFly;
        }

        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            return NULL;
        }

    }

    /**
     * Cell adapted to the ICoop game
     */
    public class ICoopCell extends Cell {
        /// Type of the cell following the enum
        private final ICoopCellType type;

        /**
         * Default ICoopCell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable movableEntity) {

            boolean authorisation = true;

            // Dans tous les cas, un objet de type Unstoppable
            // doit pouvoir entrer dans n'importe que cellule
            // car il "survole les cellules"
            if (movableEntity instanceof Unstoppable) {

                return true;
            }

            // Trois checks pour savoir si on peut entrer dans la cellules :
            // 1 : elle doit être Walkable
            // 2 : aucun des entities présentes ne doit prendre la place de la cellule (takeCellSpace)
            // 3 : si l'player a un élément, toutes les elemental entities présentes doivent avoir le même type

            // 1: La cellule doit être Walkable
            if (!type.isWalkable) {
                authorisation = false;
            }

            // 2 : Aucune des entités présentes ne doit prendre la place de la cellule
            for (Interactable ent : this.entities){
                if (ent.takeCellSpace()) {
                    authorisation = false;
                } 
            }

            // 3 : L'entité entrante, si elle a un élément, doit avoir le même que tous ceux des entités présentes
            if (movableEntity instanceof ElementalEntity){
                for (Interactable ent : this.entities){
                    if (ent instanceof ElementalWall){
                        if (!((ElementalWall) ent).getElement().equals(((ElementalEntity) movableEntity).getElement())) {
                            if (((ElementalWall) ent).isOn()) {
                                authorisation =  false;
                            }
                        }
                    }
                }
            }
            return authorisation;
        }
        

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }


    /** Call directly the interaction on this if accepted
     * @param v (AreaInteractionVisitor) : the visitor
     * */
     @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    }
}