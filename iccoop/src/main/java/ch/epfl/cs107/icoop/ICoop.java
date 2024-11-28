package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {


    // Je pense que les players doivent etre des attributs de la classe car on doit
    // pouvoir les utiliser dans update
    private ICoopPlayer player;
    private ICoopPlayer player2;

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
        Area spawnArea = new Spawn(); // Peut-être mettre en ICoop Area plutot ? jsp
        Area orbWayArea = new OrbWay();

        addArea(spawnArea);
        addArea(orbWayArea);
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
            initGame();
            return true;
        }
        return false;
    }


    private void initGame() {

        // Le jeu commence dans l'aire spwan
        ICoopArea area = (ICoopArea) setCurrentArea("Spawn", true);


        // ----- JOUEURS -----

        // Création du joueur 1
        DiscreteCoordinates coords = area.getPlayerSpawnPosition(Element.WATER);
        player =  new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player", Element.WATER);

        // Création du joueur 2
        coords =  area.getPlayerSpawnPosition(Element.FIRE);
        player2 = new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player2", Element.FIRE);

        // Register des deux joueurs
        this.getCurrentArea().registerActor(player);
        this.getCurrentArea().registerActor(player2);



    }

    @Override
    public void update(float deltaTime) {
        checkLeavingPlayer();
        super.update(deltaTime);
    }


    /**
     * Cette fonction check si un des deux player veut changer d'Area
     * a l'aide d'une porte.
     */
    private void checkLeavingPlayer() {

        ICoopPlayer[] players = {player, player2};

        DiscreteCoordinates coordinates;

        for (ICoopPlayer playerEl : players) {
            if (playerEl.isLeaving()) {
                Door door = playerEl.getLeavingDoor();

                if (playerEl.getElement().name().equals("FIRE")) {
                    coordinates = door.getFuturePositions().get(0);
                } else {
                    coordinates = door.getFuturePositions().get(1);
                }

                playerEl.changePosition(coordinates);
                setCurrentArea(door.getDestinationArea(), true);

            }
        }

    }
}