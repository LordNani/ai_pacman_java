package ai.minmax;

import ai.Point;
import main.Board;
import main.Ghost;
import main.Player;

import java.util.ArrayList;

public class PacmanLogic extends MinMaxLogic{
	ArrayList<Ghost> ghosts;
	ArrayList<Point> planned_path;

	public PacmanLogic(Player mover, Board board) {
		super(mover, board);
		ghosts = board.getGhosts();
	}

	@Override
	public int makeMove() {
		Ghost closest = closestGhost();
		PacmanMinMaxTree choice_tree = new PacmanMinMaxTree(mapGraph,
				10,
				mapGraph.tiles[mover.getGridPosition().x][mover.getGridPosition().y],
				mapGraph.tiles[closest.getGridPosition().x][closest.getGridPosition().y],
				board.getPellets());
		MinMaxVertex next_vertex = choice_tree.getBest();
//		planned_path = createPlannedPath(next_vertex);
		return mover.getGridPosition().directionTo(next_vertex.getLocation().point);
	}

	private ArrayList<Point> createPlannedPath(MinMaxVertex next_vertex) {
		ArrayList<Point> path = new ArrayList<>();
		MinMaxVertex vertex = next_vertex;
		while(vertex!=null){
			path.add(vertex.getLocation().point);
			if(!(vertex.getChildren()==null || vertex.getChildren().isEmpty())){
				for(int i=0; i<2; ++i){
					double value = vertex.getValue();
					for(MinMaxVertex v : vertex.getChildren()){
						if(v.getValue()==value){
							vertex = v;
							break;
						}
					}
				}
			}
		}
		return path;
	}

	@Override
	public ArrayList<Point> getPlannedPath() {
		return  planned_path;
	}
}
