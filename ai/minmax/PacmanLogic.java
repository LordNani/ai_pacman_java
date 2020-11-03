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
		return 0;
	}
}
