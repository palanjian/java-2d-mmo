package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.GraphicsUtil;
import main.GamePanel;
import packets.EntityInfo;

import static enums.Direction.*;
import static enums.Direction.RIGHT;

public class Pet extends Entity{

	Player player;
	private static EntityInfo entityInfo;
	private String entitySkinFileName = "entities/SNOWMAN_SPRITESHEET";

	public Pet(GamePanel gamePanel, Player player) {
		super(gamePanel);
		
		this.player = player;
		
		direction = player.direction;
		speed = 4;
		worldX = player.worldX + gamePanel.tileSize;
		worldY = player.worldY;
		collisionBox = new Rectangle(player.collisionBox.x + 8, player.collisionBox.y + 8, player.collisionBox.height + 8, player.collisionBox.width + 8);
		
		isFindingPath = true;
		spriteArray = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(entitySkinFileName), 4, 4, gamePanel.originalTileSize); // testing w solely chicken

		//entity has same id as it's owner, to identity it
		entityInfo = new EntityInfo(player.playerId, worldX, worldY, direction, 0, GraphicsUtil.bufferedImageToBytes(GraphicsUtil.loadImage(entitySkinFileName), "PNG"));
		//sends initial info
		gamePanel.requestsHandler.sendObject(entityInfo);
	}
	
	public void service() {
		if(isFindingPath) {
			int goalCol = (player.worldX + player.collisionBox.x) / gamePanel.tileSize; 
			int goalRow = (player.worldY + player.collisionBox.y) / gamePanel.tileSize;
			
			searchPath(goalCol, goalRow);

		}
	}

	public void update(){
		service();

		if(moveCondition()) {
			if(direction == UP) worldY -= speed;
			else if(direction == DOWN) worldY += speed;
			else if(direction == LEFT) worldX -= speed;
			else if(direction == RIGHT) worldX += speed;

			++epsilon;
			if(epsilon > 10) {
				if(animState == 0) { animState = 1; }
				else if(animState == 2) { animState = 1; }
				else if(animState == 1 && animLeftOrRight == 0) { animState = 0; animLeftOrRight = 1; }
				else { animState = 2; animLeftOrRight = 0;}
				epsilon = 0;
			}
			entityInfo.updatePosition(worldX, worldY, direction, animState);
			gamePanel.requestsHandler.sendObject(entityInfo);
		}
	}
	@Override
	public boolean moveCondition() {
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
			image = spriteArray[4 + animState];
		}

		int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
		g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
	}
}
