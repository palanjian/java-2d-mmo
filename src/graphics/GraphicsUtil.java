package graphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;

public class GraphicsUtil {
	
	public static BufferedImage loadImage(String filename) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File ("res/" + filename + ".png"));
		} catch(IOException e) {
			System.out.println("Unable to load sprite sheet");
			System.exit(0);
		}
		return image;
	}
	
	public static BufferedImage[] getSpriteArray(BufferedImage image, int columns, int rows, int tileSize){
		int index = 0;
		BufferedImage[] output = new BufferedImage[columns * rows];
		for(int col = 1; col <=columns; ++col) {
			for(int row = 1; row <=rows; ++row) {
				BufferedImage sprite = extractSprite(image, col, row, tileSize);
				output[index] = sprite;
				++index;
			}
		}
		return output;
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
	
	public static boolean isInViewport(GamePanel gamePanel, int absX, int absY) {
		Player player = gamePanel.player;
		int tileSize = gamePanel.tileSize;
		int playerX = player.getWorldX(); 
		int playerY = player.getWorldY();
		int screenX = player.screenX; 
		int screenY = player.screenY;
		
		if(absX + tileSize > playerX - screenX && absX - tileSize < playerX + screenX &&
		   absY + tileSize > playerY - screenY && absY - tileSize < playerY + screenY) return true;
		return false;
	}
	
	public static void drawCenteredString(Graphics2D g2, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g2.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g2.setFont(font);
	    // Draw the String
	    g2.drawString(text, x, y);
	}
}
