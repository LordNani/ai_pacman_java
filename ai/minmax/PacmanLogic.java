package ai.minmax;

import ai.Point;
import main.Board;
import main.Ghost;
import main.Player;

import java.util.ArrayList;

public class PacmanLogic extends MinMaxLogic{
	ArrayList<Ghost> ghosts;
	ArrayList<Point> targets;

	public PacmanLogic(Player mover, Board board) {
		super(mover, board);
		ghosts = board.getGhosts();
		targets = board.getTargets();
		targets = board.getPellets();
	}

	@Override
	public int makeMove() {
		PacmanMinMaxTree choice_tree = new PacmanMinMaxTree(mapGraph,
				10,
				mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
				mapGraph.tiles[ghosts.get(0).getGridPosition().x][ghosts.get(0).getGridPosition().y],
				targets);
		MapTile next_tile = choice_tree.getBest();
		return mover.getGridPosition().directionTo(next_tile.point);
	}
}
