package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.util.Random;

import graphics.CollisionUtil;
import graphics.GraphicsUtil;
import main.GamePanel;
import main.KeyHandler;
import entity.Pet;
import main.RequestsHandler;
import packets.PlayerInfo;

public class Player extends Entity{
	
	private GamePanel gamePanel;
	private KeyHandler keyHandler;
	private Socket socket;	
	private RequestsHandler requestsHandler;
	private static PlayerInfo playerInfo;
	
	private String playerSkinFileName = "players/DEFAULT_SPRITESHEET";
	private int originalTileSize;
	public int tileSize;
	
	//testing animation
	private int epsilon;
	private int animState;
	private int animLeftOrRight;
	
	public BufferedImage[] spriteArray;
	Random rand = new Random();

	public int screenX;
	public int screenY;
	
	Pet pet;
	
	public Player(GamePanel gamePanel, KeyHandler keyHandler, Socket socket) {
		
		super(gamePanel);
		
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.socket = socket;
		this.requestsHandler = gamePanel.requestsHandler;
		this.originalTileSize = gamePanel.originalTileSize;
		this.tileSize = gamePanel.tileSize;
		this.screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2); 
		this.screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2); 
		if(gamePanel.username.toLowerCase().equals("arsen")) playerSkinFileName = "players/ARSEN_SPRITESHEET_R";

		try {
			setDefaultValues();			
			//for now, unique identifier for each player is a random int of upper bound 2048
			playerInfo = new PlayerInfo(rand.nextInt(2048), gamePanel.username, worldX, worldY, direction, 0, GraphicsUtil.bufferedImageToBytes(GraphicsUtil.loadImage(playerSkinFileName), "PNG"));
						
			//sends initial location
			requestsHandler.sendObject(playerInfo);
			
			//playerInfo.setSpritesheet(null);
			spriteArray = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(playerSkinFileName), 4, 4, originalTileSize);
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		pet = new Pet(gamePanel, this);

	}
	
	public void setDefaultValues() {
		worldX = rand.nextInt(((50*64)/2)-((46*64)/2)) + ((46*64)/2);
		worldY = rand.nextInt(((50*64)/2)-((46*64)/2)) + ((46*64)/2);
		speed = 4;
		direction = "down";
		//collision
		collisionBox = new Rectangle();
		collisionBox.x = 4 * gamePanel.scale;
		collisionBox.y = 6 * gamePanel.scale;
		collisionBox.height = 8 * gamePanel.scale;
		collisionBox.width = 8 * gamePanel.scale;
		}
	
	public void update() {
		if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			//if you want diagonal movement to be possible, change else if to simply if
			if(keyHandler.upPressed) direction = "up"; 
			else if(keyHandler.downPressed) direction = "down"; 
			else if(keyHandler.leftPressed) direction = "left"; 
			else if(keyHandler.rightPressed) direction = "right"; 
			
			++epsilon;
			if(epsilon > 10) {
				if(animState == 0) { animState = 1; }
				else if(animState == 2) { animState = 1; }
				else if(animState == 1 && animLeftOrRight == 0) { animState = 0; animLeftOrRight = 1; }
				else { animState = 2; animLeftOrRight = 0;}
				epsilon = 0;
			}
			
			//if canMove is true, player can move
			if(CollisionUtil.canMove(gamePanel, this)) {
				if(direction.equals("up")) worldY -= speed;
				else if(direction.equals("down")) worldY += speed;
				else if(direction.equals("left")) worldX -= speed;
				else if(direction.equals("right")) worldX += speed;
			}
			
			playerInfo.updatePosition(worldX, worldY, direction, animState);
			requestsHandler.sendObject(playerInfo);

		}
		pet.update();
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		if(direction.equals("down")) {
			image = spriteArray[0 + animState];
		}
		if(direction.equals("left")) {
			image = spriteArray[4 + animState];
		}
		if(direction.equals("right")) {
			image = spriteArray[8 + animState];
		}
		if(direction.equals("up")) {
			image = spriteArray[12 + animState];
		}
		g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
		pet.draw(g2); //temporary
	}
	
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	public PlayerInfo getPlayerInfo() { return playerInfo; }

}
