package ai.minmax;

import ai.Point;
import main.Board;

import java.util.ArrayList;

public class GhostMinMaxTree extends MinMaxTree {
    public GhostMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location, Board board) {
        super(mapGraph, depth, location, enemy_location, board);
    }

    @Override
    protected double getCollisionValue(MinMaxVertex agent_from_some_side) {
        MinMaxVertex agent_from_other_side = agent_from_some_side.getFather();
        MinMaxVertex pacman_agent = (agent_from_some_side.length%2==1) ? agent_from_some_side : agent_from_other_side;
        int collected_pellets = amountCollected(pacman_agent);
        if(collected_pellets==board.getPellets().size()) return -10000000;
        else return 10000000;
    }
    @Override
    protected double evaluateSituation(MinMaxVertex agent, MinMaxVertex enemy) {
        double distance = (double) mapGraph.shortestWay(agent.getLocation(), enemy.getLocation()).size();
        return -1 * distance;
    }
}
