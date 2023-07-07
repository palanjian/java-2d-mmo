package entity;

import java.awt.Rectangle;

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
		collisionBox = new Rectangle(0, 0, 48, 48);
		
		isFindingPath = true;
	}
	
	public void update() {
		if(isFindingPath) {
			int goalCol = 20; 
			int goalRow = 9;
			
			searchPath(goalCol, goalRow);
		}
	}
}
