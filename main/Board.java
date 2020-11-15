package main;/* Drew Schuster */

import ai.Point;
import ai.minmax.GhostLogic;
import ai.minmax.PacmanLogic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static main.MapParser.getMapFromFile;


/* Both main.Player and Ghost inherit main.Mover.  Has generic functions relevant to both*/

/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel {

    //    public Point plannedPoint;
    public ArrayList<Point> plannedPath = new ArrayList<>();
    /* Initialize the images*/
    /* For NOT JAR file*/
    Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg");
    Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");
    Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");
    Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");
    Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");
    Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg");
    Image ghostImage1 = Toolkit.getDefaultToolkit().getImage("img/ghost10.jpg");
    Image ghostImage2 = Toolkit.getDefaultToolkit().getImage("img/ghost20.jpg");
    Image ghostImage3 = Toolkit.getDefaultToolkit().getImage("img/ghost30.jpg");
    Image ghostImage4 = Toolkit.getDefaultToolkit().getImage("img/ghost40.jpg");
    Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

    /* Initialize the player and ghosts */
    Player player = new Player(200, 300);
    ArrayList<Ghost> ghosts = new ArrayList<>();
    ArrayList<Point> map = new ArrayList<>();
    /*Contains the game map, passed to player and ghosts */
    public boolean[][] state;

    /* Game dimensions */
    int gridSize;
    int offset = 20;

    boolean[][] traversedTiles;
    boolean[][] pellets;
    /* State flags*/
    boolean titleScreen;
    int newGame;
    int totalPellets = 0;
    /* Used to call sound effects */
    GameSounds sounds;

    /* This is the font used for the menus */
    Font font = new Font("Monospaced", Font.BOLD, 12);
    private ai.Point possible_next_point;
    String map_path;
    public ArrayList<Mover> movers;

    /* Constructor initializes state flags etc.*/
    public Board(int boardSize) {
        map_path = Pacman.level_maps[Pacman.level];
        sounds = new GameSounds();
        gridSize = boardSize;
        newGame = 0;
        titleScreen = true;
        traversedTiles = new boolean[gridSize][gridSize];
        pellets = new boolean[gridSize][gridSize];
//        ghosts.add(new Ghost(220,180,ghostImage4));
        reset();
    }

    public boolean[] getSurroundingArea() {
        boolean[] result = new boolean[4];
        ai.Point player_pos = player.getPosition();
        for (int i = 0; i < 4; ++i) {
            Point possible_next_point = player.moveInDirection(i);
            // If pacman can go in chosen direction, possible_next_point will be different from current position
            result[i] = !possible_next_point.equals(player_pos);
        }
        return result;
    }

    /* Reset occurs on a new game*/
    public void reset() {
        map = getMapFromFile(map_path);

        state = new boolean[gridSize - 1][gridSize - 1];
        /* Clear state and pellets arrays */
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                state[i][j] = true;
            }
        }

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (state[i][j])
                    pellets[i][j] = true;
            }
        }

        //reset traversed
        for (int i = 0; i < traversedTiles.length; i++) {
            for (int j = 0; j < traversedTiles.length; j++) {
                traversedTiles[i][j] = false;
            }
        }

        updateStateAccordingToMap();
