package ch.epfl.cs107.icoop;


import java.util.Arrays;

import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
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
        ICoopPlayer player =  new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player", Element.WATER);

        // Création du joueur 2
        coords =  area.getPlayerSpawnPosition(Element.FIRE);
        ICoopPlayer player2 = new ICoopPlayer(area, Orientation.DOWN, coords, "icoop/player2", Element.FIRE);

        // Register des deux joueurs
        this.getCurrentArea().registerActor(player);
        this.getCurrentArea().registerActor(player2);

        // ----- PORTES -----

        // Création de la porte dans le spawn, qui mènera à orbway
        Door spawnDoor = new Door(
            "OrbWay",                                                                // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                           // Toujours open
            Arrays.asList(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            area,                                                                                 // Map actuelle, donc Spawn
            new DiscreteCoordinates(19,15),                                                   // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(19,16)                                                    // Autre cellule de la porte (l'autre "case" rouge)
            );


        // Créations des deux portes de OrbWay qui permettront de revenir à Spawn
        // 1 TO DO : JE SAIS  PAS COMMENT ACCEDER A L'AREA ORBWAY POUR LE 4EME PARAMETRE, ICI C'EST AREA DONC C'EST SPAWN
        Door orbWayDoor1 = new Door(
            "Spawn",                                                                    // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                              // Toujours open
            Arrays.asList(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            area,                                                                                    // Map actuelle, donc Spawn
            new DiscreteCoordinates(0,14),                                                       // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(0,13),
            new DiscreteCoordinates(0,12),                                                       // Autres cellules occupées par les portes
            new DiscreteCoordinates(0,11),      
            new DiscreteCoordinates(0,10)                                                    
            );

        // 2 TO DO : JE SAIS  PAS COMMENT ACCEDER A L'AREA ORBWAY POUR LE 4EME PARAMETRE, ICI C'EST AREA DONC C'EST SPAWN
        Door orbWayDoor2 = new Door(
            "Spawn",                                                                    // Aire vers laquelle la porte emmène
            Logic.TRUE,                                                                              // Toujours open
            Arrays.asList(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)), // les deux positions d'arrivées dans OrbWay après avoir pris la porte
            area,                                                                                    // Map actuelle, donc Spawn
            new DiscreteCoordinates(0,8),                                                        // Cellule principale de la porte (une des deux "cases" rouges)
            new DiscreteCoordinates(0,7),
            new DiscreteCoordinates(0,6),                                                        // Autres cellules occupées par les portes
            new DiscreteCoordinates(0,5),      
            new DiscreteCoordinates(0,4)                                                    
            );


        // ----- Register des 3 portes -----    
        this.getCurrentArea().registerActor(spawnDoor);
        this.getCurrentArea().registerActor(orbWayDoor1);
        this.getCurrentArea().registerActor(orbWayDoor2);

        

        // Gestion du centre de masse, et point de vue associé
        CenterOfMass centerOfMass = new CenterOfMass(player, player2);
        area.setViewCandidate(centerOfMass);

    }



}