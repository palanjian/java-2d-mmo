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
		collisionBox = player.collisionBox;
		
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

		//fix this
		Rectangle petCollisionBox = new Rectangle(worldX + collisionBox.x, worldY + collisionBox.y, collisionBox.height, collisionBox.width);
		Rectangle playerCollisionBox = new Rectangle(player.worldX + player.collisionBox.x, player.worldY + player.collisionBox.y, player.collisionBox.height, player.collisionBox.width);

		if(petCollisionBox.intersects(playerCollisionBox)) return false;
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
