package ai.minmax;

public class GhostMinMaxTree extends MinMaxTree {
    MapTile location;
    MapTile enemy_location;

    public GhostMinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location) {
        super(mapGraph, depth, location, enemy_location);
        this.location = location;
        this.enemy_location = enemy_location;
    }

    @Override
    protected double getCollisionValue(MinMaxVertex agent) {
        return 10000000;
    }

    @Override
    protected double evaluateSituation(MinMaxVertex agent, MinMaxVertex enemy) {
        double distance = (double) mapGraph.shortestWay(agent.getLocation(), enemy.getLocation()).size();
        return -1 * distance;
    }
}
