package projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.SpaceInvaders;
import utils.BoundingRing;
import utils.Position;

public class Projectile {
	public static final String IMG_FILE = "res/projectile.png";
    public static final double GRAVITY = -.01;
	public static final double SIZE = 14;
	public BufferedImage img;

	protected Position pos;
	protected double distancePerFrameX;
	protected double distancePerFrameY;
	protected double accelerationPerFrameY;
	protected BoundingRing ring;

	public Projectile(Position initial, double xSpeed, double ySpeed, double yAcceleration) {
		pos = new Position(initial.getX(), initial.getY());
		distancePerFrameX = xSpeed;
		distancePerFrameY = ySpeed;
		accelerationPerFrameY = yAcceleration;
		ring = new BoundingRing(pos, SIZE / 2);

		try {
			if (this.img == null) {
				this.img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imgPath()));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	public String imgPath() {
		return IMG_FILE;
	}

	public void nextPosition() {
		pos.set(pos.getX() + distancePerFrameX, pos.getY() + distancePerFrameY);
		distancePerFrameY += accelerationPerFrameY;
	}

	public boolean hit(BoundingRing r) {
		return ring.collide(r);
	}

	public Position getPosition() {
		return pos;
	}

	public boolean isOutOfBounds() {
		return (pos.getX() <= 0 || pos.getX() >= SpaceInvaders.WIDTH -24 || pos.getY() <= 0
				|| pos.getY() >= SpaceInvaders.HEIGHT -24);
	}

	/**
	 * Default draw method that provides how the object should be drawn in the GUI.
	 * This method does not draw anything. Subclass should override this method
	 * based on how their object should appear.
	 * 
	 * @param g2d The <code>Graphics</code> context used for drawing the object.
	 *            Remember graphics contexts that we used in OCaml, it gives the
	 *            context in which the object should be drawn (a canvas, a frame,
	 *            etc.)
	 */
	public void draw(Graphics2D g2d) {
		BufferedImage img = getImg();
		g2d.drawImage(img, (int) this.pos.getX(), (int) this.pos.getY(), img.getWidth(), img.getHeight(), null);
	}

	public static BufferedImage rotate(BufferedImage img, double rotation) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.toRadians(rotation), w / 2, h / 2);
		g2.drawImage(img, null, 0, 0);
		return newImage;
	}

	public BufferedImage getImg() {
		if (distancePerFrameY > 0)
			return rotate(this.img, 180);

		return rotate(this.img, 0);

	}
}
