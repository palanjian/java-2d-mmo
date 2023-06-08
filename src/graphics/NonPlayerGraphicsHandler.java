package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import main.GamePanel;
import packets.PlayerInfo;

public class NonPlayerGraphicsHandler {
	
	GamePanel gamePanel;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	private Map<Integer, BufferedImage[]> allPlayerSprites;
	
	public NonPlayerGraphicsHandler(GamePanel gamePanel){
		this.gamePanel = gamePanel;
        allPlayerInfos = new HashMap<>();
        allPlayerSprites = new HashMap<>();
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
	
	public void removePlayerInfo(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	
	public void addPlayerInfo(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
	
	public void removePlayerSprite(PlayerInfo player) {
		allPlayerSprites.remove(player.getId());
	}
	
	public void addPlayerSprite(PlayerInfo player) {
		byte[] bytes = player.getSpritesheet();
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage spritesheet = null;
		try {
			spritesheet = ImageIO.read(is);
		} catch (IOException e) { e.printStackTrace(); }
		
		BufferedImage[] spriteMap = GraphicsUtil.getSpriteArray(spritesheet, 4, 4, gamePanel.getOriginalTileSize());
		allPlayerSprites.put(player.getId(), spriteMap);
	}
	
	public void draw(Graphics2D g2) {
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
			if(p.getDirection().equals("down")) {
				image = spriteArray[0 + p.getAnimState()];
			}
			if(p.getDirection().equals("left")) {
				image = spriteArray[4 + p.getAnimState()];
			}
			if(p.getDirection().equals("right")) {
				image = spriteArray[8 + p.getAnimState()];
			}
			if(p.getDirection().equals("up")) {
				image = spriteArray[12 + p.getAnimState()];
			}
			
			int worldX = p.getPlayerX();
			int worldY = p.getPlayerY();
			int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
			int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();
			
			g2.drawImage(image, screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
		}
	}
}