package ai;

public class Logic {
	private int boardSize;
	public Logic(int boardSize){
		this.boardSize = boardSize;
	}

	public int makeMove(boolean[] surroundingArea) {
		for(int i=0; i<surroundingArea.length; ++i){
			if(surroundingArea[i]) return i;
		}
		return 3;
	}
}
