package ai;

public interface Algorithm {
	boolean isFinished();
	Vertex getNextVertex();
	void updateVertex(Vertex v);
}
