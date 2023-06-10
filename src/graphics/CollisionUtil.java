package graphics;

import entity.Entity;
import main.GamePanel;
import packets.TileMap;

public class CollisionUtil {
	public static boolean canMove(GamePanel gamePanel, Entity entity) {
		int tileSize = gamePanel.getTileSize();
		TileMap temp = GamePanel.temp;
		boolean[][] collisionMap = temp.getCollisionMap();
		
		int entityLeftWorldX = entity.worldX + entity.collisionBox.x;
		int entityRightWorldX = entity.worldX + entity.collisionBox.x + entity.collisionBox.width;
		int entityTopWorldY = entity.worldY + entity.collisionBox.y;
		int entityBottomWorldY = entity.worldY + entity.collisionBox.y + entity.collisionBox.height;
		
		int entityLeftCol = entityLeftWorldX/tileSize;
		int entityRightCol = entityRightWorldX/tileSize;
		int entityTopRow = entityTopWorldY/tileSize;
		int entityBottomRow = entityBottomWorldY/tileSize;

		boolean tileNum1, tileNum2;
		
		if(entity.direction.equals("up")) {
			entityTopRow = (entityTopWorldY - entity.speed)/tileSize;
			tileNum1 = collisionMap[entityTopRow][entityLeftCol];
			tileNum2 = collisionMap[entityTopRow][entityRightCol];
			if(tileNum1 || tileNum2) { return false; }
		}
		else if(entity.direction.equals("down")){
			entityBottomRow = (entityBottomWorldY + entity.speed)/tileSize;
			tileNum1 = collisionMap[entityBottomRow][entityLeftCol];
			tileNum2 = collisionMap[entityBottomRow][entityRightCol];
			if(tileNum1 || tileNum2) { return false; }	
		}
		else if(entity.direction.equals("left")){
			entityLeftCol = (entityLeftWorldX - entity.speed)/tileSize;
			tileNum1 = collisionMap[entityTopRow][entityLeftCol];
			tileNum2 = collisionMap[entityBottomRow][entityLeftCol];
			if(tileNum1 || tileNum2) { return false; }
		}
		else if(entity.direction.equals("right")){
			entityRightCol = (entityRightWorldX + entity.speed)/tileSize;
			tileNum1 = collisionMap[entityTopRow][entityRightCol];
			tileNum2 = collisionMap[entityBottomRow][entityRightCol];
			if(tileNum1 || tileNum2) { return false; }
		} 
		return true;
	}
}
