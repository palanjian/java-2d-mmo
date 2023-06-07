package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import graphics.SpriteHandler;

public class NonPlayerHandler implements Runnable{
	
	private Socket socket;
	GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	private Map<Integer, Map<Integer, BufferedImage>> allPlayerSprites;
	
	NonPlayerHandler(Socket socket, GamePanel gamePanel){
		this.socket = socket;
		this.gamePanel = gamePanel;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			allPlayerInfos = new HashMap<Integer, PlayerInfo>();
			allPlayerSprites = new HashMap<>();
		} catch (Exception e) {
			System.out.println("Could not initialize input stream. Exiting.");
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				//if the packet designates the player as no longer online
				PlayerInfo playerInfo = (PlayerInfo) objectInputStream.readObject();
				if(playerInfo.getOnline() == false) {
					//when a player disconnects
					allPlayerSprites.remove(playerInfo.getId());
					allPlayerInfos.remove(playerInfo.getId());
				}
				else {
					byte[] spritesheet = playerInfo.getSpritesheet();
					if(spritesheet == null) addPlayerInfo(playerInfo);
					else {
						addPlayerInfo(playerInfo);
						addPlayerSprite(playerInfo);					
					}
					//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
				}
			} catch (SocketException socketException) {
				System.out.println("Lost connection to server.");
				System.exit(0);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}

	public void draw(Graphics2D g2) {
		
		for(PlayerInfo p : allPlayerInfos.values()) {
			Map<Integer, BufferedImage> spriteMap;
			BufferedImage image;
			try {
				spriteMap = allPlayerSprites.get(p.getId());
				image = spriteMap.get(0); //default value
			}
			catch(Exception e) {
				break;
			}
			if(p.getDirection().equals("down")) {
				image = spriteMap.get(0 + p.getAnimState());
			}
			if(p.getDirection().equals("left")) {
				image = spriteMap.get(4 + p.getAnimState());
			}
			if(p.getDirection().equals("right")) {
				image = spriteMap.get(8 + p.getAnimState());
			}
			if(p.getDirection().equals("up")) {
				image = spriteMap.get(12 + p.getAnimState());
			}
			g2.drawImage(image, p.getPlayerX(), p.getPlayerY(), gamePanel.getTileSize(), gamePanel.getTileSize(), null);
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
		
		Map<Integer, BufferedImage> spriteMap = SpriteHandler.getSpriteMap(spritesheet, 4, 4, gamePanel.getOriginalTileSize());
		allPlayerSprites.put(player.getId(), spriteMap);
	}
	
}
