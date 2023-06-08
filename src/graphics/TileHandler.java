package graphics;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import main.GamePanel;
import packets.TileMap;

public class TileHandler {

	GamePanel gamePanel;
	TileMap tileMap;
	BufferedImage[] sprites;
	
	public TileHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;		
        
        sprites = GraphicsUtil.getSpriteArray(GraphicsUtil.loadImage(gamePanel.getSpriteSheetFileName()), gamePanel.getSpriteSheetColumns(), gamePanel.getSpriteSheetRows(), gamePanel.getOriginalTileSize());
	}
	
	public void service(TileMap tileMap){
		this.tileMap = tileMap;
	}
	
	public void draw(Graphics2D g2) {
		if(tileMap == null) return;
		for(int layer = 0; layer< tileMap.getLayers(); ++layer) {
			for(int row = 0; row < tileMap.getRows(); ++row) {
				for(int col = 0; col < tileMap.getColumns(); ++col) {
					int whichTile = tileMap.getMapBlueprint()[layer][row][col];
					//mathematics to determine tile's screen position
					int worldX = col*gamePanel.getTileSize();
					int worldY = row*gamePanel.getTileSize();
					int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
					int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY(); 
					g2.drawImage(sprites[whichTile], screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);	
				}
			}
		}
	}
}
