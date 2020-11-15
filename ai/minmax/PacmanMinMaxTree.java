package ai.minmax;

import ai.Point;
import main.Board;

import java.util.ArrayList;
import java.util.HashMap;

public class PacmanMinMaxTree extends MinMaxTree {
	int[][] coordinate_counters;

    public PacmanMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location, Board board, int[][] coordinate_counters) {
        super(mapGraph, depth, location, enemy_location, board);
        this.coordinate_counters = coordinate_counters;
    }

	@Override
	protected double getCollisionValue(MinMaxVertex agent_from_some_side) {
    	MinMaxVertex agent_from_other_side = agent_from_some_side.getFather();
    	MinMaxVertex pacman_agent = (agent_from_some_side.length%2==0) ? agent_from_some_side : agent_from_other_side;
		int collected_pellets = amountCollected(pacman_agent);
		if(collected_pellets==board.getPellets().size()) return 10000000;
		else return -10000000;
	}

	@Override
	protected double evaluateSituation(MinMaxVertex agent_from_some_side, MinMaxVertex agent_from_other_side) {
		double distance = (double) mapGraph.shortestWay(agent_from_some_side.getLocation(), agent_from_other_side.getLocation()).size();
		MinMaxVertex pacman_agent = (agent_from_some_side.length%2==0) ? agent_from_some_side : agent_from_other_side;
		double collected_pellets = amountCollected(pacman_agent);
		if(collected_pellets==(double)(board.getPellets().size())){
			return 10000000-last_step_when_collected_pellet(pacman_agent);
		}
		if(collected_pellets==0.0){
			collected_pellets = 1.0/(super.distanceToClosestPellet(pacman_agent.getLocation())+0.000001);
		}
		if(distance==0.0) return -10000000;
		double amount_of_visits = sumVisited(pacman_agent)*0.01;
		return distance*(collected_pellets+0.001)-(amount_of_visits/depth);
	}

	private int last_step_when_collected_pellet(MinMaxVertex pacman_agent) {
		MinMaxVertex vertex = pacman_agent;
		ArrayList<Point> targets = board.getPellets();
		int i=0;
			while(vertex!=null){
			if(targets.contains(vertex.getLocation().point)){
				break;
			}
			if(vertex.length<2) break;
			i+=2;
			vertex = vertex.getFather().getFather();
		}
		return pacman_agent.length-i;
	}

	private double sumVisited(MinMaxVertex pacman_agent) {

		int visited_sum = 0;
		MinMaxVertex vertex = pacman_agent;
		while(vertex!=null){
			visited_sum+=coordinate_counters[vertex.getLocation().point.x][vertex.getLocation().point.y];
			if(vertex.length<2) break;
			vertex = vertex.getFather().getFather();
		}
		return (double)visited_sum;
	}
}
