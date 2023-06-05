package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
	Thread nonPlayerThread;
	
	//set players default position
	int playerX = 100;
	int playerY= 100;
	int playerSpeed = 4;
	
	
	//host and port info
	static String host = "localhost";
	static int port = 4000;
	static Socket socket;

	static PlayerInfo playerInfo;
	ObjectOutputStream objectOutputStream;
	String username = "Peter";
	
	private Map<Integer, PlayerInfo> allPlayerInfos;
	int idUpperBound = 2048;
	
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
			socket = new Socket(host, port);
			System.out.println("Successfully connected to server. Please enter your username:");
			Scanner scan = new Scanner(System.in);
			username = scan.nextLine().strip();
			
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			Random rand = new Random();
			playerInfo = new PlayerInfo(rand.nextInt(idUpperBound), username, playerX, playerY);
			allPlayerInfos = new HashMap<Integer, PlayerInfo>();
			pAddPlayer(playerInfo);
			
			//sends initial location
			objectOutputStream.writeUnshared(playerInfo);
			objectOutputStream.flush();
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
		NonPlayerHandler nonPlayerHandler = new NonPlayerHandler(socket, this);
		nonPlayerThread = new Thread(nonPlayerHandler);
		nonPlayerThread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
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

		if(keyHandler.upPressed) {
			playerY -= playerSpeed;
			playerInfo.updatePosition(playerX, playerY);
			
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
		if(keyHandler.downPressed) {
			playerY += playerSpeed;
			playerInfo.updatePosition(playerX, playerY);
			
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }

		}
		if(keyHandler.leftPressed) {
			playerX -= playerSpeed;
			playerInfo.updatePosition(playerX, playerY);

			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
		if(keyHandler.rightPressed) {
			playerX += playerSpeed;
			playerInfo.updatePosition(playerX, playerY);
			
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		for(PlayerInfo p : allPlayerInfos.values()) {
			g2.setColor(Color.white);
			g2.fillRect(p.getPlayerX(), p.getPlayerY(), 48, 48); //hardcoding tile size
		}
		g2.dispose();
	}
	
	public void pRemovePlayer(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	
	public void pAddPlayer(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
}
