package ai;
import java.util.Random;

public class Sensor {
    int finishTile = 0;
    int[] possibleFinishLocations = {170};
    public Sensor(){
        Random rnd = new Random(System.currentTimeMillis());
        finishTile = possibleFinishLocations[rnd.nextInt( possibleFinishLocations.length)];
        System.out.println("Finish Tile is " + finishTile + ", or " + finishTile/19 + " " + finishTile % 19);
    }

    public int getFinishLocation() {return finishTile;};

    public boolean isOnFinish(int pacmanX, int pacmanY, int gridSize){

        return calculatePacmanTile(pacmanX,pacmanY, gridSize) == finishTile;
    }

    private int calculatePacmanTile(int x,int y,int gridSize){
        int result = (x/gridSize * gridSize - gridSize) +  (y / gridSize - 1);
        System.out.println("CURRENT PACMAN TILE IS: " + result);
        return result;
    }
}
