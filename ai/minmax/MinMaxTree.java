package ai.minmax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public abstract class MinMaxTree {
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
				if(!enemy_move.isEvaluated()) moves.addAll(addChildrenByFather(enemy_move, false));
			}
			enemy_moves.clear();
			for(MinMaxVertex move : moves){
				if(!move.isEvaluated()) enemy_moves.addAll(addChildrenByFather(move, true));
			}
		}
	}

	private ArrayList<MinMaxVertex> addChildrenByFather(MinMaxVertex enemy_move, boolean max) {
		ArrayList<MinMaxVertex> result = new ArrayList<>(4);
		MapTile location_of_previous = (enemy_move.getFather().getFather()!=null && enemy_move.getFather().getFather().getFather()!=null) ?
				enemy_move.getFather().getFather().getFather().getLocation() : null;
		for(MapTile neighbour_of_father : enemy_move.getFather().getLocation().neighbours){
			if(neighbour_of_father!=null && !neighbour_of_father.equals(location_of_previous)) {
				MinMaxVertex new_vertex = new MinMaxVertex(max, enemy_move, null, neighbour_of_father);
				result.add(new_vertex);
				if(neighbour_of_father.equals(enemy_move.getLocation())) new_vertex.setValue(getCollisionValue(enemy_move));
			}
		}
		enemy_move.setChildren(result);
		return result;
	}

	MinMaxVertex getBest(){
		evaluate(root);
		return getMaxVertex(root.getChildren().get(0).getChildren());
	}

	LinkedList<MapTile> getBest(int amount_of_steps){
		amount_of_steps = Math.min(amount_of_steps, depth);
		LinkedList<MapTile> path = new LinkedList<>();
		evaluate(root);
		MinMaxVertex vertex = getMaxVertex(root.getChildren().get(0).getChildren());
		for(int i=0; i<amount_of_steps; ++i){
			MapTile next = vertex.getLocation();
			path.add(next);
			vertex = getMaxVertex(getMinVertex(vertex.getChildren()).getChildren());
		}
		return path;
	}

	private double evaluate(MinMaxVertex vertex) {
		if(vertex==null){
			System.out.println("Bug here");
		}

		if(vertex.isEvaluated()) return vertex.getValue();
		if(vertex.getChildren()==null || vertex.getChildren().isEmpty()) return evaluateSituation(vertex.getFather(), vertex);
		for(MinMaxVertex child : vertex.getChildren())
			child.setValue(evaluate(child));
		if(vertex.isMax()) return getMaxVertex(vertex.getChildren()).getValue();
		else return getMinVertex(vertex.getChildren()).getValue();
	}

	private MinMaxVertex getMinVertex(ArrayList<MinMaxVertex> children) {
		MinMaxVertex best_vertex = null;
		double min = 10000001;
		for(MinMaxVertex v : children){
			if(min > v.getValue()){
				min = v.getValue();
				best_vertex = v;
			}
		}
		return best_vertex;
	}

	private MinMaxVertex getMaxVertex(ArrayList<MinMaxVertex> children) {
		MinMaxVertex best_vertex = null;
		double max = -10000001;
		for(MinMaxVertex v : children){
			if(max<v.getValue()){
				max = v.getValue();
				best_vertex = v;
			}
		}
		if(best_vertex==null){
			System.out.println("bug");
		}
		return best_vertex;
	}

	protected abstract double getCollisionValue(MinMaxVertex agent);

	protected abstract double evaluateSituation(MinMaxVertex agent, MinMaxVertex enemy);

}
