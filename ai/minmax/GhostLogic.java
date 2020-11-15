package ai.minmax;

import ai.Point;
import main.Board;
import main.Ghost;
import main.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class GhostLogic extends MinMaxLogic {
    Player pacman;
    LinkedList<MapTile> current_path = new LinkedList<>();

    public GhostLogic(Ghost mover, Board board) {
        super(mover, board);
        pacman = board.getPacman();
    }

    @Override
    public int makeMove() {
        int depth = 3;
        if (current_path.isEmpty()) {
            GhostMinMaxTree choice_tree = new GhostMinMaxTree(mapGraph,
                    depth,
                    mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
                    mapGraph.tiles[pacman.getGridPosition().x][pacman.getGridPosition().y],
                    board);
            current_path = choice_tree.getBest(1 + (int) (4 * Math.random()));
        }
        return mover.getGridPosition().directionTo(current_path.removeFirst().point);
//		int next = mapGraph.shortestWay(mover.getGridPosition(), pacman.getGridPosition()).get(0);
//		return next;
    }

    public LinkedList<MapTile> getPlannedPath() {
        return current_path;
    }
}
