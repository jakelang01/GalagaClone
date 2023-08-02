package projectiles;

import utils.Position;

public class DefenderProjectile extends Projectile {
	public DefenderProjectile(Position initial, double ySpeed) {
		super(initial, 0, ySpeed, 0);
	}

	@Override
	public String imgPath() {
		return "res/projectile.png";
	}
}
