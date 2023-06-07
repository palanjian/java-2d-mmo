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
	
	BufferedImage spritesheet;
	BufferedImage[] tiles;
	
	
	public TileHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.tileSize = gamePanel.getTileSize();
		this.originalTileSize = gamePanel.getOriginalTileSize();
		
		mapBlueprint = new int[gamePanel.getMaxScreenRow()][gamePanel.getMaxScreenCol()];
		spritesheet = SpriteHandler.loadImage(filename);
		tiles = SpriteHandler.getSpriteArray(spritesheet, columns, rows, originalTileSize);
		loadMap();
	}
	
	public void draw(Graphics2D g2) {
		for(int row = 0; row < gamePanel.getMaxScreenRow(); ++row) {
			for(int col = 0; col < gamePanel.getMaxScreenCol(); col++) {
				int whichTile = mapBlueprint[row][col];
				g2.drawImage(tiles[whichTile], col*tileSize, row*tileSize, tileSize, tileSize, null);		
			}
		}
	}
	
	public void loadMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			for(int row = 0; row < gamePanel.getMaxScreenRow(); ++row) {
				String line = br.readLine();
				String numbers[] = line.split(" ");
				for(int col = 0; col < gamePanel.getMaxScreenCol(); col++) {
					mapBlueprint[row][col] = Integer.parseInt(numbers[col]);
				}
			}
			

		} catch(Exception e) { e.printStackTrace(); }
	}
}
