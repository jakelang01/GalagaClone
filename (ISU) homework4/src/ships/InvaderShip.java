package ships;

/**
 * @author jakelangenfeld
 */

import utils.Position;

public abstract class InvaderShip extends SpaceShip {
	public static final int NUM_INVADER_SHIPS = 4;
	public static final double SHIP_SPACING = 60;
	public static final int SHIPS_X = 6;
	public static final int SHIPS_Y = 6;
	public static final double FIRING_PROBABILITY = .0012;

	/**
	 * Constructs an InvaderShip
	 * @param p - the initial position
	 * @param armor - the initial armor level
	 */
	public InvaderShip(Position p, int armor) {
		super(p, armor);
	}

	public abstract int getPoints();

}