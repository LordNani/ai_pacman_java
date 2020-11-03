package ai.minmax;

public class MinMaxVertex implements Cloneable{
	private boolean max;
	private MinMaxVertex[] children;
	private double value;

	public MinMaxVertex(boolean max,
						MinMaxVertex[] children){
		this.max = max;
		this.children = children.clone();
	}

	void setValue(double value){this.value = value;}

	public boolean isMax() {
		return max;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public MinMaxVertex[] getChildren() {
		return children;
	}

	public void setChildren(MinMaxVertex[] children) {
		this.children = children;
	}

	public double getValue() {
		return value;
	}
}
