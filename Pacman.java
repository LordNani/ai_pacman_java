/* Drew Schuster */

import ai.DFSAlgorithm;
import ai.Logic;
import ai.Sensor;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.JApplet;
import java.awt.*;
import java.lang.*;

/* This class contains the entire game... most of the game logic is in the Board class but this
   creates the gui and captures mouse and keyboard input, as well as controls the game states */
public class Pacman extends JFrame implements MouseListener, KeyListener {

    /* These timers are used to kill title, game over, and victory screens after a set idle period (5 seconds)*/
    long titleTimer = -1;
    long timer = -1;
    int boardSize = 20;

	/* framesPerMove defines how often pacman_logic should decide where to go */
	int framesPerMove = 3;

    /* Create a new board */
    Board b = new Board(boardSize);
    Sensor sensor = new Sensor(boardSize);
    Logic logic = new Logic(boardSize-1, b.player.getGridPosition(),
			new DFSAlgorithm(b.player.getGridPosition()));
    /* This timer is used to do request new frames be drawn*/
    javax.swing.Timer frameTimer;


    /* This constructor creates the entire game essentially */
    public Pacman() {


        b.requestFocus();

        /* Create and set up window frame*/
        JFrame f = new JFrame();
        f.setSize(420, 460);

        /* Add the board to the frame */
        f.add(b, BorderLayout.CENTER);

        /*Set listeners for mouse actions and button clicks*/
        b.addMouseListener(this);
        b.addKeyListener(this);

        /* Make frame visible, disable resizing */
        f.setVisible(true);
        f.setResizable(false);

        /* Set the New flag to 1 because this is a new game */
        b.newGame = 1;

        /* Manually call the first frameStep to initialize the game. */
        stepFrame(true);

        /* Create a timer that calls stepFrame every 30 milliseconds */
        frameTimer = new javax.swing.Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepFrame(false);
            }
        });

        /* Start the timer */
        frameTimer.start();


        b.requestFocus();
    }

    /* This repaint function repaints only the parts of the screen that may have changed.
       Namely the area around every player ghost and the menu bars
    */
    public void repaint() {
        b.repaint(0, 0, 600, 20);
        b.repaint(0, 420, 600, 40);
        b.repaint(b.player.current.x - boardSize, b.player.current.y - boardSize, boardSize * 4, boardSize * 4);

    }

    /* Steps the screen forward one frame */
    public void stepFrame(boolean newGame) {
        /* If we aren't on a special screen than the timers can be set to -1 to disable them */
        if (!b.titleScreen && !b.winScreen) {
            timer = -1;
            titleTimer = -1;
        }


    /* newGame can either be specified by the newGame parameter in stepFrame function call or by the state
       of b.newGame.  Update newGame accordingly */
        newGame = newGame || (b.newGame != 0);

    /* If this is the title screen, make sure to only stay on the title screen for 5 seconds.
       If after 5 seconds the user hasn't started a game, start up demo mode */
        if (b.titleScreen) {
            if (titleTimer == -1) {
                titleTimer = System.currentTimeMillis();
            }

            long currTime = System.currentTimeMillis();
            if (currTime - titleTimer >= 1000) {
                b.titleScreen = false;
                titleTimer = -1;
            }
            b.repaint();
            return;
        }
 
    /* If this is the win screen or game over screen, make sure to only stay on the screen for 5 seconds.
       If after 5 seconds the user hasn't pressed a key, go to title screen */
        else if (b.winScreen ) {
            if (timer == -1) {
                timer = System.currentTimeMillis();
            }

            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 1000) {
                b.winScreen = false;
                b.titleScreen = true;
                timer = -1;
            }
            b.repaint();
            return;
        }


        /* If we have a normal game state, move all pieces and update pellet status */
        if (!newGame) {
			if(b.player.stableFCount % framesPerMove == 0 && !b.player.inAction){
				System.out.println("Move "+ b.player.stableFCount / framesPerMove);
				b.player.inAction=true;
				b.player.currDirection = logic.makeMove(b.getSurroundingArea());
				b.player.desiredPoint = b.player.moveInDirection(b.player.currDirection);
				b.player.inAction=false;
			}

      /* The pacman player has two functions, demoMove if we're in demo mode and move if we're in
         user playable mode.  Call the appropriate one here */
            b.player.move();
            /* Also move the ghosts, and update the pellet states */
            if(b.player.finished){
                b.titleScreen = true;
                b.newGame = 1;
                System.out.println("!!!WIN!!!");
            }
            b.player.finished = sensor.isOnFinish(b.player.current.x, b.player.current.y, b.gridSize);

        }

        /* We either have a new game or the user has died, either way we have to reset the board */
        if (newGame) {
            /*Temporarily stop advancing frames */
            frameTimer.stop();

            /* Move all game elements back to starting positions and orientations */
            b.player.current.x = 200;
            b.player.current.y = 300;
            b.player.currDirection=3;
            b.player.desiredPoint = b.player.moveInDirection(3);

            b.player.finished = false;
            b.finishTile = sensor.getFinishLocation();
            /* Advance a frame to display main state*/
            b.repaint(0, 0, 600, 600);
            /*Start advancing frames once again*/
            b.stopped = false;
            frameTimer.start();
        }
        /* Otherwise we're in a normal state, advance one frame*/
        else {
            repaint();
        }
    }

    /* Handles user key presses*/
    public void keyPressed(KeyEvent e) {
        /* Pressing a key in the title screen starts a game */
        if (b.titleScreen) {
            b.titleScreen = false;
            return;
        }
        /* Pressing a key in the win screen or game over screen goes to the title screen */
        else if (b.winScreen ) {
            b.titleScreen = true;
            b.winScreen = false;
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        /* Pressing a key during a demo kills the demo mode and starts a new game */


        /* Otherwise, key presses control the player! */
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_LEFT:
//                b.player.desiredDirection = 3;
//                break;
//            case KeyEvent.VK_RIGHT:
//                b.player.desiredDirection = 1;
//                break;
//            case KeyEvent.VK_UP:
//                b.player.desiredDirection = 0;
//                break;
//            case KeyEvent.VK_DOWN:
//                b.player.desiredDirection = 2;
//                break;
//        }

        repaint();
    }

    /* This function detects user clicks on the menu items on the bottom of the screen */
    public void mousePressed(MouseEvent e) {
        if (b.titleScreen || b.winScreen) {
            /* If we aren't in the game where a menu is showing, ignore clicks */
            return;
        }

        /* Get coordinates of click */
        int x = e.getX();
        int y = e.getY();
        if (400 <= y && y <= 460) {
            if (100 <= x && x <= 150) {
                /* New game has been clicked */
                b.newGame = 1;
            } else if (180 <= x && x <= 300) {
                /* Clear high scores has been clicked */
                b.clearHighScores();
            } else if (350 <= x && x <= 420) {
                /* Exit has been clicked */
                System.exit(0);
            }
        }
    }


    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }



    /* Main function simply creates a new pacman instance*/
    public static void main(String[] args) {
       new Pacman().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
