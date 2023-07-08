package entity;

import java.awt.Rectangle;

import graphics.CollisionUtil;
import graphics.GraphicsUtil;
import main.GamePanel;

public class Pet extends Entity{

	Player player;
	public Pet(GamePanel gamePanel, Player player) {
		super(gamePanel);
		
		this.player = player;
		
		direction = player.direction;
		speed = 3;
		worldX = player.worldX;
		worldY = player.worldY;
		collisionBox = new Rectangle(0, 0, 48, 48);
		
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
}
