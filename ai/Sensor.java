package ai;
import java.util.Random;

public class Sensor {
    int finishTile = 0;
    int[] possibleFinishLocations = {359,94,18,170,0};
    public Sensor(){
        Random rnd = new Random(System.currentTimeMillis());
        finishTile = possibleFinishLocations[rnd.nextInt( possibleFinishLocations.length)];
        System.out.println("Finish Tile is " + finishTile + ", or " + finishTile/19 + " " + finishTile % 19);
    }

    public boolean isOnFinish(int pacmanX, int pacmanY, int gridSize){
        System.out.println(pacmanY / gridSize * gridSize +  pacmanX/ gridSize);
        return pacmanX / gridSize + pacmanY % gridSize == finishTile;
    }
}
