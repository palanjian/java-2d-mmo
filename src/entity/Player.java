package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
	
	SpriteHandler spriteHandler;
	String filename = "spritesheet";
	int originalTileSize;
	int tileSize;
	
	//testing animation
	int animCounter;
	int animFrame;
	int animLeftOrRight;
	
	public Player(GamePanel gamePanel, KeyHandler keyHandler, Socket socket) {
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;
		this.socket = socket;
		try {
			setDefaultValues();
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			Random rand = new Random();
			playerInfo = new PlayerInfo(rand.nextInt(idUpperBound), x, y, direction); 
			
			//sends initial location
			objectOutputStream.writeUnshared(playerInfo);
			objectOutputStream.flush();
			
			originalTileSize = gamePanel.getOriginalTileSize();
			tileSize = gamePanel.getTileSize();
			spriteHandler = new SpriteHandler(filename, originalTileSize, 4, 4);
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
			
			++animCounter;
			if(animCounter > 10) {
				if(animFrame == 0) { animFrame = 1; }
				else if(animFrame == 2) { animFrame = 1; }
				else if(animFrame == 1 && animLeftOrRight == 0) { animFrame = 0; animLeftOrRight = 1; }
				else { animFrame = 2; animLeftOrRight = 0;}
				animCounter = 0;
			}
			
			playerInfo.updatePosition(x, y, direction);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		if(direction == "down") {
			image = spriteHandler.get(0 + animFrame);
		}
		if(direction == "left") {
			image = spriteHandler.get(4 + animFrame);
		}
		if(direction == "right") {
			image = spriteHandler.get(8 + animFrame);
		}
		if(direction == "up") {
			image = spriteHandler.get(12 + animFrame);

		}
		g2.drawImage(image, x, y, tileSize, tileSize, null);
	}
}
