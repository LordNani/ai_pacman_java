/* Drew Schuster */

import ai.*;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.JApplet;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.*;
import java.text.NumberFormat;
import java.util.Scanner;

/* This class contains the entire game... most of the game logic is in the Board class but this
   creates the gui and captures mouse and keyboard input, as well as controls the game states */
public class Pacman extends JFrame implements MouseListener, KeyListener {

    /* These timers are used to kill title, game over, and victory screens after a set idle period (5 seconds)*/
    long titleTimer = -1;
    long timer = -1;
    int frameFreq = 15;
    int boardSize = 20;
    enum algoritmType {DFS, BFS, Greedy, AStar}
    int currentAlgoritm=0;
    /* framesPerMove defines how often pacman_logic should decide where to go */
    int framesPerMove = 1;

    /* Create a new board */
    Board b = new Board(boardSize);
    Sensor sensor;
    boolean isDFS = true;

    Logic logic ;
    /* This timer is used to do request new frames be drawn*/
    javax.swing.Timer frameTimer;

    /* This constructor creates the entire game essentially */
    public Pacman() {

        b.requestFocus();

        /* Create and set up window frame*/
        JFrame f = new JFrame();
        f.setSize(440, 460);

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
        stepFrame();
        /* Create a timer that calls stepFrame every 30 milliseconds */
        frameTimer = new javax.swing.Timer(frameFreq, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepFrame();
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
    public void stepFrame() {
        /* If we aren't on a special screen than the timers can be set to -1 to disable them */
        if (!b.titleScreen) {
            timer = -1;
            titleTimer = -1;
        }
        /* If this is the title screen, make sure to only stay on the title screen for 5 seconds.
           If after 5 seconds the user hasn't started a game, start up demo mode */
        if (b.titleScreen) {
            if (titleTimer == -1) {
                titleTimer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - titleTimer >= 1000) {
                b.titleScreen = false;
                titleTimer = -1;
            }
            repaint();
            return;
        }

        /* If we have a normal game state, move all pieces and update pellet status */
        if (b.newGame == 0) {
            if (b.player.stableFCount % framesPerMove == 0 && !b.player.inAction) {
//                System.out.println("Move " + b.player.stableFCount / framesPerMove);
                b.player.inAction = true;
                b.player.currDirection = logic.makeMove(b.getSurroundingArea());
                b.player.desiredPoint = b.player.moveInDirection(b.player.currDirection);
                b.player.inAction = false;
                b.plannedPoint = logic.plannedPoint;
                b.plannedPath = logic.convertedPath;
            }
            b.player.finished = sensor.isOnFinish(b.player.current.x, b.player.current.y, b.gridSize);

            if (b.player.finished) {
                File file = new File("logs.txt");

                try {
                    StringBuffer sb = new StringBuffer();
                    BufferedWriter bf = new BufferedWriter(new FileWriter(file, true));
                    String title = !isDFS ? "\n=======DFS TEST RESULT=======\n" : "\n=======BFS TEST RESULT=======\n";
                    sb.append(title);
                    sb.append("Average RAM: " + Logic.averageRAM + "MB\n");
                    sb.append("Steps: " + b.player.stableFCount / framesPerMove + "\n");
                    sb.append("Time: " + b.player.stableFCount * frameFreq + " milliseconds\n");
                    System.out.println(sb);
                    bf.append(sb);
                    bf.close();

                    Logic.averageRAM = 0;
                    Logic.ramCounter = 0;
                    b.player.stableFCount = 0;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                b.newGame = 1;
//                System.out.println("!!!WIN!!!");
            }
            b.player.move();


        } else {
            /* We either have a new game or the user has died, either way we have to reset the board */
            /*Temporarily stop advancing frames */
            frameTimer.stop();
            sensor = new Sensor(boardSize);
            b.finishTile = sensor.getFinishLocation();
            /* Advance a frame to display main state*/
            repaint(0, 0, 600, 600);
            //Change algorithm on every game restart
            Algorithm algorithm;
            switch(currentAlgoritm){
                case 0: algorithm = new DFSAlgorithm(b.player.getGridPosition()); break;
                case 1: algorithm = new BFSAlgorithm(b.player.getGridPosition()); break;
                case 2: algorithm = new GreedyAlgorithm(
                        new VertexPoint(b.player.getGridPosition()),
                        new VertexPoint(b.player.toGridFormat(sensor.getFinishLocation()))
                ); break;
                default: algorithm = new AStarAlgorithm(
                        new VertexPoint(b.player.getGridPosition()),
                        new VertexPoint(b.player.toGridFormat(sensor.getFinishLocation()))
                ); break;
            }
            currentAlgoritm=(currentAlgoritm+1)%4;
            logic = new Logic(boardSize-1, b.player.getGridPosition(),algorithm);
//            logic = isDFS ? new Logic(boardSize - 1, b.player.getGridPosition(),
//                    new DFSAlgorithm(b.player.getGridPosition())) : new Logic(boardSize - 1, b.player.getGridPosition(),
//                    new BFSAlgorithm(b.player.getGridPosition()));
//            ;
            isDFS = !isDFS;

            b.newGame = 0;
            frameTimer.start();
        }


        repaint();

    }

    /* Handles user key presses*/
    public void keyPressed(KeyEvent e) {
        /* Pressing a key in the title screen starts a game */
        if (b.titleScreen) {
            b.titleScreen = false;
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);

    }

    /* This function detects user clicks on the menu items on the bottom of the screen */
    public void mousePressed(MouseEvent e) {
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