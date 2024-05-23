package main;

import javax.swing.JFrame;

public class Main {
	
	private static JFrame window;
	public static GamePanel gp;

	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		setupGame();
	}	
	
	public void setupGame() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Java2DMMO");
		
		gp = new GamePanel();
		window.add(gp);
		
		window.pack();
		window.setLocationRelativeTo(null);
		
		gp.startGameThread();
	}
	
	public static void setVisible() {
		window.setVisible(true);
	}
}
