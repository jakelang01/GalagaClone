package ships;

import projectiles.DefenderProjectile;
import projectiles.Projectile;
import ui.SpaceInvaders;
import utils.Position;

public class DefenderShip extends SpaceShip {
	public static final String IMG_FILE = "res/spaceship.png";
	public static final double SHIP_HEIGHT = 60;
	public static final double SHIP_WIDTH = 50;
	public static final double Y_START_POS = SpaceInvaders.HEIGHT - SHIP_HEIGHT;
	public static final double X_START_POS = SpaceInvaders.WIDTH / 2;
	public static final double MOVE_DELTA = 5;

	public DefenderShip() {
		super(new Position(X_START_POS, Y_START_POS), MAX_ARMOR);
	}

	public Projectile[] fire() {
		if (!canFire()) {
			return null;
		}
		
		lastShotTime = System.currentTimeMillis();
		Projectile[] out = new Projectile[1];
		Position p = new Position(pos.getX() + DefenderShip.SHIP_WIDTH / 2, pos.getY() - DefenderShip.SHIP_HEIGHT / 2);
		out[0] = new DefenderProjectile(p, PROJECTILE_SPEED);

		return out;
	}

	@Override
	public String imgPath() {
		return IMG_FILE;
	}

}
