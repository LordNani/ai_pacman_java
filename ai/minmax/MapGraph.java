package ai.minmax;

import ai.Point;

import java.util.HashMap;

public class MapGraph {
	public MapTile[][] tiles;

	public MapGraph(boolean[][] state){
		tiles = new MapTile[state.length][state[0].length];
			for (int i = 0; i < state.length; ++i) {
				for (int j = 0; j < state[0].length; ++j) {
					if (state[i][j]) {
						Point p = new Point(i, j);
						MapTile tile = new MapTile(p);
						if (i != 0 && state[i-1][j]) {
							MapTile upper_tile = tiles[i - 1][j];
							tile.neighbours[0] = upper_tile;
							upper_tile.neighbours[2] = tile;
						}
						if (j != 0 && state[i][j-1]) {
							MapTile left_tile = tiles[i][j - 1];
							tile.neighbours[3] = tile;
							left_tile.neighbours[1] = tile;
						}
						tiles[i][j] = tile;
					}
				}
			}
	}
}

class MapTile {
	Point point;
	MapTile[] neighbours;

	public MapTile(Point p) {
		this.point = p;
		neighbours = new MapTile[4];
	}
}