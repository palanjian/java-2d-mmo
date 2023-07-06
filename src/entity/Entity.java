package entity;

import java.awt.Rectangle;

import main.GamePanel;

public class Entity {
	
	public GamePanel gamePanel;
	public int worldX, worldY;
	public int speed;
	public String direction;
	public Rectangle collisionBox;
	
	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
}
