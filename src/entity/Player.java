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
	
	String filename = "players/DEFAULT_SPRITESHEET";
	int originalTileSize;
	int tileSize;
	
	//testing animation
	int epsilon;
	int animState;
	int animLeftOrRight;
	BufferedImage[] spriteArray;
	Random rand = new Random();
	
	public Player(GamePanel gamePanel, KeyHandler keyHandler, Socket socket) {
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.socket = socket;
		this.originalTileSize = gamePanel.getOriginalTileSize();
		this.tileSize = gamePanel.getTileSize();
		try {
			setDefaultValues();
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			playerInfo = new PlayerInfo(rand.nextInt(idUpperBound), x, y, direction, 0, SpriteHandler.bufferedImageToBytes(SpriteHandler.loadImage(filename), "PNG"));
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
		x = rand.nextInt(750);
		y = rand.nextInt(550);
		speed = 4;
		direction = "down";
	}
	
	public void update() {
		if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			//if you want diagonal movement to be possible, change else if to simply if
			if(keyHandler.upPressed) {
				y -= speed;
				direction = "up";
			}
			else if(keyHandler.downPressed) {
				y += speed;
				direction = "down";
			}
			else if(keyHandler.leftPressed) {
				x -= speed;
				direction = "left";
			}
			else if(keyHandler.rightPressed) {
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
		g2.drawImage(image, x, y, tileSize, tileSize, null);
	}
}
