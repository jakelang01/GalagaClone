package ships;

/**
 * @author jakelangenfeld
 */

import projectiles.Projectile;
import utils.Position;

public class ShooterShip extends InvaderShip {
  /**
   * Constructs a ShooterShip
   * @param p The initial position
   * @param armor The initial armor level
   */
	public ShooterShip(Position p, int armor) {
		super(p, armor);
	}

	/**
	 * Fires projectiles
	 * @return An array of one or more projectiles
	 */
	@Override
	public Projectile[] fire() {
		if(!canFire())
		{
			return null;
		}
		
		lastShotTime = System.currentTimeMillis();
		Projectile[] out = new Projectile[1];
		Position p = new Position(pos.getX(), pos.getY());
		out[0] = new Projectile(p, 0, -PROJECTILE_SPEED, -Projectile.GRAVITY);
		
		return out;
	}

	@Override
	public String imgPath() {
		return "res/monster.png";
	}

	@Override
	public int getPoints() {
		return 50;
	}
}
