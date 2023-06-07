package graphics;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteHandler {
	
	public static BufferedImage loadImage(String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File ("res/sprites/" + file + ".png"));
		} catch(IOException e) {
			System.out.println("Unable to load sprite sheet");
			System.exit(0);
		}
		return image;
	}
	
	public static Map<Integer, BufferedImage> getSpriteMap(BufferedImage image, int columns, int rows, int tileSize){
		int index = 0;
		Map<Integer, BufferedImage> sMap = new HashMap<Integer, BufferedImage>();
		for(int col = 1; col <=columns; ++col) {
			for(int row = 1; row <=rows; ++row) {
				BufferedImage sprite = extractSprite(image, col, row, tileSize);
				sMap.put(index, sprite);
				++index;
			}
		}
		return sMap;
	} 
	
	public static BufferedImage extractSprite(BufferedImage image, int col, int row, int tileSize) {
		return image.getSubimage((row*tileSize) - tileSize, (col*tileSize) - tileSize, tileSize, tileSize);
	}
	
	public static byte[] bufferedImageToBytes(BufferedImage image, String format) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    try {
			ImageIO.write(image, format, outputStream);
		} catch (IOException e) { e.printStackTrace(); }
	    return outputStream.toByteArray();
	}
	
}
