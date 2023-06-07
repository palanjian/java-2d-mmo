package main;

import javax.swing.JFrame;

public class Main {
	
	static JFrame window;
	
	public static void main(String[] args) {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Java2DMMO");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		
		gamePanel.startGameThread();
	}
	public static void setVisible() {
		window.setVisible(true);
	}
}
