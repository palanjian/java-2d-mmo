package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.GraphicsUtil;
import main.GamePanel;
import packets.EntityInfo;

import static enums.Direction.*;
import static enums.Direction.RIGHT;
import static enums.EntityType.PET;

public class Pet extends Entity{

	Player player;
	private static EntityInfo entityInfo;
	private String entitySkinFileName = "entities/SNOWMAN_SPRITESHEET";
	boolean isFindingPath;

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

		//the pet has the same id as its owner, + 5000. this should be changed later lol
		entityInfo = new EntityInfo(PET, player.playerId + 5000, worldX, worldY, direction, 0, GraphicsUtil.bufferedImageToBytes(GraphicsUtil.loadImage(entitySkinFileName), "PNG"), null);
		//sends initial info
		gamePanel.requestsHandler.sendObject(entityInfo);

		//once we send the sprite sheet once, we dont need to keep sending it over. already saved on server/clients
		entityInfo.setSpritesheet(null);
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

			entityInfo.updatePosition(worldX, worldY, direction, getSpriteNumber());
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

		image = spriteArray[getSpriteNumber()];

		int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
		g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
	}

	public int getSpriteNumber(){
		//default direction is left
		int spriteNumber = 0;
		//direction is right
		if(player.worldX <= worldX) spriteNumber = 4;
		return spriteNumber + animState;
	}
}
