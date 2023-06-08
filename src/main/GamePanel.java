package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.Socket;

import javax.swing.JPanel;

import entity.Player;
import graphics.TileHandler;

public class GamePanel extends JPanel implements Runnable{
	
	//screen settings
	final int originalTileSize = 16; //16x16 tile
	final int scale = 4;
	
	final int tileSize = originalTileSize * scale; //48x48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; //768 pixels
	final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	final int FPS = 60;
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	NonPlayerHandler nonPlayerHandler;
	TileHandler tileHandler;
	Thread nonPlayerThread;
	Player player;

	//host and port info
	static String host = "localhost";
	static int port = 4000;
	static Socket socket;
	

	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.GRAY);
		this.setDoubleBuffered(true); //all drawing will be done in an offscreen painting buffer	
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		try {
			System.out.println("Attempting to connect to server " + host + " on port:" + port);
			socket = new Socket(host, port);
			System.out.println("Successfully connected to server.");
			
			player = new Player(this, keyHandler, socket);
			nonPlayerHandler = new NonPlayerHandler(socket, this);
			tileHandler = new TileHandler(this);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		//if user is able to connect to socket, enters username -> display the screen
		Main.setVisible();
		
		//begins to run the game loop
		gameThread = new Thread(this);
		gameThread.start();
		
		//begins running the thread that receives & displays information on other players
		nonPlayerThread = new Thread(nonPlayerHandler);			
		nonPlayerThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/60;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			//game loop
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if(delta >=1) {
				update();
				repaint(); //calls paintComponent		
				--delta;
			}
		}
	}	
	
	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		tileHandler.draw(g2);
		nonPlayerHandler.draw(g2);
		player.draw(g2);
		g2.dispose();
	}
	
	public int getTileSize() { return tileSize; }	
	public int getOriginalTileSize() { return originalTileSize; }
	public int getMaxScreenCol() { return maxScreenCol; }
	public int getMaxScreenRow() { return maxScreenRow; }
	public int getScreenHeight() { return screenWidth; }
	public int getScreenWidth() { return screenHeight; }
	public Player getPlayer() { return player; }

}
