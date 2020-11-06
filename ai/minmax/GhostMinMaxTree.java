package ai.minmax;

import ai.EuclidVertex;

public class GhostMinMaxTree extends MinMaxTree {
	MapTile location;
	MapTile enemy_location;
	public GhostMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location) {
		super(mapGraph, depth, location, enemy_location);
		this.location = location;
		this.enemy_location = location;
	}

	@Override
	protected double getCollisionValue() {
		return Double.MAX_VALUE-1;
	}

	@Override
	protected double evaluateSituation() {
		return -1.0*location.point.squaredDistance((EuclidVertex) enemy_location.point);
	}
}
