/* Drew Schuster */

import ai.Point;

import java.awt.*;

import javax.swing.JPanel;
import java.util.*;
import java.io.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


/* Both Player and Ghost inherit Mover.  Has generic functions relevant to both*/
class Mover {
    /* Framecount is used to count animation frames*/
    public int frameCount = 0;
    public long stableFCount=0;

    /* State contains the game map */
    boolean[][] state;

    /* gridSize is the size of one square in the game.
       max is the height/width of the game.
       increment is the speed at which the object moves,
       1 increment per move() call */
    int gridSize = 20;
    int max;
    int increment;

    /* Generic constructor */
    public Mover() {
        increment = 20;
        max = 400;
        state = new boolean[gridSize -1][gridSize -1];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                state[i][j] = false;
            }
        }
    }

    /* Updates the state information */
    public void updateState(boolean[][] state) {
        for (int i = 0; i < state.length; i++)
            for (int j = 0; j < state.length; j++)
                this.state[i][j] = state[i][j];
    }

    /* Determines if a set of coordinates is a valid destination.*/
    public boolean isValidDest(int x, int y) {
    /* The first statements check that the x and y are inbounds.  The last statement checks the map to
       see if it's a valid location */
        return (((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400 && state[x / 20 - 1][y / 20 - 1];
    }

}


/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel {

    public Point plannedPoint;
    public ArrayList<Point> plannedPath;
    /* Initialize the images*/
    /* For NOT JAR file*/
    Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg");
    Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");
    Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");
    Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");
    Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");
    Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg");
    Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

    /* Initialize the player and ghosts */
    Player player = new Player(200, 300);

    /* Score information */
    int currScore;
    int highScore;

    /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
    boolean clearHighScores = false;

    /*Contains the game map, passed to player and ghosts */
    boolean[][] state;


    /* Game dimensions */
    int gridSize;
    int offset = 20;

    boolean[][] traversedTiles;
    int finishTile = 0;

    /* State flags*/
    boolean stopped;
    boolean titleScreen;
    boolean winScreen = false;
    int newGame;

    /* Used to call sound effects */
    GameSounds sounds;

    /* This is the font used for the menus */
    Font font = new Font("Monospaced", Font.BOLD, 12);
	private ai.Point possible_next_point;

	/* Constructor initializes state flags etc.*/
    public Board(int boardSize) {


        initHighScores();
        sounds = new GameSounds();
        currScore = 0;
        stopped = false;
        gridSize = boardSize;
        newGame = 0;
        titleScreen = true;
        traversedTiles = new boolean[gridSize][gridSize];
    }

	public boolean[] getSurroundingArea(){
		boolean[] result = new boolean[4];
		ai.Point player_pos = player.getPosition();
		for(int i=0; i<4; ++i){
			Point possible_next_point = player.moveInDirection(i);
			// If pacman can go in chosen direction, possible_next_point will be different from current position
			result[i]=!possible_next_point.equals(player_pos);
		}
		return result;
	}

    /* Reads the high scores file and saves it */
    public void initHighScores() {
        File file = new File("highScores.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            highScore = sc.nextInt();
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /* Wipes the high scores file and sets flag to update it on screen */
    public void clearHighScores() {
        PrintWriter out;
        try {
            out = new PrintWriter("highScores.txt");
            out.println("0");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        highScore = 0;
        clearHighScores = true;
    }

    /* Reset occurs on a new game*/
    public void reset() {
        state = new boolean[gridSize - 1][gridSize - 1];


        /* Clear state and pellets arrays */
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                state[i][j] = true;
//                pellets[i][j] = true;
            }
        }

        //reset traversed
        for (int i = 0; i < traversedTiles.length; i++) {
            for (int j = 0; j < traversedTiles.length; j++) {
                traversedTiles[i][j] = false;
            }
        }
    }


    /* Function is called during drawing of the map.
       Whenever the a portion of the map is covered up with a barrier,
       the map and pellets arrays are updated accordingly to note
       that those are invalid locations to travel or put pellets
    */
    public void updateMap(int x, int y, int width, int height) {
        for (int i = x / gridSize; i < x / gridSize + width / gridSize; i++) {
            for (int j = y / gridSize; j < y / gridSize + height / gridSize; j++) {
                state[i - 1][j - 1] = false;

            }
        }
    }



    /*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
        manually.  Whenever I draw a wall, I call updateMap to invalidate those coordinates.  This way the pacman
        and ghosts know that they can't traverse this area */
    public void drawBoard(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 420, 420);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 20, 600);
        g.fillRect(0, 0, 600, 20);
        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);
        g.setColor(Color.BLUE);

        g.fillRect(40, 40, 60, 20);
        updateMap(40, 40, 60, 20);
        g.fillRect(120, 40, 60, 20);
        updateMap(120, 40, 60, 20);
        g.fillRect(200, 20, 20, 40);
        updateMap(200, 20, 20, 40);
        g.fillRect(240, 40, 60, 20);
        updateMap(240, 40, 60, 20);
        g.fillRect(320, 40, 60, 20);
        updateMap(320, 40, 60, 20);
        g.fillRect(40, 80, 60, 20);
        updateMap(40, 80, 60, 20);
        g.fillRect(160, 80, 100, 20);
        updateMap(160, 80, 100, 20);
        g.fillRect(200, 80, 20, 60);
        updateMap(200, 80, 20, 60);
        g.fillRect(320, 80, 60, 20);
        updateMap(320, 80, 60, 20);

        g.fillRect(20, 120, 80, 60);
        updateMap(20, 120, 80, 60);
        g.fillRect(320, 120, 80, 60);
        updateMap(320, 120, 80, 60);
        g.fillRect(20, 200, 80, 60);
        updateMap(20, 200, 80, 60);
        g.fillRect(320, 200, 80, 60);
        updateMap(320, 200, 80, 60);

        g.fillRect(160, 160, 40, 20);
        updateMap(160, 160, 40, 20);
        g.fillRect(220, 160, 40, 20);
        updateMap(220, 160, 40, 20);
        g.fillRect(160, 180, 20, 20);
        updateMap(160, 180, 20, 20);
        g.fillRect(160, 200, 100, 20);
        updateMap(160, 200, 100, 20);
        g.fillRect(240, 180, 20, 20);
        updateMap(240, 180, 20, 20);
        g.setColor(Color.BLUE);


        g.fillRect(120, 120, 60, 20);
        updateMap(120, 120, 60, 20);
        g.fillRect(120, 80, 20, 100);
        updateMap(120, 80, 20, 100);
        g.fillRect(280, 80, 20, 100);
        updateMap(280, 80, 20, 100);
        g.fillRect(240, 120, 60, 20);
        updateMap(240, 120, 60, 20);

        g.fillRect(280, 200, 20, 60);
        updateMap(280, 200, 20, 60);
        g.fillRect(120, 200, 20, 60);
        updateMap(120, 200, 20, 60);
        g.fillRect(160, 240, 100, 20);
        updateMap(160, 240, 100, 20);
        g.fillRect(200, 260, 20, 40);
        updateMap(200, 260, 20, 40);

        g.fillRect(120, 280, 60, 20);
        updateMap(120, 280, 60, 20);
        g.fillRect(240, 280, 60, 20);
        updateMap(240, 280, 60, 20);

        g.fillRect(40, 280, 60, 20);
        updateMap(40, 280, 60, 20);
        g.fillRect(80, 280, 20, 60);
        updateMap(80, 280, 20, 60);
        g.fillRect(320, 280, 60, 20);
        updateMap(320, 280, 60, 20);
        g.fillRect(320, 280, 20, 60);
        updateMap(320, 280, 20, 60);

        g.fillRect(20, 320, 40, 20);
        updateMap(20, 320, 40, 20);
        g.fillRect(360, 320, 40, 20);
        updateMap(360, 320, 40, 20);
        g.fillRect(160, 320, 100, 20);
        updateMap(160, 320, 100, 20);
        g.fillRect(200, 320, 20, 60);
        updateMap(200, 320, 20, 60);

        g.fillRect(40, 360, 140, 20);
        updateMap(40, 360, 140, 20);
        g.fillRect(240, 360, 140, 20);
        updateMap(240, 360, 140, 20);
        g.fillRect(280, 320, 20, 40);
        updateMap(280, 320, 20, 60);
        g.fillRect(120, 320, 20, 60);
        updateMap(120, 320, 20, 60);

        g.setColor(Color.GREEN);
        g.fillRect(gridSize + finishTile/ gridSize * gridSize,gridSize + finishTile % gridSize * gridSize  ,gridSize,gridSize);

//        for (int i = 0; i < state.length; i++) {
//            for (int j = 0; j < state.length; j++) {
//                System.out.print("[" + (!state[j][i] ? "#" : ".") + "]");
//            }
//            System.out.println();
//        }
    }

    private void drawPlanned(Graphics g){

        traversedTiles[(player.current.x)/gridSize][(player.current.y)/gridSize] = !player.finished;
        g.setColor(Color.ORANGE);
        // Drawing traversed path
        for(int i = 0; i < traversedTiles.length; i++)
            for(int j = 0; j < traversedTiles.length; j++)
                if(traversedTiles[i][j])
                    g.fillRect(i*gridSize, j*gridSize, gridSize, gridSize);

        g.setFont(font);
        for(int i = 0; i < plannedPath.size(); i++){
            g.setColor(Color.PINK);
            g.fillRect(plannedPath.get(i).x * gridSize + offset, plannedPath.get(i).y * gridSize + offset, gridSize, gridSize);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(i), plannedPath.get(i).x * gridSize + offset + offset/2, plannedPath.get(i).y * gridSize + offset+ offset/2);

        }

        g.setColor(Color.RED);
        if(nonNull(plannedPoint)) {
            System.out.println("IN BOARD " + plannedPoint.toString());

            g.fillRect(plannedPoint.x * gridSize + offset, plannedPoint.y * gridSize + offset, gridSize, gridSize);
        }
    }

    /* This is the main function that draws one entire frame of the game */
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

        /* If this is the win screen, draw the win screen and return */
        else if (winScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 600);
            g.drawImage(winScreenImage, 0, 0, Color.BLACK, null);
            newGame = 1;
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
            return;
        }

        /* If need to update the high scores, redraw the top menu bar */
        if (clearHighScores) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 18);
            g.setColor(Color.YELLOW);
            g.setFont(font);
            clearHighScores = false;
            g.drawString("Score: " + (currScore) + "\t High Score: " + highScore, 20, 10);
        }

        /* Game initialization */
        if (newGame == 1) {
            reset();
            player = new Player(200, 300);
            currScore = 0;
            drawBoard(g);
            /* Send the game map to player and all ghosts */
            player.updateState(state);
            /* Don't let the player go in the ghost box*/
            player.state[9][7] = false;

            /* Draw the top menu bar*/
            g.setColor(Color.YELLOW);
            g.setFont(font);
            g.drawString("Score: " + (currScore) + "\t High Score: " + highScore, 20, 10);
            newGame =0;
        }
        drawBoard(g);
        /* Delete the players and ghosts */
        g.setColor(Color.BLACK);
        g.fillRect(player.last.x, player.last.y, 20, 20);


        drawPlanned(g);


        /* Draw the pacman */
        if (player.frameCount < 5) {
            /* Draw mouth closed */
            g.drawImage(pacmanImage, player.current.x, player.current.y, Color.BLACK, null);
        } else {
            /* Draw mouth open in appropriate direction */
            if (player.frameCount >= 10)
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
}
