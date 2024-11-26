package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
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


    private final String[] areas = {"Spawn", "Orbway"};
    private int areaIndex;

    /**
     * Add all the ICoop areas
     */
    private void createAreas() {
        addArea(new Spawn());
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
        Area area = setCurrentArea("Spawn", true);
        ICoopPlayer player =  new ICoopPlayer(area, Orientation.DOWN, new DiscreteCoordinates(13,6), "shadow", Element.WATER);
        this.getCurrentArea().registerActor(player);
        //DiscreteCoordinates coords = area.getPlayerSpawnPosition();
    }



}