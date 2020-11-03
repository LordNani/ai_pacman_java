package ai.minmax;

import java.util.ArrayList;

public class MinMaxVertex implements Cloneable{
	private boolean max;
	private MinMaxVertex father;
	private ArrayList<MinMaxVertex> children;
	private double value;
	private MapTile location;

	public MinMaxVertex(boolean max,
						MinMaxVertex father,
						ArrayList<MinMaxVertex> children,
						MapTile location){
		this.max = max;
		this.father = father;
		this.children = children;
		this.location = location;
	}

	void setValue(double value){this.value = value;}

	public boolean isMax() {
		return max;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public double getValue() {
		return value;
	}

	public ArrayList<MinMaxVertex> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<MinMaxVertex> children) {
		this.children = children;
	}

	public MapTile getLocation() {
		return location;
	}

	public void setLocation(MapTile location) {
		this.location = location;
	}

	public MinMaxVertex getFather() {
		return father;
	}

	public void setFather(MinMaxVertex father) {
		this.father = father;
	}
}
