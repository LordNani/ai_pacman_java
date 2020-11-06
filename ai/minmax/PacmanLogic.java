package ai.minmax;

import ai.Point;
import main.Board;
import main.Player;

import java.util.ArrayList;

public class PacmanLogic extends MinMaxLogic{
	Player[] ghosts;
	ArrayList<Point> targets;

	public PacmanLogic(Player mover, Board board) {
		super(mover, board);
		ghosts = board.getGhosts();
		targets = board.getTargets();
	}

	@Override
	public int makeMove() {
		PacmanMinMaxTree choice_tree = new PacmanMinMaxTree(mapGraph,
				5,
				mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
				mapGraph.tiles[ghosts[0].getGridPosition().x][ghosts[0].getGridPosition().y]);
		MapTile next_tile = choice_tree.getBest();
		return mover.getGridPosition().directionTo(next_tile.point);
	}
}
