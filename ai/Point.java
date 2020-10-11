package ai;

public class Point implements Vertex {
	public int x;
	public int y;
	public Point(int x, int y){
		this.x=x;
		this.y=y;
	}

	public Point(Point position) {
		this.x=position.x;
		this.y=position.y;
	}

	public Point() {

	}

	@Override
	public boolean equals(Object player_pos) {
		return x==((Point)player_pos).x && y==((Point)player_pos).y;
	}

	@Override
	public boolean isConnected(Vertex v) {
		return ((Point)v).x==x || ((Point)v).y==y;
	}

	@Override
	public String toString(){
		return "{x: "+x+", y: "+y+"}";
	}
}
