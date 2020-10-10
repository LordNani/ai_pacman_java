package ai;

import java.util.LinkedList;

public class Logic {
	private int boardSize;
	// planned_path contains planned path to the next vertex
	LinkedList<Integer> planned_path = new LinkedList<>();
	// list of directions from initial position
	LinkedList<Integer> current_path = new LinkedList<>();
	VertexPoint position;
	Algorithm a;
	public Logic(int boardSize, Point start_pos, Algorithm algorithm){
		this.boardSize = boardSize;
		position = new VertexPoint(start_pos);
		a=algorithm;
	}

	public int makeMove(boolean[] surroundingArea) {
		for(int i=0; i<surroundingArea.length; ++i){
			if(surroundingArea[i]){
				a.updateVertex(moveInDirection(i));
			}
		}
		if(planned_path.isEmpty()){
			if(!a.isFinished()){
				VertexPoint next = (VertexPoint)a.getNextVertex();
				planned_path = createPathTo(next);
				int step = planned_path.pollFirst();
				if(current_path.isEmpty()){
					current_path.addLast(step);
					return step;
				}
				//if next step is opposite to the last step, just remove last step from path
				if((step+2)%4 == (int)(current_path.peekFirst())){
					current_path.pollLast();
				}
				else current_path.addLast(step);
				return step;
			}
			else{
				System.out.println("We have a problem, this shouldn't happen");
				return 0;
			}
		}
		else{
			return planned_path.pollFirst();
		}
	}

	private LinkedList<Integer> createPathTo(VertexPoint next) {
		// Algorithm can give you vertex which isn't near, so pacman needs to go there by himself.
		// The simplest way to create path  - return back step by step
		LinkedList<Integer> res_path = new LinkedList<>();
		// Need to optimize later
		int first_different_index = 0;
		for(int i = 0; i<Math.min(current_path.size(), next.path.size()); ++i){
				if(!current_path.get(i).equals(next.path.get(i))){
					first_different_index=i;
					break;
				}
		}
		for(int j=current_path.size()-1; j>=first_different_index; --j) res_path.addFirst((current_path.get(j)+2)%4);
		for(int j=first_different_index; j<next.path.size();++j) res_path.addLast(next.path.get(j));
		return res_path;
	}

	public VertexPoint moveInDirection(int currDirection) {
		VertexPoint res_pos = new VertexPoint(position);
		switch (currDirection) {
			case 0:
				--res_pos.y;
				break;
			case 1:
				++res_pos.x;
				break;
			case 2:
				++res_pos.y;
				break;
			case 3:
				--res_pos.x;
				break;
		}
		res_pos.path.addLast(currDirection);
		return res_pos;
	}
}
class VertexPoint extends Point{
	LinkedList<Integer> path;
	public VertexPoint(int x, int y, LinkedList<Integer> path) {
		super(x, y);
		this.path = path;
	}
	public VertexPoint(VertexPoint vp) {
		super(vp.x, vp.y);
		this.path = ((LinkedList<Integer>)vp.path.clone());
	}
	public VertexPoint(Point p) {
		super(p.x, p.y);
		this.path = new LinkedList<>();
	}
}
