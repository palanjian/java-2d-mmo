package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteHandler {
	private int tileSize;
	private static int columns;
	private static int rows;

	private String file;
	private BufferedImage spritesheet;
	private Map<Integer, BufferedImage> spriteMap;
	
	public SpriteHandler(String filename, int gameTileSize, int columns, int rows){
		this.file = filename;
		this.tileSize = gameTileSize;
		this.columns = columns;
		this.rows = rows;
		this.spritesheet = loadImage(file);
		this.spriteMap = getSpriteMap();
	}
	
	public BufferedImage loadImage(String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File ("res/" + file + ".png"));
		} catch(IOException e) {
			System.out.println("Unable to load sprite sheet");
			System.exit(0);
		}
		return image;
	}
	
	public BufferedImage extractSprite(int col, int row) {
		return spritesheet.getSubimage((row*tileSize) - tileSize, (col*tileSize) - tileSize, tileSize, tileSize);
	}
	
	public Map<Integer, BufferedImage> getSpriteMap(){
		int index = 0;
		Map<Integer, BufferedImage> sMap = new HashMap<Integer, BufferedImage>();
		for(int col = 1; col <=columns; ++col) {
			for(int row = 1; row <=rows; ++row) {
				BufferedImage image = extractSprite(col, row);
				sMap.put(index, image);
				++index;
			}
		}
		return sMap;
	} 
	
	public BufferedImage get(int index) {
		return spriteMap.get(index);
	}
}
