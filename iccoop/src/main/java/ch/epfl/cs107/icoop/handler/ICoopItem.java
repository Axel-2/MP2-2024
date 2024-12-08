package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

enum ICoopItem implements InventoryItem {

    SWORD("Sword", 0, "resources/images/sprites/sword.icon"),
    FIREKEY("FireKey", 0,  "resources/images/sprites/key_blue"),
    WATERKEY("WaterKey", 0, "resources/images/sprites/key_red"),
    FIRESTAFF("FireStaff", 0, "resources/images/sprites/staff_fire.icon"),
    WATERSTAFF("WaterStaff", 0, "resources/images/sprites/staff_water.icon"),
    EXPLOSIVE("Explosive", 0, "resources/images/sprites/explosive");

    private final String name;
    private final int pocketId;
    private final String path;

    ICoopItem(String name, int pocketId, String path){
        this.name = name;
        this.pocketId = pocketId;
        this.path = path;
    }

    /** @return (int): pocket id, the item is referred to */
    @Override
    public int getPocketId(){
        return pocketId;
    }

    /** @return (String): name of the item, not null */
    @Override
    public String getName(){
        return name;
    }

   /** @return (String): name of the item, not null */
   public String getPath(){
        return path;
}

    
}
