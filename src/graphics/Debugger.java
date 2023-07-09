package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import entity.Pet;
import entity.Player;
import main.GamePanel;

public class Debugger {
	static GamePanel gamePanel;


	public static void draw(Graphics2D g2, GamePanel gp) {
		gamePanel = gp;
		drawPathfinding(g2);
		drawHitboxes(g2, gamePanel.player, Color.green);
		drawHitboxes(g2, gamePanel.player.pet, Color.blue);
	}
	
	public static void drawPathfinding(Graphics2D g2) {
		//pathfinding test
		
		g2.setColor(Color.red);
		for(int i=0; i< gamePanel.pathfinder.pathList.size(); i++) {
			int worldX = gamePanel.pathfinder.pathList.get(i).col * gamePanel.tileSize;
			int worldY = gamePanel.pathfinder.pathList.get(i).row * gamePanel.tileSize;
			int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
			int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
			
			g2.fillRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);
		} 
	}
	public static void drawHitboxes(Graphics2D g2, Entity e, Color c) {
		g2.setColor(c);
		int boxX = e.worldX + e.collisionBox.x;
		int boxY = e.worldY + e.collisionBox.y;
		g2.fillRect(boxX - gamePanel.player.getWorldX() + gamePanel.player.screenX, boxY - gamePanel.player.getWorldY() + gamePanel.player.screenY, e.collisionBox.height, e.collisionBox.width);
	}
}
