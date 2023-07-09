package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JPanel;
import entity.Player;
import graphics.Debugger;
import graphics.TileHandler;
import npc.Pathfinder;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	
	//screen settings
	public final int originalTileSize = 16; //16x16 tile
	public final int scale = 4;
	public final int tileSize = originalTileSize * scale; //48x48
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	public final int FPS = 60;
	
	public KeyHandler keyHandler = new KeyHandler(this);
	
	public Thread gameThread;
	public RequestsHandler requestsHandler;
	public TileHandler tileHandler;
	public ChatHandler chatHandler;
	public Thread nonPlayerThread;
	public Player player;
	public String username;
	
	//host and port info
	private static String host = "localhost";
	private static int port = 4000;
	private static Socket socket;
	
	//sprite sheet information
	private String spriteSheetFileName = "tiles/OVERWORLD_TILESHEET";
	private int spriteSheetRows = 40;
	private int spriteSheetColumns = 36;
	
	//game state
	private int gameState;
	public int playState = 0;
	public int typingState = 1;
	
	//font settings
	private String fontFileName = "fonts/chatfont";
	public int fontSize = 24; //px
	public Font font;
	
	public Pathfinder pathfinder;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.GRAY);
		this.setDoubleBuffered(true); //all drawing will be done in an offscreen painting buffer	
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
		
		//initializes font
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/" + fontFileName + ".ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		}
		catch(IOException | FontFormatException e) {
			System.out.println("Error loading chat font.");
			System.exit(0);
		}
		
	}
	
	public void startGameThread() {
		try {
			System.out.println("Attempting to connect to server " + host + " on port:" + port);
			socket = new Socket(host, port);
			System.out.println("Successfully connected to server. Please enter your username:");
			Scanner scan = new Scanner(System.in);
			username = scan.nextLine().strip();
			//Instantiates all handlers
			tileHandler = new TileHandler(this);		
			chatHandler = new ChatHandler(this);
			requestsHandler = new RequestsHandler(socket, this);
			player = new Player(this, keyHandler, socket);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		//if user is able to connect to socket -> display the screen
		Main.setVisible();
		
		//begins to run the game loop
		gameThread = new Thread(this);
		gameThread.start();
		
		//begins running the thread that receives & displays information on other players
		nonPlayerThread = new Thread(requestsHandler);			
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
		if(gameState == playState) player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		requestsHandler.draw(g2);
		player.draw(g2);
		
		//temp
		Debugger.draw(g2, this);
		//temp
		
		g2.dispose();
	}
	
	public String getSpriteSheetFileName() { return spriteSheetFileName; }
	public int getSpriteSheetRows() { return spriteSheetRows; }
	public int getSpriteSheetColumns() { return spriteSheetColumns; }
	public void setFontFileName(String fontFileName) { this.fontFileName = fontFileName; }
	public int getGameState() { return gameState; }
	public void setGameState(int gameState) { this.gameState = gameState;}
	public Socket getSocket() { return socket; }
}
