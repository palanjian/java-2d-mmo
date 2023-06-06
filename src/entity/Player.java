package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

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
		if(keyHandler.upPressed) {
			y -= speed;
			direction = "up";
			playerInfo.updatePosition(x, y, direction);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
		if(keyHandler.downPressed) {
			y += speed;
			direction = "down";
			playerInfo.updatePosition(x, y, direction);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }

		}
		if(keyHandler.leftPressed) {
			x -= speed;
			direction = "left";
			playerInfo.updatePosition(x, y, direction);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
		if(keyHandler.rightPressed) {
			x += speed;
			direction = "right";
			playerInfo.updatePosition(x, y, direction);
			try {
				objectOutputStream.writeUnshared(playerInfo);
				objectOutputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.white);
		g2.fillRect(x, y, 48, 48); //hardcoding tile size
	}
}
