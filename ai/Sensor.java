package ai;
import main.Ghost;
import main.Player;

import java.util.ArrayList;
import java.util.Random;

public class Sensor {
    Point finishPoint = new Point();
    Point[] possibleFinishLocations = {new Point(0,0),new Point(160,160),new Point(300,0)};
    public Sensor(int gridSize){
        Random rnd = new Random(System.currentTimeMillis());
        finishPoint = possibleFinishLocations[rnd.nextInt( possibleFinishLocations.length)];
        System.out.println();
        System.out.println("Finish Tile is " + finishPoint.toString());
    }

    public Point getFinishLocation() {return finishPoint;};

    public boolean isOnFinish(int x, int y, int gs){
        Point pacmanPoint = new Point(x-gs,y-gs);
        return pacmanPoint.equals(finishPoint);
    }


    /*
    1 - Won
    2 - Lost
    0 - continue playing
     */
    public int checkWinOrLooseCondition(ArrayList<Ghost> ghosts, Player player, int totalPoints){
        for(Ghost ghost: ghosts){
            System.out.println("ghost pos: " + ghost.getGridPosition() );

            if(ghost.getGridPosition().equals(player.getGridPosition()))
                return 2;
        }
        System.out.println("player pos: " + player.getGridPosition() );
        return player.getCollectedPellets() >= totalPoints? 1: 0;
    }
}
