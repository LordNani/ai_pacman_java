public class Point {
	public int x;
	public int y;
	public Point(int x, int y){
		this.x=x;
		this.y=y;
	}

	public boolean equalsTo(Point player_pos) {
		return x==player_pos.x && y==player_pos.y;
	}
}
