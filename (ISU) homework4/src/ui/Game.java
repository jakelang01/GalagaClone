package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The main class to run the game
 * @author yalabsi - Yousef Alabsi
 * 
 * - Sound effects were extracted from the 1978 game by Taito
 * - Title screen was designed by @jmusil  
 *
 */

public class Game implements Runnable {
	/**
	 * This method creates the interface itself and populates it with two buttons
	 * and the SpaceInvaders frame
	 */
	public void run() {
		final JFrame frame = new JFrame("Space Invaders");

		final SpaceInvaders court = new SpaceInvaders();
		frame.add(court, BorderLayout.CENTER);

		final JPanel cPanel = new JPanel();
		frame.add(cPanel, BorderLayout.NORTH);

		final JButton reset = new JButton("Reset");

		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});

		final JButton mute = new JButton("Mute");

		mute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.mute();
				mute.setText(mute.getText().equals("Mute") ? "Unmute" : "Mute");
			}
		});

		cPanel.add(reset);
		cPanel.add(mute);

		/*
		 * https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-
		 * centered-regardless-of-monitor-resolution/15000866
		 */
		frame.setPreferredSize(new Dimension(700, 755));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	/**
	 * Invokes the game instance appropriately
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game()); /* https://www.javamex.com/tutorials/threads/invokelater.shtml */

	}
}
