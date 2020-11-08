package main;

import ai.Point;

import java.awt.*;

public class Ghost extends Mover {
    Image img;

    public Ghost(int x, int y, Image ghostImg) {
        super();
        this.current = new Point(x, y);
        this.last = new Point(current);
        currDirection = 3;
        desiredPoint = current;
        img = ghostImg;
    }

    public void resetGhost(int x, int y) {
        this.current = new Point(x, y);
        this.last = new Point(current);
        currDirection = 3;
        desiredPoint = current;
        frameCount = 0;
        stableFCount = 0;
    }
}