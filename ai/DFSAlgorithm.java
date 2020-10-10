package ai;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFSAlgorithm implements Algorithm{
	LinkedList<Vertex> stack = new LinkedList();
	ArrayList<Vertex> used = new ArrayList<>();
	@Override
	public boolean isFinished() {
		return stack.isEmpty();
	}

	@Override
	public Vertex getNextVertex() {
		Vertex first = stack.pollFirst();
		used.add(first);
		return  first;
	}

	@Override
	public void updateVertex(Vertex v) {
		if(!used.contains(v) && !stack.contains(v)) stack.addFirst(v);
	}
}
