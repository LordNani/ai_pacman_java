package ai.minmax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class MinMaxTree {
	MinMaxVertex root;
	MapGraph mapGraph;
	int depth;
	public MinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location){
		this.mapGraph = mapGraph;
		this.depth = depth;
		ArrayList<MinMaxVertex> moves = new ArrayList<>();
		ArrayList<MinMaxVertex> enemy_moves = new ArrayList<>();
		root = new MinMaxVertex(false, null, new ArrayList<>(), location);
		MinMaxVertex enemy_root = new MinMaxVertex(true, root, null, enemy_location);
		root.getChildren().add(enemy_root);
		enemy_moves.add(enemy_root);

		for(int i=0; i<depth; ++i){
			moves.clear();
			for(MinMaxVertex enemy_move : enemy_moves){
				moves.addAll(addChildrenByFather(enemy_move, false));
			}
			enemy_moves.clear();
			for(MinMaxVertex move : moves){
				enemy_moves.addAll(addChildrenByFather(move, true));
			}
		}
	}

	private ArrayList<MinMaxVertex> addChildrenByFather(MinMaxVertex enemy_move, boolean max) {
		ArrayList<MinMaxVertex> result = new ArrayList<>(4);
		MapTile location_of_previous = (enemy_move.getFather().getFather()!=null && enemy_move.getFather().getFather().getFather()!=null) ?
				enemy_move.getFather().getFather().getFather().getLocation() : null;
		for(MapTile neighbour_of_father : enemy_move.getFather().getLocation().neighbours){
			if(neighbour_of_father!=null && !neighbour_of_father.equals(location_of_previous)) result.add(new MinMaxVertex(max, enemy_move, null, neighbour_of_father));
		}
		enemy_move.setChildren(result);
		return result;
	}
}
