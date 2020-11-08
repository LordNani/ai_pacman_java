package ai;

import java.util.ArrayList;
import java.util.LinkedList;

public class BFSAlgorithm implements Algorithm {
    LinkedList<Vertex> stack = new LinkedList();
    ArrayList<Vertex> used = new ArrayList<>();

    public BFSAlgorithm(Vertex v) {
        used.add(v);
    }

    @Override
    public boolean isFinished() {
        return stack.isEmpty();
    }

    @Override
    public Vertex getNextVertex() {
        Vertex first = stack.pollLast();
        used.add(first);
        return first;
    }

    @Override
    public void updateVertex(Vertex v) {
        if (!used.contains(v) && !stack.contains(v)) stack.addFirst(v);
    }
}
