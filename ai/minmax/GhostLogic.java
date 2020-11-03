package ai.minmax;

import main.Board;
import main.Player;

public class GhostLogic extends MinMaxLogic {
	Player pacman;

	public GhostLogic(Player mover, Board board) {
		super(mover, board);
		pacman = board.getPacman();
	}

	@Override
	public int makeMove() {
		return 0;
	}
}
