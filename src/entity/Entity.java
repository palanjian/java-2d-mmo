package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import enums.Direction;
import graphics.CollisionUtil;
import main.GamePanel;
import npc.Pathfinder;

import static enums.Direction.*;

public class Entity {
	
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
	boolean isFindingPath;
	public BufferedImage[] spriteArray;

	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void update() {
	}
	

	public void draw(Graphics2D g2) {
	}
	
	public void searchPath(int goalCol, int goalRow) {
		int startCol = (worldX + collisionBox.x)/gamePanel.tileSize;
		int startRow = (worldY + collisionBox.y)/gamePanel.tileSize;
		
		Pathfinder pathfinder = gamePanel.pathfinder;
		if(pathfinder == null) return; //quick fix to pathFinder not being instantiated before tilemap
									   //is sent from server, causing Null error
		pathfinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if(pathfinder.search() == true) {
			
			//next worldX and worldY
			int nextX = gamePanel.pathfinder.pathList.get(0).col * gamePanel.tileSize;
			int nextY = gamePanel.pathfinder.pathList.get(0).row * gamePanel.tileSize;
			
			//entity's solidarea position 
			int enLeftX = worldX + collisionBox.x;
			int enRightX = worldX + collisionBox.x + collisionBox.width;
			int enTopY = worldY + collisionBox.y;
			int enBottomY = worldY + collisionBox.y + collisionBox.height;
			
			//based on entity's current position, find out the relative direction of the next node
			if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
				direction = UP;
			}
			else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
				direction = DOWN;
			}
			else if(enTopY >= nextY && enBottomY < nextY + gamePanel.tileSize) {
				//left or right
				if(enLeftX > nextX) direction = LEFT;
				if(enLeftX < nextX) direction = RIGHT;
			}
			else if(enTopY > nextY && enLeftX > nextX) {
				//up or left
				direction = UP;
				if(!CollisionUtil.canMove(gamePanel, this)) direction = LEFT;
			}
			else if(enTopY > nextY && enLeftX < nextX) {
				//up or right
				direction = UP;
				if(!CollisionUtil.canMove(gamePanel, this)) direction = RIGHT;
			}
			else if(enTopY < nextY && enLeftX > nextX) {
				//down or left
				direction = DOWN;
				if(!CollisionUtil.canMove(gamePanel, this)) direction = LEFT;
			}
			else if(enTopY < nextY && enLeftX < nextX) {
				//down or right
				direction = DOWN;
				if(!CollisionUtil.canMove(gamePanel, this)) direction = RIGHT;
			}
		}
	}
}
