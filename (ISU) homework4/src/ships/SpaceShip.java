package ships;

/**
 * @author jakelangenfeld
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import projectiles.Projectile;
import utils.BoundingRing;
import utils.Position;

public abstract class SpaceShip {
	protected Position pos;
	protected int armorLevel;
	protected BoundingRing size;
	protected long lastShotTime;
	public static final double HALO_SIZE = 10;
	public static int MAX_ARMOR = 3;
	public static final double PROJECTILE_SPEED = -5;
	protected BufferedImage img;
	protected boolean blink;

	public SpaceShip(Position p, int armor) {
		pos = p;
		armorLevel = armor;
		size = new BoundingRing(p, HALO_SIZE);
		lastShotTime = 0;

		try {
			if (this.img == null) {
				this.img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imgPath()));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	public abstract String imgPath();

  /**
   * Fires projectiles
   * @return An array of one or more projectiles
   */
    public abstract Projectile[] fire();

	public boolean canFire() {
		if (System.currentTimeMillis() - lastShotTime > 100) {
			return true;
		}
		return false;
	}

	public int getArmor() {
		return armorLevel;
	}

	public Position getPosition() {
		return pos;
	}

  /**
   * Moves the ship a distance of deltaX horizontally and deltaY vertically.
   */
    public void translate(double deltaX, double deltaY) {
        pos.set(pos.getX() + deltaX, pos.getY() + deltaY);
	}

  /**
   * Places the ship at (newX, newY)
   */
	public void setPosition(double newX, double newY) {
		pos.set(newX, newY);
	}

  /**
   * Reduces the armor level of the ship and returns true if its bounding
   * ring intersects r, otherwise returns false
   * @return true if and only if there is a collision.
   */
    public boolean collide(BoundingRing r) {
		if(size.collide(r))
		{
			takeHit();
			return true;
		}
		return false;
	}

	public BoundingRing getBounds() {
		return size;
	}

	public void takeHit() {
		armorLevel--;
		blink = true;
	}

	public boolean isHit() {
		return blink;
	}

	public void setHit(boolean blink) {
		this.blink = blink;
	}

	public void draw(Graphics2D g2d) {
		BufferedImage img = getImg();
		g2d.drawImage(img, (int) this.pos.getX(), (int) this.pos.getY(), img.getWidth(), img.getHeight(), null);

		g2d.setColor(Color.WHITE);
		if (this.getArmor() > 1)
			g2d.drawRect((int) this.pos.getX(), (int) this.pos.getY(), 2, 2);
		if (this.getArmor() > 2)
			g2d.drawRect((int) this.pos.getX() + 5, (int) this.pos.getY(), 2, 2);

	}

	public BufferedImage getImg() {
		return this.img;
	}
}
