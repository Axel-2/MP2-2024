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

    private Area spawnArea;
    private Area orbWayArea;

    // TO BE COMPLETED
    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * Add all the ICoop areas
     */
    private void createAreas() {
        spawnArea = new Spawn(); // Peut-être mettre en ICoop Area plutot ? jsp
        orbWayArea = new OrbWay();

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

        CenterOfMass centerOfMass = new CenterOfMass(player, player2);
        getCurrentArea().setViewCandidate(centerOfMass);

    }

    @Override
    public void update(float deltaTime) {

        // A chaque tour de boucle on doit check
        // si un des joueurs traverse une porte
        checkLeavingPlayer();


        // On update le scale factor
        //(ICoopArea) getCurrentArea().getCameraScaleFactor()

        super.update(deltaTime);
    }


    /**
     * Cette fonction check si un des deux players traverse une porte et si c'est le cas les changements
     * de positions
     * et d'areas sont effectués
     */
    private void checkLeavingPlayer() {

        ICoopPlayer[] players = {player, player2};

        DiscreteCoordinates coordinates;

        for (ICoopPlayer playerEl : players) {
            if (playerEl.isLeaving()) {
                playerEl.setLeaving(false);
                Door door = playerEl.getLeavingDoor();
                Area areaToGo;
                setCurrentArea(door.getDestinationArea(), true);

                switch (door.getDestinationArea()) {
                    case "OrbWay" -> areaToGo = orbWayArea;
                    default -> areaToGo = spawnArea;
                }

                player.leaveArea();
                player2.leaveArea();

                player.enterArea(areaToGo, door.getFuturePositions().get(0));
                player2.enterArea(areaToGo, door.getFuturePositions().get(1));

            }
        }

    }
}