package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

import graphics.SpriteHandler;
import main.GamePanel;
import main.KeyHandler;
import main.PlayerInfo;

public class Player extends Entity{
	
	GamePanel gamePanel;
	KeyHandler keyHandler;
	Socket socket;
	
	static PlayerInfo playerInfo;
	ObjectOutputStream objectOutputStream;
	int idUpperBound = 2048;
	
	String filename = "players/ARSEN_SPRITESHEET";
	int originalTileSize;
	int tileSize;
	
	//testing animation
	int epsilon;
	int animState;
	int animLeftOrRight;
	BufferedImage[] spriteArray;
	Random rand = new Random();
	
	int screenX;
	int screenY;
	
	public Player(GamePanel gamePanel, KeyHandler keyHandler, Socket socket) {
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.socket = socket;
		this.originalTileSize = gamePanel.getOriginalTileSize();
		this.tileSize = gamePanel.getTileSize();
		this.screenY = gamePanel.getScreenWidth() / 2 - (gamePanel.getTileSize() / 2);
		this.screenX = gamePanel.getScreenHeight() / 2 - (gamePanel.getTileSize() / 2);
		System.out.println("screenX=" + screenX + "screenY=" + screenY);
		try {
			setDefaultValues();
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			playerInfo = new PlayerInfo(rand.nextInt(idUpperBound), worldX, worldY, direction, 0, SpriteHandler.bufferedImageToBytes(SpriteHandler.loadImage(filename), "PNG"));
			//sends initial location
			objectOutputStream.writeUnshared(playerInfo);
			objectOutputStream.flush();
			
			//playerInfo.setSpritesheet(null);
			spriteArray = SpriteHandler.getSpriteArray(SpriteHandler.loadImage(filename), 4, 4, originalTileSize);			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public void setDefaultValues() {
		worldX = rand.nextInt(((50*64)/2)-((46*64)/2)) + ((46*64)/2);
		worldY = rand.nextInt(((50*64)/2)-((46*64)/2)) + ((46*64)/2);
		speed = 4;
		direction = "down";
	}
	
	public void update() {
		if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			//if you want diagonal movement to be possible, change else if to simply if
			if(keyHandler.upPressed) {
				worldY -= speed;
				direction = "up";
			}
			else if(keyHandler.downPressed) {
				worldY += speed;
				direction = "down";
			}
			else if(keyHandler.leftPressed) {
				worldX -= speed;
				direction = "left";
			}
			else if(keyHandler.rightPressed) {
				worldX += speed;
				direction = "right";
			}
			++epsilon;
			if(epsilon > 10) {
				if(animState == 0) { animState = 1; }
				else if(animState == 2) { animState = 1; }
				else if(animState == 1 && animLeftOrRight == 0) { animState = 0; animLeftOrRight = 1; }
				else { animState = 2; animLeftOrRight = 0;}
				epsilon = 0;
			}
			
			playerInfo.updatePosition(worldX, worldY, direction, animState);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
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
	}
	
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	public int getScreenX() { return screenX; }
	public int getScreenY() { return screenY; }
}
