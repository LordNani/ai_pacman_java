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
		this.value = Double.NaN;
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

	@Override
	public String toString(){
		int length = 0;
		MinMaxVertex c_father = getFather();
		while(c_father!=null){
			c_father = c_father.getFather();
			++length;
		}
		return ((max) ? "Ghost " : "Pacman ")+location.point.toString()+" length: "+length;
	}

	public boolean isEvaluated() {
		return !(Double.isNaN(value));
	}
}
