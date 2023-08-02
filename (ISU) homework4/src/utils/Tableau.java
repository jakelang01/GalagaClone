package utils;

/**
 * @author jakelangenfeld
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import projectiles.Bomb;
import projectiles.DefenderProjectile;
import projectiles.Projectile;
import ships.BomberShip;
import ships.DefenderShip;
import ships.InvaderShip;
import ships.MultiShooterShip;
import ships.ShooterShip;
import ships.SpaceShip;
import ships.TsarBombaShip;
import ui.SpaceInvaders;

public class Tableau {
  /**
   * The speed at which the invaders' fleet moves horizontally.
   */
    public static final double FLEET_MOVE_X = 4;
  /**
   * The speed at which the invaders' fleet moves vertically.
   */
	public static final double FLEET_MOVE_Y = 10;

	private InvaderShip[][] enemyFleet;
	private DefenderShip defender; 
	private ArrayList<Projectile> projectiles;
	private Random r;
	private boolean fleetMovePositiveX;
	private int score;

	public Tableau() {
		fleetMovePositiveX = true;
		r = new Random();
		enemyFleet = new InvaderShip[InvaderShip.SHIPS_Y][InvaderShip.SHIPS_X];
		for (int y = 0; y < InvaderShip.SHIPS_Y; y++) {
			for (int x = 0; x < InvaderShip.SHIPS_X; x++) {
				Position pos = new Position(((SpaceInvaders.WIDTH / 4) + (x * InvaderShip.SHIP_SPACING)),
						((y * InvaderShip.SHIP_SPACING)));
				switch (r.nextInt(InvaderShip.NUM_INVADER_SHIPS)) {
				case 0:
					enemyFleet[y][x] = new ShooterShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 1:
					enemyFleet[y][x] = new BomberShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 2:
					enemyFleet[y][x] = new MultiShooterShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				case 3:
					enemyFleet[y][x] = new TsarBombaShip(pos, r.nextInt(SpaceShip.MAX_ARMOR) + 1);
					break;
				}
			}
		}
		projectiles = new ArrayList<>();
		defender = new DefenderShip();
		score = 0;
	}


  /**
   * Checks all enemy ships to see if they are hit by d.  If ship is hit,
   * calls takeHit().  If ships armor falls to zero or below, score is
   * updated and ship is destroyed.
   * @param d A projectile
   * @return true if and only if a ship is hit
   */
    private boolean checkForEnemyHit(DefenderProjectile d) {
        for(int i = 0; i < enemyFleet.length; i++)
        {
        	for(int j = 0; j < enemyFleet[0].length; j++)
        	{
        		if(enemyFleet[i][j] != null && d.hit(enemyFleet[i][j].getBounds()))
        		{
        			enemyFleet[i][j].takeHit();
        			if(enemyFleet[i][j].getArmor() <= 0)
        			{
        				score += enemyFleet[i][j].getPoints();
        				enemyFleet[i][j] = null;
        			}
        			return true;
        		}
        	}
        }
        return false;
	}

	public void handleCollisions() {
		Projectile p;

		for (int i = projectiles.size() - 1; i >= 0; i--) {
			p = projectiles.get(i);

			if (p.isOutOfBounds()) {
				if (p instanceof Bomb) {
					if (defender != null && ((Bomb) p).blowUp(defender.getBounds())) {
						defender.takeHit();
					}
				}
				projectiles.remove(i);
				i--;
			} else if (p instanceof DefenderProjectile) {
				if (checkForEnemyHit((DefenderProjectile) p)) {
					projectiles.remove(i);
					i--;
				}
			} else if (defender != null && p.hit(defender.getBounds())) {
				defender.takeHit();
				projectiles.remove(i);
				i--;
				if (defender.getArmor() <= 0) {
					defender = null;
					return;
				}
			}

			p.nextPosition();
		}
	}

  /**
   * Returns a reference to the right-most ship in the invaders' fleet, or
   * null if there are no ships.
   * @return a reference to the right-most ship
   */
    private SpaceShip getRightMostEnemy() {
    	for(int i = enemyFleet[0].length - 1; i > 0; i--)
    	{
    		for(int j = enemyFleet.length - 1; j > 0; j--)
    		{
    			if(enemyFleet[i][j] != null)
    			{
    				return enemyFleet[i][j];
    			}
    		}
    	}
    	return null;
	}

  /**
   * Returns a reference to the left-most ship in the invaders' fleet, or
   * null if there are no ships.
   * @return a reference to the left-most ship
   */
	private SpaceShip getLeftMostEnemy() {
    	for(int i = 0; i < enemyFleet.length - 1; i++)
    	{
    		for(int j = 0; j < enemyFleet[0].length - 1; j++)
    		{
    			if(enemyFleet[i][j] != null)
    			{
    				return enemyFleet[i][j];
    			}
    		}
    	}
    	return null;
	}

  /**
   * Returns a reference to the bottom-most ship in the invaders' fleet, or
   * null if there are no ships.
   * @return a reference to the bottom-most ship
   */
	private SpaceShip getLowestEnemy() {
    	for(int i = enemyFleet.length - 1; i > 0; i--)
    	{
    		for(int j = enemyFleet[0].length - 1; j > 0; j--)
    		{
    			if(enemyFleet[i][j] != null)
    			{
    				return enemyFleet[i][j];
    			}
    		}
    	}
    	return null;
	}

	private void translateEnemyFleet(double deltaX, double deltaY) {
		for (int y = 0; y < InvaderShip.SHIPS_Y; y++) {
			for (int x = 0; x < InvaderShip.SHIPS_X; x++) {
				if (enemyFleet[y][x] != null) {
					enemyFleet[y][x].translate(deltaX, deltaY);

					if (defender.getBounds().collide(enemyFleet[y][x].getBounds())) {
						defender = null;
						return;
					}
					/* Overloading to handle firing here, too */
					if (r.nextDouble() < InvaderShip.FIRING_PROBABILITY) {
						Projectile[] p = enemyFleet[y][x].fire();
						if (p != null)
							projectiles.addAll(Arrays.asList(p));
					}
				}
			}
		}
	}


  /**
   * Does nothing if gameIsOver(), otherwise calculates the deltas to the
   * invaders next position and calls translateEnemyFleet() on those deltas.
   * fleetMovePositiveX gives the direction of fleet movement in the
   * horizontal; if true, the fleet moves in the positive X direction, else
   * it moves in the negative X direction.  The entire fleet moves until it
   * would go out of bounds (0 to SpaceInvaders.WIDTH), at which time, rather
   * than go out of bounds, its direction changes and it moves down.
   * Magnitude of the deltas are given by 0, FLEET_MOVE_X, and FLEET_MOVE_Y.
   */
    public void moveEnemyFleet() {
        if(gameIsOver())
        {
        	return;
        }
        else
        {
        	if(fleetMovePositiveX)
        	{
        		if(getRightMostEnemy().getPosition().getX() >= SpaceInvaders.WIDTH)
        		{
        			translateEnemyFleet(0, FLEET_MOVE_Y);
        			fleetMovePositiveX = false;
        		}
        		else
        		{
        			translateEnemyFleet(FLEET_MOVE_X, 0);
        		}
        	}
        	else
        	{
        		if(getLeftMostEnemy().getPosition().getX() <= 0)
        		{
        			translateEnemyFleet(0, FLEET_MOVE_Y);
        			fleetMovePositiveX = true;
        		}
        		else
        		{
        			translateEnemyFleet(FLEET_MOVE_X * -1, 0);
        		}
        	}
        }
	}

	public void moveDefender(double xAmount, double yAmount) {
		double newX, newY;
		Position pos;

		pos = defender.getPosition();
		newX = pos.getX() + xAmount;
		if (newX > SpaceInvaders.WIDTH - DefenderShip.SHIP_WIDTH / 2) {
			newX = SpaceInvaders.WIDTH - DefenderShip.SHIP_WIDTH / 2;
		}
		if (newX < 0) {
			newX = 0;
		}

		newY = pos.getY() + yAmount;
		if (newY > SpaceInvaders.HEIGHT - DefenderShip.SHIP_HEIGHT / 2) {
			newY = SpaceInvaders.HEIGHT - DefenderShip.SHIP_HEIGHT / 2;
		}
		if (newY < 0) {
			newY = 0;
		}

		defender.setPosition(newX, newY);
	}

	public boolean defenderDestroyed() {
		return defender == null;
	}

	public boolean enemyFleetDestroyed() {
		return getLowestEnemy() == null;
	}

	public boolean gameIsOver() {
		return defenderDestroyed() || enemyFleetDestroyed()
				|| getLowestEnemy().getPosition().getY() > SpaceInvaders.HEIGHT;
	}

	public void moveDefender(boolean left, boolean right, boolean up, boolean down, boolean fire) {
		if (gameIsOver())
			return;

		if (fire) {
			Projectile[] projectiles = defender.fire();
			if (projectiles != null)
				this.projectiles.addAll(Arrays.asList(projectiles));
		}

		if (right) {
			moveDefender(DefenderShip.MOVE_DELTA, 0);
		} else if (left) {
			moveDefender(-1 * DefenderShip.MOVE_DELTA, 0);
		} else if (up) {
			moveDefender(0, -1 * DefenderShip.MOVE_DELTA);
		} else if (down) {
			moveDefender(0, DefenderShip.MOVE_DELTA);
		}

	}

	public InvaderShip[][] getEnemyFleet(){
		return enemyFleet;
	}

	public ArrayList<Projectile> getProjectiles(){
		return projectiles;
	}
	
	public DefenderShip getDefenderShip() {
		return defender;
	}
	public int getScore() {
		return score;
	}
}
