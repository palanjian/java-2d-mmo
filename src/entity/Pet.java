package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.CollisionUtil;
import graphics.GraphicsUtil;
import main.GamePanel;

public class Pet extends Entity{

	Player player;
	public Pet(GamePanel gamePanel, Player player) {
		super(gamePanel);
		
		this.player = player;
		
		direction = player.direction;
		speed = 4;
		worldX = player.worldX;
		worldY = player.worldY;
		collisionBox = new Rectangle(4 * gamePanel.scale, 6 * gamePanel.scale, 8 * gamePanel.scale, 48);
		
		isFindingPath = true;
		spriteArray = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage("entities/DOG_SPRITESHEET"), 4, 4, gamePanel.originalTileSize); // testing w solely chicken
	}
	
	public void service() {
		if(isFindingPath) {
			int goalCol = (player.worldX + player.collisionBox.x) / gamePanel.tileSize; 
			int goalRow = (player.worldY + player.collisionBox.y) / gamePanel.tileSize;
			
			searchPath(goalCol, goalRow);
		}
	}

	@Override
	public boolean moveCondition() {
		switch(player.direction) {
		case "up":
		case "down":
		case "left":
		case "right":
		}
		if(Math.abs(worldX - player.worldX) <= gamePanel.tileSize && Math.abs(worldY - player.worldY) <= gamePanel.tileSize) return false;
		return true;
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		
		
		if(player.worldX > worldX) {
			//direction is left
			image = spriteArray[0 + animState];
		}
		
		if(player.worldX <= worldX) {
			//direction is right
			image = spriteArray[8 + animState];
		}

		int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
		g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
	}
}
