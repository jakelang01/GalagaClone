package utils;

public class BoundingRing {
	private double radius;
	Position pos;

	public BoundingRing(Position p, double radius) {
		this.pos = p;
		this.radius = radius;
	}

	public boolean collide(BoundingRing r) {
		return pos.distance(r.pos) <= (radius + r.radius);
	}

	public double getX() {
		return pos.getX();
	}

	public double getY() {
		return pos.getY();
	}
}
