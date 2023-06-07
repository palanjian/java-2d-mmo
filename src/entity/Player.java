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
	
	String filename = "spritesheet";
	int originalTileSize;
	int tileSize;
	
	//testing animation
	int epsilon;
	int animState;
	int animLeftOrRight;
	Map<Integer, BufferedImage> spriteMap;
	
	public Player(GamePanel gamePanel, KeyHandler keyHandler, Socket socket) {
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.socket = socket;
		this.originalTileSize = gamePanel.getOriginalTileSize();
		this.tileSize = gamePanel.getTileSize();
		try {
			setDefaultValues();
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			Random rand = new Random();
			playerInfo = new PlayerInfo(rand.nextInt(idUpperBound), x, y, direction, 0, SpriteHandler.bufferedImageToBytes(SpriteHandler.loadImage(filename), "PNG"));
			//sends initial location
			objectOutputStream.writeUnshared(playerInfo);
			objectOutputStream.flush();
			
			playerInfo.setSpritesheet(null);
			spriteMap = SpriteHandler.getSpriteMap(SpriteHandler.loadImage(filename), 4, 4, originalTileSize);			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		direction = "up";
	}
	
	public void update() {
		if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			if(keyHandler.upPressed) {
				y -= speed;
				direction = "up";
			}
			if(keyHandler.downPressed) {
				y += speed;
				direction = "down";
			}
			if(keyHandler.leftPressed) {
				x -= speed;
				direction = "left";
			}
			if(keyHandler.rightPressed) {
				x += speed;
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
			
			playerInfo.updatePosition(x, y, direction, animState);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		if(direction.equals("down")) {
			image = spriteMap.get(0 + animState);
		}
		if(direction.equals("left")) {
			image = spriteMap.get(4 + animState);
		}
		if(direction.equals("right")) {
			image = spriteMap.get(8 + animState);
		}
		if(direction.equals("up")) {
			image = spriteMap.get(12 + animState);
		}
		g2.drawImage(image, x, y, tileSize, tileSize, null);
	}
}
