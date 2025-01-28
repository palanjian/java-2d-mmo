package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.GamePanel;
import packets.TileMap;

public class TileHandler {

	private GamePanel gamePanel;
	private TileMap tileMap;
	private BufferedImage[] sprites;
	
	public TileHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;		
        sprites = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(gamePanel.getSpriteSheetFileName()), 
        		gamePanel.getSpriteSheetColumns(), gamePanel.getSpriteSheetRows(), gamePanel.originalTileSize);
	}
	
	public void service(TileMap tileMap){
		this.tileMap = tileMap;
	}
	
	public void draw(Graphics2D g2) {
		if(tileMap == null) return;
		for(int layer = 0; layer< tileMap.getLayers(); ++layer) {
			for(int row = 0; row < tileMap.getRows(); ++row) {
				for(int col = 0; col < tileMap.getColumns(); ++col) {
					int whichTile = 0;
					try {
						whichTile = tileMap.getTextureMap()[layer][row][col];
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						continue;
					}
					//mathematics to determine tile's screen position
					int worldX = col*gamePanel.tileSize;
					int worldY = row*gamePanel.tileSize;
					int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
					int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY; 
					if(GraphicsUtil.isInViewport(gamePanel, worldX, worldY)){
						g2.drawImage(sprites[whichTile], screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);	
					}
				}
			}
		}
	}
	
	//Getters & Setters
	public TileMap getTileMap() { return tileMap; }
}
