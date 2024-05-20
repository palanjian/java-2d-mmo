package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import graphics.CollisionUtil;
import graphics.GraphicsUtil;
import main.GamePanel;
import main.KeyHandler;
import main.RequestsHandler;
import packets.PlayerInfo;

import static enums.Direction.*;

public class Player extends Entity{
	
	private GamePanel gamePanel;
	private KeyHandler keyHandler;
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
	
	public Entity pet;
	public int playerId;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {
		
		super(gamePanel);
		
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.requestsHandler = gamePanel.requestsHandler;
		this.originalTileSize = gamePanel.originalTileSize;
		this.tileSize = gamePanel.tileSize;
		this.screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2); 
		this.screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2); 
		if(gamePanel.username.toLowerCase().equals("arsen")) playerSkinFileName = "players/ARSEN_SPRITESHEET";

		try {
			setDefaultValues();			
			//for now, unique identifier for each player is a random int of upper bound 2048
			playerId = rand.nextInt(2048);
			playerInfo = new PlayerInfo(playerId, gamePanel.username, worldX, worldY, direction, 0, GraphicsUtil.bufferedImageToBytes(GraphicsUtil.loadImage(playerSkinFileName), "PNG"));
						
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
		direction = DOWN;
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
			if(keyHandler.upPressed) direction = UP;
			else if(keyHandler.downPressed) direction = DOWN;
			else if(keyHandler.leftPressed) direction = LEFT;
			else if(keyHandler.rightPressed) direction = RIGHT;
			
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
				if(direction == UP) worldY -= speed;
				else if(direction == DOWN) worldY += speed;
				else if(direction == LEFT) worldX -= speed;
				else if(direction == RIGHT) worldX += speed;
			}
			
			playerInfo.updatePosition(worldX, worldY, direction, getSpriteNumber());
			requestsHandler.sendObject(playerInfo);

		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;

		image = spriteArray[getSpriteNumber()];
		if(direction == DOWN) {
			image = spriteArray[0 + animState];
		}
		if(direction == LEFT) {
			image = spriteArray[4 + animState];
		}
		if(direction == RIGHT) {
			image = spriteArray[8 + animState];
		}
		if(direction == UP) {
			image = spriteArray[12 + animState];
		}
		g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
	}

	public int getSpriteNumber(){
		int spriteNumber = 0;
		if(direction == DOWN) spriteNumber = 0;
		if(direction == LEFT) spriteNumber = 4;
		if(direction == RIGHT) spriteNumber = 8;
		if(direction == UP) spriteNumber = 12;
		return spriteNumber + animState;
	}
	
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	public PlayerInfo getPlayerInfo() { return playerInfo; }

}
