import ai.Point;

public class Player extends Mover {
	/* Direction is used in demoMode, currDirection and desiredDirection are used in non demoMode*/
	Point desiredPoint;
	int currDirection;

	/* Keeps track of pellets eaten to determine end of game */
	int pelletsEaten;

	/* Last location */
	Point last;

	/* Current location */
	Point current;

	/* Which pellet the pacman is on top of */
	int pelletX;
	int pelletY;


	/* Stopped is set when the pacman is not moving or has been killed */
	boolean stopped = false;
	boolean finished = false;

	public boolean inAction=false;

	/* Constructor places pacman in initial location and orientation */
	public Player(int x, int y) {
		pelletsEaten = 0;
		pelletX = x / gridSize - 1;
		pelletY = y / gridSize - 1;
		this.current = new Point(x,y);
		this.last = new Point(current);
		finished = false;
		currDirection = 3;
		desiredPoint = current;
	}

	public Point getPosition() {
		return current;
	}

	public Point getGridPosition() {
		return new Point(current.x/gridSize-1, current.y/gridSize-1);
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
//			inAction=false;
		}
		/* If we haven't moved, then move in the direction the pacman was headed anyway */
//        if (lastX == x && lastY == y) {
//            moveInDirection(currDirection);
//        }
//        else {
//			/* If we did change direction, update currDirection to reflect that */
//            currDirection = desiredDirection;
//        }

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

	/* Update what pellet the pacman is on top of */
	public void updatePellet() {
		if (current.x % gridSize == 0 && current.y % gridSize == 0) {
			pelletX = current.x / gridSize - 1;
			pelletY = current.y / gridSize - 1;
		}
	}
}