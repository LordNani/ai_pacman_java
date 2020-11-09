package ai.minmax;

import ai.Point;

import java.util.ArrayList;

public class PacmanMinMaxTree extends MinMaxTree {

    public PacmanMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location, ArrayList<Point> targets) {
        super(mapGraph, depth, location, enemy_location, targets);
    }

	@Override
	protected double getCollisionValue(MinMaxVertex agent_from_some_side) {
    	MinMaxVertex agent_from_other_side = agent_from_some_side.getFather();
    	MinMaxVertex pacman_agent = (agent_from_some_side.length%2==0) ? agent_from_some_side : agent_from_other_side;
		int collected_pellets = amountCollected(pacman_agent);
		if(collected_pellets==targets.size()) return 10000000;
		else return -10000000;
	}

	@Override
	protected double evaluateSituation(MinMaxVertex agent_from_some_side, MinMaxVertex agent_from_other_side) {
		double distance = (double) mapGraph.shortestWay(agent_from_some_side.getLocation(), agent_from_other_side.getLocation()).size();
		MinMaxVertex pacman_agent = (agent_from_some_side.length%2==0) ? agent_from_some_side : agent_from_other_side;
		int collected_pellets = amountCollected(pacman_agent);
		if(distance==0.0){
			if(collected_pellets==targets.size()) return 10000000;
			else return -10000000;
		}
		return distance+collected_pellets;
	}
}
