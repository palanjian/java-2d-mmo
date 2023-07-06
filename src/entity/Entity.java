package entity;

import java.awt.Rectangle;

import graphics.CollisionUtil;
import main.GamePanel;
import npc.Pathfinder;

public class Entity {
	
	public GamePanel gamePanel;
	public int worldX, worldY;
	public int speed;
	public String direction;
	public Rectangle collisionBox;
	
	boolean isFindingPath;
	int delta = 0;
	
	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void searchPath(int goalCol, int goalRow) {
		
		int startCol = (worldX + collisionBox.x)/gamePanel.tileSize;
		int startRow = (worldY + collisionBox.y)/gamePanel.tileSize;
		
		Pathfinder pathfinder = gamePanel.pathfinder;
		pathfinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if(pathfinder.search() == true) {
			
			int nextCol = gamePanel.pathfinder.pathList.get(0).col;
			int nextRow = gamePanel.pathfinder.pathList.get(0).row;
			//next worldX and worldY
			int nextX = nextCol * gamePanel.tileSize;
			int nextY = nextRow * gamePanel.tileSize;
			
			//entity's solidarea position 
			int enLeftX = worldX + collisionBox.x;
			int enRightX = worldX + collisionBox.x + collisionBox.width;
			int enTopY = worldY + collisionBox.y;
			int enBottomY = worldY + collisionBox.y + collisionBox.height;
			
			//based on entity's current position, find out the relative direction of the next node
			if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
				direction = "up";
			}
			else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.tileSize) {
				direction = "down";
			}
			else if(enTopY >= nextY && enBottomY < nextY + gamePanel.tileSize) {
				//left or right
				if(enLeftX > nextX) direction = "left";
				if(enLeftX < nextX) direction = "right";
			}
			else if(enTopY > nextY && enLeftX > nextX) {
				//up or left
				direction = "up";
				if(!CollisionUtil.canMove(gamePanel, this)) direction = "left";
			}
			else if(enTopY > nextY && enLeftX < nextX) {
				//up or right
				direction = "up";
				if(!CollisionUtil.canMove(gamePanel, this)) direction = "right";
			}
			else if(enTopY < nextY && enLeftX > nextX) {
				//down or left
				direction = "down";
				if(!CollisionUtil.canMove(gamePanel, this)) direction = "left";
			}
			else if(enTopY < nextY && enLeftX < nextX) {
				//down or right
				direction = "down";
				if(!CollisionUtil.canMove(gamePanel, this)) direction = "right";
			}
			
			System.out.println(direction);
			
			if(nextCol == goalCol && nextRow == goalRow) {
				isFindingPath = false;
			}
		}
	}
}
