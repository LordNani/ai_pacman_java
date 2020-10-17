package ai;

import java.util.LinkedList;

public class VertexPoint extends Point implements EuclidVertex{
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

    @Override
    public int squaredDistance(EuclidVertex target){
        return (((Point)target).x-x)*(((Point)target).x-x)+(((Point)target).y-y)*(((Point)target).y-y);
    }

    @Override
    public int getCostOfWayTo(EuclidVertex v) {
        return (path==null) ? 0 : path.size();
    }
}
