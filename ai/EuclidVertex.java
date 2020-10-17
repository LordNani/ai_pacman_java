package ai;

public interface EuclidVertex extends Vertex{
	int squaredDistance(EuclidVertex v);
	int getCostOfWayTo(EuclidVertex v);
}
