package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.GamePanel;

public class TileHandler {
	
	GamePanel gamePanel;
	int mapBlueprint[][];
	
	String filename="tiles/OVERWORLD_TILESHEET";
	int columns = 36;
	int rows = 40;
	int originalTileSize;
	int tileSize;
	
	//WORLD SETTINGS
	private final int maxWorldCol = 50;
	private final int maxWorldRow = 50;
	private final int worldWidth = tileSize * maxWorldCol;
	private final int worldHeight = tileSize * maxWorldRow;
	
	BufferedImage spritesheet;
	BufferedImage[] tiles;
	
	
	public TileHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.tileSize = gamePanel.getTileSize();
		this.originalTileSize = gamePanel.getOriginalTileSize();
		
		mapBlueprint = new int[maxWorldRow][maxWorldCol];
		spritesheet = SpriteHandler.loadImage(filename);
		tiles = SpriteHandler.getSpriteArray(spritesheet, columns, rows, originalTileSize);
		loadMap();
	}
	
	public void draw(Graphics2D g2) {
		for(int row = 0; row < maxWorldRow; ++row) {
			for(int col = 0; col < maxWorldCol; col++) {
				int whichTile = mapBlueprint[row][col];
				//mathematics to determine tile's screen position
				int worldX = col*tileSize;
				int worldY = row*tileSize;
				int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
				int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY(); 

				//only what's in our viewport
				g2.drawImage(tiles[whichTile], screenX, screenY, tileSize, tileSize, null);		
			}
		}
	}
	
	public void loadMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			for(int row = 0; row < maxWorldRow; ++row) {
				String line = br.readLine();
				String numbers[] = line.split(" ");
				for(int col = 0; col < maxWorldCol; col++) {
					mapBlueprint[row][col] = Integer.parseInt(numbers[col]);
				}
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
}
