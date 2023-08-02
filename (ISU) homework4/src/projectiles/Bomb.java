package projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.SpaceInvaders;
import utils.BoundingRing;
import utils.Position;

public class Bomb extends Projectile {
	private BoundingRing explosion;
	private boolean blowUp;

	public Bomb(Position initial, double xSpeed, double ySpeed, double yAcceleration, double explosionRadius) {
		super(initial, xSpeed, ySpeed, yAcceleration);

		explosion = new BoundingRing(pos, explosionRadius / 2);
	}

	public boolean blowUp(BoundingRing r) {
		return (blowUp = ((hit(r) || explosion.getY() >= SpaceInvaders.HEIGHT - 200) || explosion.collide(r)));
	}

	@Override
	public String imgPath() {
		return "res/bomb.png";
	}

	public void draw(Graphics2D g2d) {
		BufferedImage img = getImg();

		if(blowUp)
			System.out.println("SOFS");
		
		if (!blowUp)
			g2d.drawImage(img, (int) this.pos.getX(), (int) this.pos.getY(), img.getWidth(), img.getHeight(), null);
		else {
			g2d.drawImage(img, (int) this.pos.getX(), (int) this.pos.getY(), img.getWidth() * 3, img.getHeight() * 3,
					null);
		}
	}

}
