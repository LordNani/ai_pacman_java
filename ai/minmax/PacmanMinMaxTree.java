package ai.minmax;

import ai.EuclidVertex;

public class PacmanMinMaxTree extends MinMaxTree {
	MapTile location;
	MapTile enemy_location;
	public PacmanMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location) {
		super(mapGraph, depth, location, enemy_location);
		this.location = location;
		this.enemy_location = location;
	}

	@Override
	protected double getCollisionValue() {
		return Double.MIN_VALUE+1;
	}

	@Override
	protected double evaluateSituation() {
		return (double) location.point.squaredDistance((EuclidVertex) enemy_location.point);
	}
}
