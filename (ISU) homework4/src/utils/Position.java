package utils;

public class Position {
	double x, y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double distance(Position p) {
		return Math.sqrt(((p.x - x) * (p.x - x)) + ((p.y - y) * (p.y - y)));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}