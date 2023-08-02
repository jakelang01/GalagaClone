package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import projectiles.Projectile;
import ships.SpaceShip;
import utils.Tableau;

/**
 * This class provides the implementation of the 4 different screens as well as
 * read the keyboard inputs from the user in addition to starting a timer based
 * on the interval as well as handle the sound effects
 * 
 * @author yalabsi - Yousef Alabsi
 *
 */
public class SpaceInvaders extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;
	public static final int INTERVAL = 25;

	private Tableau tableau;
	private State STATUS, PREV;
	private boolean left, right, up, down, space, escape;
	private Timer timer;
	private Clip play;
	private boolean shoot;
	private boolean mute;

	enum State {
		INTRO, GAME, PAUSE, OVER
	}

	/**
	 * Constructor that sets up a Tableau, the intro music, the keyboard listeners,
	 * and the timer to set up a game.
	 */
	public SpaceInvaders() {

		tableau = new Tableau();
		STATUS = State.INTRO;

		try {
			AudioInputStream mainScreenMusic = AudioSystem.getAudioInputStream(new File("src/res/spaceinvaders.wav"));
			play = AudioSystem.getClip();
			play.open(mainScreenMusic);
			FloatControl volume = (FloatControl) play.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(1.0f);
			play.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}

		timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (STATUS == State.GAME) {
					int x = tableau.getProjectiles().size();
					tableau.moveDefender(left, right, up, down, space);
					shoot = tableau.getProjectiles().size() != x;
					tableau.handleCollisions();
					tableau.moveEnemyFleet();
					if (tableau.gameIsOver()) {
						STATUS = State.OVER;
					}
				}
			}
		});

		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && STATUS == State.INTRO) {
					timer.start();
					STATUS = State.GAME;
					play.stop();
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					left = true;
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					right = true;
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					up = true;
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					down = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					escape = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					space = true;
				}

				if (escape) {
					if (STATUS == State.PAUSE) {
						STATUS = PREV;
					} else {
						PREV = STATUS;
						STATUS = State.PAUSE;
					}
				}
			}

			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					up = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					down = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					left = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					right = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					space = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					escape = false;
				}
			}

		});
	}

	/**
	 * Resets everything by restarting the timer and the music.
	 */
	public void reset() {
		tableau = new Tableau();
		STATUS = State.INTRO;
		requestFocusInWindow();
		timer.restart();
		setFocusable(true);
		play.start();
	}

	/**
	 * Flips the mute state
	 */
	public void mute() {
		BooleanControl volume = (BooleanControl) play.getControl(BooleanControl.Type.MUTE);
		volume.setValue(volume.getValue() ^ true);
		mute = volume.getValue();
	}

	/**
	 * This method draws the game and handles the shooting sounds
	 * 
	 * @param g
	 */
	private void playGame(Graphics g) {
		int hitCount = 0;
		int style = Font.BOLD | Font.BOLD;
		Font font = new Font("Arial", style, 30);

		String img_name = "res/background.png";
		BufferedImage img = null;
		try {
			if (img == null) {
				img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
			}
		} catch (IOException e) {
			System.out.println("Background Image Error:" + e.getMessage());
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, null, 0, 0);

		for (SpaceShip[] x : tableau.getEnemyFleet()) {
			for (SpaceShip p : x) {
				if (p != null) {
					if (!p.isHit()) {
						p.draw(g2d);
					} else {
						p.setHit(false);
						hitCount++;
					}
				}
			}
		}

		for (Projectile x : tableau.getProjectiles()) {
			x.draw(g2d);
		}

		tableau.getDefenderShip().draw(g2d);

		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("SCORE:" + tableau.getScore(), 400, 40);

		if (mute) {
			return;
		}

		if (shoot) {
			try {
				Clip shoots = AudioSystem.getClip();
				shoots.open(AudioSystem.getAudioInputStream(new File("src/res/shoot.wav")));
				FloatControl volume = (FloatControl) shoots.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(1.0f);
				shoots.start();
				shoot = false;
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}

		if (hitCount > 0) {
			try {
				Clip shoots = AudioSystem.getClip();
				shoots.open(AudioSystem.getAudioInputStream(new File("src/res/invaderkilled.wav")));
				FloatControl volume = (FloatControl) shoots.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(1.0f);
				shoots.start();
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method draws the intro screen
	 * 
	 * @param g
	 */
	private void introScreen(Graphics g) {
		String img_name = "res/title.png";
		BufferedImage img = null;
		try {
			if (img == null) {
				img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
			}
		} catch (IOException e) {
			System.out.println("MEH3 Error:" + e.getMessage());
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, null, 0, 0);
	}

	/**
	 * This method draws the gameover screen
	 * 
	 * @param g
	 */

	private void gameOver(Graphics g) {
		String img_name = "res/end.png";
		BufferedImage img = null;
		try {
			if (img == null) {
				img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
			}
		} catch (IOException e) {
			System.out.println("MEH3 Error:" + e.getMessage());
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, null, 0, 0);
		int style = Font.BOLD | Font.BOLD;
		Font font = new Font("Arial", style, 50);

		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(tableau.defenderDestroyed() ? "You Lose" : "You Win", SpaceInvaders.WIDTH / 3,
				SpaceInvaders.HEIGHT / 2);

		g.drawString("SCORE:" + tableau.getScore(), SpaceInvaders.WIDTH / 3, SpaceInvaders.HEIGHT / 2 + 250);
	}

	/**
	 * This method draws a pause screen
	 * 
	 * @param g
	 */

	private void pause(Graphics g) {
		String img_name = "res/background.png";
		BufferedImage img = null;
		try {
			if (img == null) {
				img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(img_name));
			}
		} catch (IOException e) {
			System.out.println("MEH3 Error:" + e.getMessage());
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, null, 0, 0);
		int style = Font.BOLD | Font.BOLD;
		Font font = new Font("Arial", style, 50);

		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Game Paused", SpaceInvaders.WIDTH / 4, 100);
	}

	/**
	 * Switches between the screens appropriately
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.repaint();
		if (STATUS == State.INTRO) {
			introScreen(g);
		} else if (STATUS == State.GAME) {
			playGame(g);
		} else if (STATUS == State.OVER) {
			gameOver(g);
		} else if (STATUS == State.PAUSE) {
			pause(g);
		}
		requestFocusInWindow();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
