package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import graphics.GraphicsUtil;
import main.GamePanel;

public class Pet extends Entity{
	
	private GamePanel gamePanel;
	private Player player;
	private String petType;
	public BufferedImage[] spriteArray;

	Random rand = new Random();

	
	public Pet(GamePanel gamePanel, Player player, String petType) {
		this.gamePanel = gamePanel;
		this.player = player;
		this.petType = petType;
		spriteArray = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(petType), 4, 4, gamePanel.originalTileSize);			

		setDefaultValues();
	}
	
	public void setDefaultValues() {
		worldX = player.worldX + rand.nextInt(15);
		worldY = this.worldY + rand.nextInt(15);
	}
	
	public void draw(Graphics2D g2) {
		
	}
}
