package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import main.GamePanel;
import packets.EntityPacket;

public class NonPlayerGraphicsHandler {
	
	private GamePanel gamePanel;
	private Map<Integer, EntityPacket> allEntityInfos;
	private Map<Integer, BufferedImage[]> allEntitySprites;


	//reused code from chatHandler, could probably clean up
	Font FONT;
	int FONT_SIZE;
	public NonPlayerGraphicsHandler(GamePanel gamePanel){
		this.gamePanel = gamePanel;
		this.FONT = gamePanel.font;
		this.FONT_SIZE = gamePanel.fontSize;

		allEntityInfos = new HashMap<>();
		allEntitySprites = new HashMap<>();
	}

	public void service(EntityPacket entity) {
		if (!entity.getOnline()) {
			// When an entity disconnects
			removeEntityInfo(entity);
			removeEntitySprite(entity);
			return;
		}
		byte[] spritesheet = entity.getSpritesheet();

		addEntityInfo(entity);
		if (spritesheet != null) {
			addSprite(entity);
		}
		else System.out.println("Received entity info but no sprite");
	}

	public void addEntityInfo(EntityPacket entity) {
		allEntityInfos.put(entity.getId(), entity);
	}
	public void removeEntitySprite(EntityPacket entity) {
		allEntitySprites.remove(entity.getId()); //O(1)
	}
	public void removeEntityInfo(EntityPacket entity) {
		allEntityInfos.remove(entity.getId()); //O(1)
	}

	public void addSprite(EntityPacket entity) {
		//System.out.println("Adding sprite for entity with ID: " + entity.getId() + " and username: " + entity.getUsername());
		byte[] bytes = entity.getSpritesheet();
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage spritesheet = null;
		try {
			spritesheet = ImageIO.read(is);
		} catch (IOException e) { e.printStackTrace(); }
		
		BufferedImage[] spriteMap = GraphicsUtil.getSpriteArray(spritesheet, 4, 4, gamePanel.originalTileSize);
		allEntitySprites.put(entity.getId(), spriteMap);
	}


	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		drawEntities(g2);
	}

	public void drawEntities(Graphics2D g2) {
		for(EntityPacket entity : allEntityInfos.values()) {
			BufferedImage[] spriteArray;
			BufferedImage image;
			
			try {
				spriteArray = allEntitySprites.get(entity.getId());
				image = spriteArray[0]; //default value
			}
			catch(Exception e) {
				//System.out.println(e.getMessage() + " for player " + entity.getId());
				break;
			}
			image = spriteArray[entity.getSpriteNumber()];

			int worldX = entity.getWorldX();
			int worldY = entity.getWorldY();
			int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
			int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY;
			
			if(GraphicsUtil.isInViewport(gamePanel, worldX, worldY)){
				//draws username above player
				Rectangle rect = new Rectangle(screenX, screenY - (gamePanel.tileSize/4), gamePanel.tileSize, 0);
				if(entity.getUsername() != null)
					GraphicsUtil.drawCenteredString(g2, entity.getUsername(), rect, new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
				
				//draws player sprite
				g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
			}
		}
	}

	public void removeAllInfos() {
		allEntityInfos = new HashMap<>();
		allEntitySprites = new HashMap<>();
	}
}