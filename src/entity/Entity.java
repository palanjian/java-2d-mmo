package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.CollisionUtil;
import main.GamePanel;
import npc.Pathfinder;

public class Entity {
	
	public GamePanel gamePanel;
	public int worldX, worldY;
	public int speed;
	public String direction;
	public Rectangle collisionBox;
	
	private int epsilon = 0;
	private int animState = 0;
	private int animLeftOrRight = 0;
	public void service() { }
	boolean isFindingPath;
	public BufferedImage[] spriteArray;

	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void update() {
		service();
		
		++epsilon;
		if(epsilon > 10) {
			if(animState == 0) { animState = 1; }
			else if(animState == 2) { animState = 1; }
			else if(animState == 1 && animLeftOrRight == 0) { animState = 0; animLeftOrRight = 1; }
			else { animState = 2; animLeftOrRight = 0;}
			epsilon = 0;
		}
		
		if(CollisionUtil.canMove(gamePanel, this)) {
			if(direction.equals("up")) worldY -= speed;
			else if(direction.equals("down")) worldY += speed;
			else if(direction.equals("left")) worldX -= speed;
			else if(direction.equals("right")) worldX += speed;
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

		int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
		g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
	}
	
	public void searchPath(int goalCol, int goalRow) {
		int startCol = (worldX + collisionBox.x)/gamePanel.tileSize;
		int startRow = (worldY + collisionBox.y)/gamePanel.tileSize;
		
		Pathfinder pathfinder = gamePanel.pathfinder;
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
			
			//System.out.println(direction);
			/*
			int nextCol = gamePanel.pathfinder.pathList.get(0).col;
			int nextRow = gamePanel.pathfinder.pathList.get(0).row;
			if(nextCol == goalCol && nextRow == goalRow) {
				isFindingPath = false;
			} */
		}
	}
}
