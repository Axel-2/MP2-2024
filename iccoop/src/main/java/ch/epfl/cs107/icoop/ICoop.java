package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.enums.Element;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame implements DialogHandler {


    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private ICoopPlayer[] players;

    private Area spawnArea;
    private Area orbWayArea;

    private Dialog activeDialog = null;

    // TO BE COMPLETED
    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * Add all the ICoop areas
     */
    private void createAreas() {
        spawnArea = new Spawn(this); // Peut-être mettre en ICoop Area plutot ? jsp
        orbWayArea = new OrbWay(this);

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

        // ORBWAY POUR DEBUG
        ICoopArea area = (ICoopArea) setCurrentArea("OrbWay", true);
        createPlayers(area);

        // On centre la caméra sur le centre de masse
        setCamera();

        // On met le dialog welcome au début du jeu
        setActiveDialog("welcome");


    }

    private void createPlayers(ICoopArea area) {
        // ----- JOUEURS -----

        // Création du joueur 1
        DiscreteCoordinates coords = area.getPlayerSpawnPosition(Element.WATER);
        player1 =  new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player", Element.WATER);

        // Création du joueur 2
        coords =  area.getPlayerSpawnPosition(Element.FIRE);
        player2 = new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player2", Element.FIRE);


        // Register des acteurs
        this.getCurrentArea().registerActor(player1);
        this.getCurrentArea().registerActor(player2);

        players = new ICoopPlayer[2];
        players[0] = player1;
        players[1] = player2;

    }

    private void setCamera() {

        CenterOfMass centerOfMass = new CenterOfMass(player1, player2);
        getCurrentArea().setViewCandidate(centerOfMass);
    }


    @Override
    public void update(float deltaTime) {
        

        // A chaque tour de boucle on doit check
        // si un des joueurs traverse une porte
        checkLeavingPlayer();

        // A chaque tout on check si
        // une touche pour reset est pressée
        checkReset();

        // On test si les joueurs ont encore de la
        // vie
        checkHealth();

        // S'occupe des dialogues : affichage et skip si "next dialog" est appuyée
        checkDialog(deltaTime);

        // Ajustement du scale factor
        ICoopArea currentICoopArea = (ICoopArea) getCurrentArea();
        currentICoopArea.updateScaleFactor(player1, player2);
        

        super.update(deltaTime);

    }

    @Override
    public void draw() {

        if (activeDialog != null) {
            activeDialog.draw(getWindow());
        }
        super.draw();
    }

    // On test si les players ont encore de la
    // vie sinon on reset la map
    private void checkHealth() {
        for (ICoopPlayer player : players) {
            if (!player.isAlive()) {
                resetMap();
            }
        }
    }

    // Partie qui gère les dialogues

    private void checkDialog(float deltaTime){
        Keyboard kbd = getCurrentArea().getKeyboard();

        // Affiche le dialogue s'il y en a un en cours, et pause le jeu
        if (activeDialog != null){
            //activeDialog.draw(getWindow());

            // Pause pendant les dialogues
            //if (!getCurrentArea().isPaused()){
            //    getCurrentArea().requestPause();
            //}
            getCurrentArea().requestPause();
            

            // Check si le joueur veut skip le dialogue
            if (kbd.get(KeyBindings.NEXT_DIALOG).isPressed()){
                activeDialog.update(deltaTime);
            }

            // Enlève le dialogue s'il est terminé
            if (activeDialog.isCompleted()){
                activeDialog = null;
                getCurrentArea().requestResume();
            }
        }


    }

    public void setActiveDialog(String fileName){
        activeDialog = new Dialog(fileName);
    }

    @Override
    public void publish(Dialog dialog) {
        this.activeDialog = dialog;
    }

    private void checkReset() {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (keyboard.get(KeyBindings.RESET_AREA).isPressed()) {
            resetMap();
        } else if (keyboard.get(KeyBindings.RESET_GAME).isPressed()) {

            // Ici il suffit de réinitialiser le jeu en entier
            this.begin(getWindow(), getFileSystem());

            // On reset la vie aussi
            resetPlayersHealth();
        }
    }

    private void resetMap() {
        // On reéinitialise la map
        getCurrentArea().begin(getWindow(), getFileSystem());

        // On remet les joueurs
        createPlayers((ICoopArea) getCurrentArea());

        // Il ne faut pas oublier de remettre la camera centre de masse
        setCamera();

        // On reset la vie
        resetPlayersHealth();
    }

    private void resetPlayersHealth() {
        player1.resetHealth();
        player2.resetHealth();
    }


    /**
     * Cette fonction check si un des deux players traverse une porte et si c'est le cas les changements
     * de positions
     * et d'areas sont effectués
     */
    private void checkLeavingPlayer() {

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

                player1.leaveArea();
                player2.leaveArea();

                player1.enterArea(areaToGo, door.getFuturePositions().get(0));
                player2.enterArea(areaToGo, door.getFuturePositions().get(1));

                setCamera();

            }
        }

    }
}