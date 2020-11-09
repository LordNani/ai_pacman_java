package ai.minmax;

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
	protected double evaluateSituation(MinMaxVertex agent, MinMaxVertex enemy) {
		double distance = (double) mapGraph.shortestWay(agent.getLocation(), enemy.getLocation()).size();
		int collected_pellets = amountCollected((agent.length%2==0) ? agent : enemy);
		if(distance==0.0){
			if(collected_pellets==targets.size()) return 10000000;
			else return -10000000;
		}
		return distance+collected_pellets;
	}

	private int amountCollected(MinMaxVertex agent) {
		if(!(agent.length%2==0)){
			System.out.println("bug");
		}
		int collected_pellets = 0;
		MinMaxVertex vertex = agent;
		while(vertex!=null){
			if(targets.contains(vertex.getLocation().point)){
				collected_pellets++;
			}
			if(vertex.length<2) break;
			vertex = vertex.getFather().getFather();
		}
		return collected_pellets;
	}
}
