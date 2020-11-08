package ai.minmax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public abstract class MinMaxTree {
	MinMaxVertex root;
	MapGraph mapGraph;
	int depth;
	int allies_amount;
	int enemies_amount;

	public MinMaxTree(MapGraph mapGraph, int depth, MapTile location, MapTile enemy_location){
		allies_amount=1;
		enemies_amount=1;
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

	public MinMaxTree(MapGraph mapGraph, int depth, ArrayList<MapTile> allies, ArrayList<MapTile> enemies){
		allies_amount=allies.size();
		enemies_amount=enemies.size();
		this.mapGraph = mapGraph;
		this.depth = depth;
		ArrayList<MinMaxVertex>[] ally_moves = new ArrayList[allies.size()];
		ArrayList<MinMaxVertex>[] enemy_moves = new ArrayList[enemies.size()];
		root = new MinMaxVertex(true, null, new ArrayList<>(), allies.get(0));
		ally_moves[0] = new ArrayList<>();
		ally_moves[0].add(root);
		for(int i=1; i<allies.size(); ++i){
			ally_moves[i] = new ArrayList<>();
			ally_moves[i].add(new MinMaxVertex(true, ally_moves[i-1].get(0), new ArrayList<>(), allies.get(i)));
			ally_moves[i-1].get(0).getChildren().add(ally_moves[i].get(0));
			if(i+1==allies.size()) ally_moves[i].get(0).setMax(false);
		}
		MinMaxVertex enemy_root = new MinMaxVertex(false, ally_moves[ally_moves.length-1].get(0), new ArrayList<>(), enemies.get(0));
//		root.getChildren().add(enemy_root);
		enemy_moves[0] = new ArrayList<>();
		enemy_moves[0].add(enemy_root);
		for(int i=1; i<enemies.size(); ++i){
			enemy_moves[i] = new ArrayList<>();
			enemy_moves[i].add(new MinMaxVertex(false, enemy_moves[i-1].get(0), new ArrayList<>(), enemies.get(i)));
			enemy_moves[i-1].get(0).getChildren().add(enemy_moves[i].get(0));
			if(i+1==enemies.size()) enemy_moves[i].get(0).setMax(true);
		}

		int generation_gap = ally_moves.length+enemy_moves.length;

		for(int i=0; i<depth; ++i){
			ArrayList<MinMaxVertex> last_level = enemy_moves[enemy_moves.length-1];
			for(int j=0; j<ally_moves.length; ++j){
				ArrayList<MinMaxVertex> new_level = new ArrayList<>();
				for(MinMaxVertex move : last_level){
					if(!move.isEvaluated()) new_level.addAll(addChildrenByAncestor(move, ((j+1)!=ally_moves.length), generation_gap));
				}
				last_level = new_level;
			}
			for(int j=0; j<enemy_moves.length; ++j){
				ArrayList<MinMaxVertex> new_level = new ArrayList<>();
				for(MinMaxVertex move : last_level){
					if(!move.isEvaluated()) new_level.addAll(addChildrenByAncestor(move, ((j+1)==enemy_moves.length), generation_gap));
				}
				last_level = new_level;
			}
		}
	}

	private ArrayList<MinMaxVertex> addChildrenByAncestor(MinMaxVertex father, boolean max, int generation_gap) {
		ArrayList<MinMaxVertex> result = new ArrayList<>(4);
		MinMaxVertex ancestor = father;
		for(int i=0; i<generation_gap-1; ++i){
			ancestor=father.getFather();
		}
		MinMaxVertex previous_ancestor = null;
		MapTile previous_ancestor_location = null;
		if(ancestor.calculateLength() > generation_gap){
			previous_ancestor = ancestor;
			for(int i=0; i<generation_gap; ++i){
				previous_ancestor=ancestor.getFather();
			}
			previous_ancestor_location = previous_ancestor.getLocation();
		}
		for(MapTile ancestor_neighbour : ancestor.getLocation().neighbours){
			if(ancestor_neighbour!=null && !ancestor_neighbour.equals(previous_ancestor_location)){
				MinMaxVertex new_vertex = new MinMaxVertex(max, father, null, ancestor_neighbour);
				result.add(new_vertex);
				if(ancestor_neighbour.equals(father.getLocation())) new_vertex.setValue(getCollisionValue(father));
			}
		}
		father.setChildren(result);
		return result;
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
		MinMaxVertex last_enemy_vertex = root;
		for(int i = 0; i<allies_amount+enemies_amount-1; ++i){
			last_enemy_vertex = last_enemy_vertex.getChildren().get(0);
		}
		return getMaxVertex(last_enemy_vertex.getChildren());
	}

	LinkedList<MapTile> getBest(int amount_of_steps){
		amount_of_steps = Math.min(amount_of_steps, depth);
		LinkedList<MapTile> path = new LinkedList<>();
		MinMaxVertex vertex = getBest();
		for(int i=0; i<amount_of_steps; ++i){
			MapTile next = vertex.getLocation();
			path.add(next);
			if(vertex.getChildren()==null) return path;
			for(int j=0; j<allies_amount-1; ++j){
				vertex = getMaxVertex(vertex.getChildren());
			}
			for(int j=0; j<enemies_amount; ++j){
				if(vertex.getChildren()==null){
					System.out.println("Empty list");
				}
				vertex = getMinVertex(vertex.getChildren());
			}
			vertex = getMaxVertex(vertex.getChildren());
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
