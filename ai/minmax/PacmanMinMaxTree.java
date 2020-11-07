package ai.minmax;

import ai.EuclidVertex;
import ai.Point;

import java.util.ArrayList;

public class PacmanMinMaxTree extends MinMaxTree {
	MapTile location;
	MapTile enemy_location;
	ArrayList<Point> targets;
	public PacmanMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location, ArrayList<Point> targets) {
		super(mapGraph, depth, location, enemy_location);
		this.location = location;
		this.enemy_location = enemy_location;
		this.targets = targets;
	}

	@Override
	protected double getCollisionValue() {
		return Double.MIN_VALUE+1;
	}

	@Override
	protected double evaluateSituation(MinMaxVertex agent, MinMaxVertex enemy) {
		double distance = (double) mapGraph.shortestWay(agent.getLocation(), enemy.getLocation()).size();
		int collected_pellets = 0;
		MinMaxVertex vertex = agent;
		while(vertex!=null){
			if(targets.contains(vertex.getLocation().point)){
				collected_pellets++;
			}
			vertex = vertex.getFather();
		}
		return distance+collected_pellets;
	}
}
