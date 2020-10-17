package ai;

import java.util.*;

public class GreedyAlgorithm implements AwareAlgorithm {
	ArrayList<EuclidVertex> euclidVertices = new ArrayList<>();
	ArrayList<EuclidVertex> used = new ArrayList<>();
	EuclidVertex targetPosition;
	EuclidVertex position;
	public GreedyAlgorithm(EuclidVertex startPosition, EuclidVertex targetPosition){
		this.targetPosition=targetPosition;
		updateVertex(startPosition);
	}

	@Override
	public boolean isFinished() {
		return euclidVertices.isEmpty();
	}

	@Override
	public Vertex getNextVertex() {
		int closest_index=0;
		int min_distance = targetPosition.squaredDistance(euclidVertices.get(closest_index));
		int current_distance;
		for(int i=1; i<euclidVertices.size(); ++i){
			current_distance=targetPosition.squaredDistance(euclidVertices.get(i));
			if(current_distance<min_distance){
				min_distance=current_distance;
				closest_index=i;
			}
		}
		return euclidVertices.remove(closest_index);
	}

	@Override
	public void updateVertex(Vertex v) {
		if(!euclidVertices.contains((EuclidVertex)v) && !used.contains((EuclidVertex)v)) {
			used.add((EuclidVertex)v);
			euclidVertices.add((EuclidVertex)v);
		}
	}

	@Override
	public void updatePosition(EuclidVertex ev) {
		position=ev;
	}
}
