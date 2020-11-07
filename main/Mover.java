package main;

import ai.Point;
import ai.minmax.GhostLogic;
import ai.minmax.MinMaxLogic;

public class Mover {
	/* Framecount is used to count animation frames*/
	public int frameCount = 0;
	public long stableFCount=0;
	public MinMaxLogic logic;

	Point desiredPoint;
	int currDirection;

	Point last;
	Point current;

	boolean stopped = false;
	public boolean inAction=false;
	/* State contains the game map */
	boolean[][] state;

	/* gridSize is the size of one square in the game.
	   max is the height/width of the game.
	   increment is the speed at which the object moves,
	   1 increment per move() call */
	int gridSize = 20;
	int increment;

	/* Generic constructor */
	public Mover() {
		increment = 20;
		state = new boolean[gridSize -1][gridSize -1];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				state[i][j] = false;
			}
		}
	}

	public Point getPosition() {
		return current;
	}

	public Point getGridPosition() {
		return toGridFormat(current);
	}

	/* The move function moves the pacman for one frame in non demo mode */
	public void move() {
		++stableFCount;

		last.x = current.x;
		last.y = current.y;

		/* Try to turn in the direction input by the user */
		/*Can only turn if we're in center of a grid*/
		if (current.x % gridSize == 0 && current.y % gridSize == 0
				&& !current.equals(desiredPoint)
//				|| (desiredDirection + 2 % 4) == currDirection
		) {
			current = moveInDirection(currDirection);
		}

		/* If we didn't move at all, set the stopped flag */
		if (last.equals(current))
			stopped = true;

			/* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
		else {
			stopped = false;
			frameCount++;
		}
	}

	public Point moveInDirection(int currDirection) {
		Point nest_p = new Point(current);
		switch (currDirection) {
			case 0:
				if (isValidDest(nest_p.x, nest_p.y - increment))
					nest_p.y -= increment;
				break;
			case 1:
				if (isValidDest(nest_p.x + gridSize, nest_p.y))
					nest_p.x += increment;
				break;
			case 2:
				if (isValidDest(nest_p.x, nest_p.y + gridSize))
					nest_p.y += increment;
				break;
			case 3:
				if (isValidDest(nest_p.x - increment, nest_p.y))
					nest_p.x -= increment;
				break;
		}
		return nest_p;
	}


	public Point toGridFormat(Point point) {
		return new Point(point.x/gridSize-1, point.y/gridSize-1);
	}

	/* Updates the state information */
	public void updateState(boolean[][] state) {
		for (int i = 0; i < state.length; i++)
			for (int j = 0; j < state.length; j++)
				this.state[i][j] = state[i][j];
	}

	/* Determines if a set of coordinates is a valid destination.*/
	public boolean isValidDest(int x, int y) {
    /* The first statements check that the x and y are inbounds.  The last statement checks the map to
       see if it's a valid location */
		return (((x) % gridSize == 0) || ((y) % gridSize) == 0) && gridSize <= x && x < 400 && gridSize <= y && y < 400 && state[x / gridSize - 1][y / gridSize - 1];
	}

}
