package main;

import ai.Point;

public class Player extends Mover {
	boolean finished = false;
	int collectedPellets = 0;
	/* Constructor places pacman in initial location and orientation */
	public Player(int x, int y) {
		super();
		finished = false;
		this.current = new Point(x,y);
		this.last = new Point(current);
		currDirection = 3;
		desiredPoint = current;
	}

	public void resetPlayer(int x, int y){
		finished = false;
		this.current = new Point(x,y);
		this.last = new Point(current);
		currDirection = 3;
		desiredPoint = current;
		frameCount = 0;
		stableFCount=0;
		collectedPellets = 0;
	}

	public Point getPosition() {
		return current;
	}

	public Point getGridPosition() {
		return toGridFormat(current);
	}

	public int getCollectedPellets() {
		return collectedPellets;
	}

	//	/* The move function moves the pacman for one frame in non demo mode */
//	public void move() {
//		++stableFCount;
//
//		last.x = current.x;
//		last.y = current.y;
//
//		/* Try to turn in the direction input by the user */
//		/*Can only turn if we're in center of a grid*/
//		if (current.x % gridSize == 0 && current.y % gridSize == 0
//				&& !current.equals(desiredPoint)
////				|| (desiredDirection + 2 % 4) == currDirection
//		) {
//			current = moveInDirection(currDirection);
////			inAction=false;
//		}
//
//		/* If we didn't move at all, set the stopped flag */
//		if (last.equals(current))
//			stopped = true;
//
//			/* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
//		else {
//			stopped = false;
//			frameCount++;
//		}
//	}
//
//	public Point moveInDirection(int currDirection) {
//		Point nest_p = new Point(current);
//		switch (currDirection) {
//			case 0:
//				if (isValidDest(nest_p.x, nest_p.y - increment))
//					nest_p.y -= increment;
//				break;
//			case 1:
//				if (isValidDest(nest_p.x + gridSize, nest_p.y))
//					nest_p.x += increment;
//				break;
//			case 2:
//				if (isValidDest(nest_p.x, nest_p.y + gridSize))
//					nest_p.y += increment;
//				break;
//			case 3:
//				if (isValidDest(nest_p.x - increment, nest_p.y))
//					nest_p.x -= increment;
//				break;
//		}
//		return nest_p;
//	}


	public Point toGridFormat(Point point) {
		return new Point(point.x/gridSize-1, point.y/gridSize-1);
	}
}