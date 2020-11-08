package ai;

public class GreedyAlgorithm extends AwareAlgorithm {
    public GreedyAlgorithm(EuclidVertex startPosition, EuclidVertex targetPosition) {
        super(startPosition, targetPosition);
    }

    @Override
    public void updateVertex(Vertex v) {
        if (!euclidVertices.contains((EuclidVertex) v) && !used.contains((EuclidVertex) v)) {
            used.add((EuclidVertex) v);
            euclidVertices.add((EuclidVertex) v);
        }
    }
}
