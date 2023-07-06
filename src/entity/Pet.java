package entity;

import main.GamePanel;

public class Pet extends Entity{

	Player player;
	public Pet(GamePanel gamePanel, Player player) {
		super(gamePanel);
		
		this.player = player;
		
		direction = player.direction;
		speed = player.speed;
		worldX = player.worldX;
		worldY = player.worldY;
	}
}
