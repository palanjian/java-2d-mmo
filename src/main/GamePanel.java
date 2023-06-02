package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.Socket;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	//screen settings
	final int originalTileSize = 16; //16x16 tile
	final int scale = 3;
	
	final int tileSize = originalTileSize * scale; //48x48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; //768 pixels
	final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	final int FPS = 60;
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	
	//set players default position
	int playerX = 100;
	int playerY= 100;
	int playerSpeed = 4;
	
	
	//host and port info
	static String host = "localhost";
	static int port = 4000;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); //all drawing will be done in an offscreen painting buffer	
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		try {
			System.out.println("Attempting to connect to server " + host + " on port:" + port);
			Socket socket = new Socket(host, port);
			System.out.println("Successfully connected to server");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			//gameloop
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
		if(keyHandler.upPressed == true) {
			playerY -= playerSpeed;
		}
		if(keyHandler.downPressed == true) {
			playerY += playerSpeed;
		}
		if(keyHandler.leftPressed == true) {
			playerX -= playerSpeed;
		}
		if(keyHandler.rightPressed == true) {
			playerX += playerSpeed;
		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(playerX, playerY, tileSize, tileSize);

		g2.dispose();
	}
}
