package ai.minmax;

import ai.Point;
import main.Board;
import main.Ghost;
import main.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PacmanLogic extends MinMaxLogic {
    ArrayList<Ghost> ghosts;
	int[][] coordinate_counters;
    public PacmanLogic(Player mover, Board board) {
        super(mover, board);
        ghosts = board.getGhosts();
		coordinate_counters = new int[board.state.length][board.state[0].length];
    }

	@Override
	public int makeMove() {
		Ghost closest = closestGhost();
		PacmanMinMaxTree choice_tree = new PacmanMinMaxTree(mapGraph,
				3,
				mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
				mapGraph.tiles[closest.getGridPosition().x][closest.getGridPosition().y],
				board,
				coordinate_counters);
		MinMaxVertex next_vertex = choice_tree.getBest();
		coordinate_counters[next_vertex.getLocation().point.x][next_vertex.getLocation().point.y]++;
        return mover.getGridPosition().directionTo(next_vertex.getLocation().point);
    }

    private ArrayList<Point> createPlannedPath(MinMaxVertex next_vertex) {
        ArrayList<Point> path = new ArrayList<>();
        MinMaxVertex vertex = next_vertex;
        while (vertex != null) {
            path.add(vertex.getLocation().point);
            if (!(vertex.getChildren() == null || vertex.getChildren().isEmpty())) {
                for (int i = 0; i < 2; ++i) {
                    double value = vertex.getValue();
                    for (MinMaxVertex v : vertex.getChildren()) {
                        if (v.getValue() == value) {
                            vertex = v;
                            break;
                        }
                    }
                }
            }
        }
        return path;
    }
}
