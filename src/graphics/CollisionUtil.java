package graphics;

import entity.Entity;
import main.GamePanel;
import packets.TileMap;

public class CollisionUtil {
	public static boolean canMove(GamePanel gamePanel, Entity entity) {
		int tileSize = gamePanel.tileSize;
		TileMap tileMap = gamePanel.tileHandler.getTileMap();
		boolean[][] collisionMap = tileMap.getCollisionMap();
		
		int entityLeftWorldX = entity.worldX + entity.collisionBox.x;
		int entityRightWorldX = entity.worldX + entity.collisionBox.x + entity.collisionBox.width;
		int entityTopWorldY = entity.worldY + entity.collisionBox.y;
		int entityBottomWorldY = entity.worldY + entity.collisionBox.y + entity.collisionBox.height;
		
		int entityLeftCol = entityLeftWorldX/tileSize;
		int entityRightCol = entityRightWorldX/tileSize;
		int entityTopRow = entityTopWorldY/tileSize;
		int entityBottomRow = entityBottomWorldY/tileSize;

		boolean collider1, collider2;
		
		if(entity.direction.equals("up")) {
			entityTopRow = (entityTopWorldY - entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityLeftCol];
			collider2 = collisionMap[entityTopRow][entityRightCol];
			if(collider1 || collider2) { return false; }
		}
		else if(entity.direction.equals("down")){
			entityBottomRow = (entityBottomWorldY + entity.speed)/tileSize;
			collider1 = collisionMap[entityBottomRow][entityLeftCol];
			collider2 = collisionMap[entityBottomRow][entityRightCol];
			if(collider1 || collider2) { return false; }	
		}
		else if(entity.direction.equals("left")){
			entityLeftCol = (entityLeftWorldX - entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityLeftCol];
			collider2 = collisionMap[entityBottomRow][entityLeftCol];
			if(collider1 || collider2) { return false; }
		}
		else if(entity.direction.equals("right")){
			entityRightCol = (entityRightWorldX + entity.speed)/tileSize;
			collider1 = collisionMap[entityTopRow][entityRightCol];
			collider2 = collisionMap[entityBottomRow][entityRightCol];
			if(collider1 || collider2) { return false; }
		} 
		return true;
	}
}
