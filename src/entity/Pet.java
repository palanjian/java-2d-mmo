package entity;

public class Pet extends Entity{
	String type;
	Player player;
	
	public Pet(String type, Player player) {
		this.type = type;
		this.player = player;
		direction = player.direction;
		speed = player.speed;
		worldX = player.worldX;
		worldY = player.worldY;
		player.setHasPet(true);
	}
	
	public void update() {
		if((player.direction.equals("up") || player.direction.equals("down")) && worldX != player.worldX) {
			if(direction.equals("left"))  worldX -= speed;
			else if(direction.equals("right"))  worldX += speed;
			if(direction.equals("up"))  worldY -= speed;
			else if(direction.equals("down")) worldY += speed;
			return;
		}
		else if((player.direction.equals("left") || player.direction.equals("right")) && worldY != player.worldY) {
			if(direction.equals("left"))  worldX -= speed;
			else if(direction.equals("right"))  worldX += speed;
			if(direction.equals("up"))  worldY -= speed;
			else if(direction.equals("down")) worldY += speed;
			return;
		}
		else if((player.direction.equals("left")) && (direction.equals("right"))){
			if(player.worldX > worldX - player.tileSize) return;
		}
		else if((player.direction.equals("right")) && (direction.equals("left"))) {
			if(player.worldX < worldX + player.tileSize) return;
		}
		
		else if((player.direction.equals("up")) && (direction.equals("down"))){
			if(player.worldY > worldY - player.tileSize) return;

		}
		else if((player.direction.equals("down")) && (direction.equals("up"))){
			if(player.worldY < worldY + player.tileSize) return;
		}
		direction = player.direction;
		if(direction.equals("down")) worldY = player.worldY - player.tileSize;
		else if(direction.equals("up")) worldY = player.worldY + player.tileSize;
		else if(direction.equals("left")) worldX = player.worldX + player.tileSize;
		else if(direction.equals("right")) worldX = player.worldX - player.tileSize;
	}
}
