package ai;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class Logic {
    private int boardSize;
    // planned_path contains planned path to the next vertex
    LinkedList<Integer> planned_path = new LinkedList<>();
    public ArrayList<Point> convertedPath = new ArrayList<>();
    // position.path - list of directions from initial position
    VertexPoint position;
    Algorithm a;
    public Point plannedPoint;

    public Logic(int boardSize, Point start_pos, Algorithm algorithm) {
        this.boardSize = boardSize;
        position = new VertexPoint(start_pos);
        a = algorithm;
    }

    public int makeMove(boolean[] surroundingArea) {

        calcRAMUsage();
        for (int i = 0; i < surroundingArea.length; ++i) {
            if (surroundingArea[i]) {
                a.updateVertex(moveInDirection(i));
            }
        }
        if (planned_path.isEmpty()) {
            if (!a.isFinished()) {
                VertexPoint next = (VertexPoint) a.getNextVertex();
                plannedPoint = new Point(next);
//				System.out.println("planned " + plannedPoint.toString());
                planned_path = createPathTo(next);
                convertedPath = convertToPoints(planned_path);
            } else {
                System.out.println("We have a problem, this shouldn't happen");
                return 0;
            }
        }
        int step = planned_path.pollFirst();
        position = moveInDirection(step);
        //if next step is opposite to the last step, just remove both steps from path
        if (position.path.size() > 2) {
            int last = position.path.pollLast();
            int prev = position.path.pollLast();
            if ((4 + last - prev) % 4 != 2) {
                position.path.addLast(prev);
                position.path.addLast(last);
            }
        }
        return step;
    }

    private ArrayList<Point> convertToPoints(LinkedList<Integer> planned_path) {
        ArrayList<Point> result = new ArrayList<>();
        Point currentPoint = new Point(position);
        for (int i = 0; i < planned_path.size(); i++) {
            switch (planned_path.get(i)) {
                case 0:
                    currentPoint.y -= 1;
                    break;
                case 1:
                    currentPoint.x += 1;
                    break;
                case 2:
                    currentPoint.y += 1;
                    break;
                case 3:
                    currentPoint.x -= 1;
                    break;
            }
            result.add(new Point(currentPoint));
        }

        return result;
    }

    private LinkedList<Integer> createPathTo(VertexPoint next) {
        // Algorithm can give you vertex which isn't near, so pacman needs to go there by himself.
        // The simplest way to create path  - return back step by step
        LinkedList<Integer> res_path = new LinkedList<>();
        // Need to optimize later
        int first_different_index = Math.min(position.path.size(), next.path.size());
        for (int i = 0; i < first_different_index; ++i) {
            if (position.path.get(i) != next.path.get(i)) {
                first_different_index = i;
                break;
            }
        }
        for (int j = first_different_index; j < position.path.size(); ++j)
            res_path.addFirst((position.path.get(j) + 2) % 4);
        for (int j = first_different_index; j < next.path.size(); ++j) res_path.addLast(next.path.get(j));
        return res_path;
    }

    public VertexPoint moveInDirection(int currDirection) {
        VertexPoint res_pos = new VertexPoint(position);
        switch (currDirection) {
            case 0:
                --res_pos.y;
                break;
            case 1:
                ++res_pos.x;
                break;
            case 2:
                ++res_pos.y;
                break;
            case 3:
                --res_pos.x;
                break;
        }
        res_pos.path.addLast(currDirection);
        return res_pos;
    }

    public static double averageRAM;
    public static int ramCounter;

    public static void calcRAMUsage() {
        final double totalMemory = Runtime.getRuntime().totalMemory();
        final double freeMemory = Runtime.getRuntime().freeMemory();
        if (ramCounter == 0) {
            averageRAM =  (totalMemory - freeMemory) / 1024 / 1024;

        } else {
            averageRAM = ((totalMemory - freeMemory) / 1024 / 1024 + averageRAM * ramCounter) / (ramCounter + 1);
            averageRAM = Math.round(averageRAM*10000.0)/10000.0;
        }
        ++ramCounter;

    }
}

class VertexPoint extends Point {
    LinkedList<Integer> path;

    public VertexPoint(int x, int y, LinkedList<Integer> path) {
        super(x, y);
        this.path = path;
    }

    public VertexPoint(VertexPoint vp) {
        super(vp.x, vp.y);
        this.path = ((LinkedList<Integer>) vp.path.clone());
    }

    public VertexPoint(Point p) {
        super(p.x, p.y);
        this.path = new LinkedList<>();
    }

    @Override
    public String toString() {
        return super.toString() + " path: " + path.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
