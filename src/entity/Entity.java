package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import enums.Direction;
import main.GamePanel;

import static enums.Direction.*;

public abstract class Entity {
	
	public GamePanel gamePanel;
	public int worldX, worldY;
	public int speed;
	public Direction direction;
	public Rectangle collisionBox;
	
	protected int epsilon = 0;
	protected int animState = 0;
	protected int animLeftOrRight = 0;
	public void service() { }
	public boolean moveCondition() { return true; };
	public BufferedImage[] spriteArray;

	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public abstract void update();

	public abstract int getSpriteNumber();

	public abstract void draw(Graphics2D g2);

}
