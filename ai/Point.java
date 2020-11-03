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

	public int squaredDistance(EuclidVertex target){
		return (((Point)target).x-x)*(((Point)target).x-x)+(((Point)target).y-y)*(((Point)target).y-y);
	}

	@Override
	public String toString(){
		return "{x: "+x+", y: "+y+"}";
	}

	public int directionTo(Point point) {
		if(!isConnected(point)) {
			System.out.println("Points are not connected!");
		}
		if(x==point.x){
			if(y+1==point.y) return 2;
			return 0;
		}
		else{
			if(x+1==point.x) return 1;
			return 3;
		}
	}
}
