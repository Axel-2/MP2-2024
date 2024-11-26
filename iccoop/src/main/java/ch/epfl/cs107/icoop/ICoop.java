package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {
    // TO BE COMPLETED
    @Override
    public String getTitle() {
        return "ICoop";
    }


    private final String[] areas = {"Spawn", "OrbWay"};
    private int areaIndex;

    /**
     * Add all the ICoop areas
     */
    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
    }

    /**
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the game begins properly
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }


    /**
     * sets the area named `areaKey` as current area in the game Tuto2
     * @param areaKey (String) title of an area
     */
    private void initArea(String areaKey) {
        //ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        ICoopArea area = (ICoopArea) setCurrentArea("Spawn", true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition(Element.WATER);
        ICoopPlayer player =  new ICoopPlayer(area, Orientation.DOWN, coords, "shadow", Element.WATER);
        coords =  area.getPlayerSpawnPosition(Element.FIRE);
        ICoopPlayer player2 = new ICoopPlayer(area, Orientation.DOWN, coords, "shadow", Element.FIRE);
        this.getCurrentArea().registerActor(player);
        this.getCurrentArea().registerActor(player2);
    }



}