//        plannedPoint = new Point();
        ghosts = new ArrayList<>();
        switch(map_path){
            case "level_3.map":
                player.resetPlayer(200, 300);
//                ghosts.add(new Ghost(180, 200, ghostImage1));
                ghosts.add(new Ghost(200, 200, ghostImage2));
//                ghosts.add(new Ghost(220, 200, ghostImage3));
//                pellets[9][8] = false;
//                pellets[8][9] = false;
                pellets[9][9] = false;
//                pellets[10][9] = false;
                break;
            case "first.txt":
                player.resetPlayer(200, 300);
                ghosts.add(new Ghost(180, 180, ghostImage1));
//                ghosts.add(new Ghost(200, 180, ghostImage2));
                ghosts.add(new Ghost(220, 180, ghostImage3));
                pellets[9][7] = false;
                pellets[8][8] = false;
                pellets[9][8] = false;
                pellets[10][8] = false;
                break;

        }
        plannedPath = new ArrayList<>();

        //Resetting total pellet counter
        totalPellets = 0;


        for (int i = 0; i < pellets.length; i++)
            for (int j = 0; j < pellets.length; j++)
                if (pellets[i][j])
                    totalPellets++;

        System.out.println("Total pellets on the map: " + totalPellets);
    }

    private void updateStateAccordingToMap() {
        for (Point p : map) {
            updateMap(p.x * gridSize + gridSize, p.y * gridSize + gridSize, gridSize, gridSize);
        }
    }

    public void updateMap(int x, int y, int width, int height) {
        for (int i = x / gridSize; i < x / gridSize + width / gridSize; i++) {
            for (int j = y / gridSize; j < y / gridSize + height / gridSize; j++) {
                state[i - 1][j - 1] = false;
            }
        }
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                pellets[i][j] = state[i][j];

            }
        }
    }

    private void drawPellets(Graphics g) {
        g.setColor(Color.YELLOW);
        for (int i = 0; i < pellets.length; i++)
            for (int j = 0; j < pellets.length; j++) {
                if (player.getGridPosition().equals(new Point(i, j)) && pellets[i][j]) {
                    pellets[i][j] = false;
                    player.collectedPellets++;
                    System.out.println(player.collectedPellets);
//                    System.out.println("Pellet eaten  " + (i) + ":" +(j));
                }
                if (pellets[i][j])
                    g.fillOval(i * gridSize + 8 + gridSize, j * gridSize + 8 + gridSize, 4, 4);
            }
    }

    private void drawBoard(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);

        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);

        for (Point p : map) {
            g.fillRect(p.x * gridSize + gridSize, p.y * gridSize + gridSize, gridSize, gridSize);
        }
    }

    private void drawPlanned(Graphics g) {
//        Iterator it = ((PacmanLogic)player.logic).coordinates.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//            it.remove(); // avoids a ConcurrentModificationException
//        }
    }

    private void drawPacman(Graphics g) {
        /* Draw the pacman */
        if (player.frameCount < 1) {
            /* Draw mouth closed */
            g.drawImage(pacmanImage, player.current.x, player.current.y, null);
        } else {
            /* Draw mouth open in appropriate direction */
            if (player.frameCount >= 3)
                player.frameCount = 0;

            switch (player.currDirection) {
                case 3:
                    g.drawImage(pacmanLeftImage, player.current.x, player.current.y, Color.BLACK, null);
                    break;
                case 1:
                    g.drawImage(pacmanRightImage, player.current.x, player.current.y, Color.BLACK, null);
                    break;
                case 0:
                    g.drawImage(pacmanUpImage, player.current.x, player.current.y, Color.BLACK, null);
                    break;
                case 2:
                    g.drawImage(pacmanDownImage, player.current.x, player.current.y, Color.BLACK, null);
                    break;
            }
        }
    }

    private void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            drawGhost(ghost, g);
        }
    }

    private void drawGhost(Ghost ghost, Graphics g) {
        g.drawImage(ghost.img, ghost.current.x, ghost.current.y, null);
//        if(ghost.frameCount<1){
//
//        }
//        else if (ghost.frameCount >= 3)
//            ghost.frameCount = 0;
    }

    public void paint(Graphics g) {

        /* If this is the title screen, draw the title screen and return */
        if (titleScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 600);
            g.drawImage(titleScreenImage, 0, 0, Color.BLACK, null);

            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
            newGame = 1;
            return;
        }


        /* Game initialization */
        if (newGame == 1) {
            reset();
            drawBoard(g);
            /* Send the game map to player and all ghosts */
            player.updateState(state);
            for (Ghost ghost : ghosts) {
                ghost.updateState(state);
            }
            movers = new ArrayList<>(ghosts.size() + 1);
            movers.add(player);

            player.logic = new PacmanLogic(player, this);
            for (Ghost ghost : ghosts) {
                ghost.logic = new GhostLogic(ghost, this);
                movers.add(ghost);
            }
        }

        drawBoard(g);
        drawPlanned(g);
        drawPellets(g);
        drawPacman(g);
        drawGhosts(g);


    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public Player getPacman() {
        return player;
    }

    public ArrayList<Point> getPellets() {
        ArrayList<Point> result = new ArrayList<>();
        for (int i = 0; i < pellets.length; i++)
            for (int j = 0; j < pellets.length; j++) {
                if (pellets[i][j]) result.add(new Point(i, j));
            }
        return result;
    }
}
