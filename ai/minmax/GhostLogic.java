package ai.minmax;

import main.Board;
import main.Ghost;
import main.Player;

public class GhostLogic extends MinMaxLogic {
	Player pacman;

	public GhostLogic(Ghost mover, Board board) {
		super(mover, board);
		pacman = board.getPacman();
	}

	@Override
	public int makeMove() {
		GhostMinMaxTree choice_tree = new GhostMinMaxTree(mapGraph,
				5,
				mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
				mapGraph.tiles[pacman.getGridPosition().x][pacman.getGridPosition().y]);
		MapTile next_tile = choice_tree.getBest();
		return mover.getGridPosition().directionTo(next_tile.point);
//		int next = mapGraph.shortestWay(mover.getGridPosition(), pacman.getGridPosition()).get(0);
//		return next;
	}
}
