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
import packets.EntityInfo;
import packets.PlayerInfo;

import static enums.Direction.*;

public class NonPlayerGraphicsHandler {
	
	private GamePanel gamePanel;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	private Map<Integer, EntityInfo> allEntityInfos;
	private Map<Integer, BufferedImage[]> allPlayerSprites;
	private Map<Integer, BufferedImage[]> allEntitySprites;


	//reused code from chatHandler, could probably clean up
	Font FONT;
	int FONT_SIZE;
	public NonPlayerGraphicsHandler(GamePanel gamePanel){
		this.gamePanel = gamePanel;
		this.FONT = gamePanel.font;
		this.FONT_SIZE = gamePanel.fontSize;
        allPlayerInfos = new HashMap<>();
        allPlayerSprites = new HashMap<>();

		allEntityInfos = new HashMap<>();
		allEntitySprites = new HashMap<>();
	}
	
	public void service(PlayerInfo player) {
		if(!player.getOnline()) {
			//when a player disconnects
			removePlayerInfo(player);
			removePlayerSprite(player);
			return;
		}
		
		byte[] spritesheet = player.getSpritesheet();
		if(spritesheet == null) addPlayerInfo(player);
		else {
			addPlayerInfo(player);
			addPlayerSprite(player);			
		}
	}

	public void service(EntityInfo entity) {
		if(!entity.getOnline()) {
			//when a player disconnects
			removeEntityInfo(entity);
			removeEntitySprite(entity);
			return;
		}
		byte[] spritesheet = entity.getSpritesheet();
		if(spritesheet == null) addEntityInfo(entity);
		else {
			addEntityInfo(entity);
			addEntitySprite(entity);
		}
	}


	public void addPlayerInfo(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
	
	public void removePlayerSprite(PlayerInfo player) {
		allPlayerSprites.remove(player.getId()); //O(1)
	}
	public void removePlayerInfo(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}


	public void addEntityInfo(EntityInfo entity) {
		allEntityInfos.put(entity.getId(), entity); //O(1)
	}
	public void removeEntitySprite(EntityInfo entity) {
		allEntitySprites.remove(entity.getId()); //O(1)
	}
	public void removeEntityInfo(EntityInfo entity) {
		allEntityInfos.remove(entity.getId()); //O(1)
	}

	public void addPlayerSprite(PlayerInfo player) {
		byte[] bytes = player.getSpritesheet();
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage spritesheet = null;
		try {
			spritesheet = ImageIO.read(is);
		} catch (IOException e) { e.printStackTrace(); }
		
		BufferedImage[] spriteMap = GraphicsUtil.getSpriteArray(spritesheet, 4, 4, gamePanel.originalTileSize);
		allPlayerSprites.put(player.getId(), spriteMap);
	}

	public void addEntitySprite(EntityInfo entity) {
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
		
		drawPlayers(g2);
		drawEntities(g2);
	}

	public void drawPlayers(Graphics2D g2) {
		for(PlayerInfo p : allPlayerInfos.values()) {
			BufferedImage[] spriteArray;
			BufferedImage image;
			
			try {
				spriteArray = allPlayerSprites.get(p.getId());
				image = spriteArray[0]; //default value
			}
			catch(Exception e) {
				break;
			}
			image = spriteArray[getWhichSpriteNumber(p)];
			
			int worldX = p.getPlayerX();
			int worldY = p.getPlayerY();
			int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
			int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY;
			
			if(GraphicsUtil.isInViewport(gamePanel, worldX, worldY)){
				//draws username above player
				Rectangle rect = new Rectangle(screenX, screenY - (gamePanel.tileSize/4), gamePanel.tileSize, 0);
				GraphicsUtil.drawCenteredString(g2, p.getUsername(), rect, new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
				
				//draws player sprite
				g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
				
			}
		}
	}

	public void drawEntities(Graphics2D g2) {
		for(EntityInfo p : allEntityInfos.values()) {
			BufferedImage[] spriteArray;
			BufferedImage image;

			try {
				spriteArray = allEntitySprites.get(p.getId());
				image = spriteArray[0]; //default value
			}
			catch(Exception e) {
				break;
			}
			image = spriteArray[getWhichSpriteNumber(p)];

			int worldX = p.getEntityX();
			int worldY = p.getEntityY();
			int screenX = worldX - gamePanel.player.getWorldX() + gamePanel.player.screenX;
			int screenY = worldY - gamePanel.player.getWorldY() + gamePanel.player.screenY;

			if(GraphicsUtil.isInViewport(gamePanel, worldX, worldY)){
				//draws player sprite
				g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

			}
		}
	}

	public int getWhichSpriteNumber(PlayerInfo p) {
		if(p.getDirection() == DOWN) {
			return 0 + p.getAnimState();
		}
		if(p.getDirection() == LEFT) {
			return 4 + p.getAnimState();
		}
		if(p.getDirection() == RIGHT) {
			 return 8 + p.getAnimState();
		}
		if(p.getDirection() == UP) {
			return 12 + p.getAnimState();
		}
		return 0;
	}
	public int getWhichSpriteNumber(EntityInfo p) {

		return 0;
	}
}