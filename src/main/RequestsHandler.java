package main;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import graphics.NonPlayerGraphicsHandler;
import graphics.TileHandler;
import packets.ChatMessage;
import packets.EntityInfo;
import packets.TileMap;

public class RequestsHandler implements Runnable{
	
	private Socket socket;
	private GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private NonPlayerGraphicsHandler nonPlayerGraphicsHandler;
	private TileHandler tileHandler;
	private ChatHandler chatHandler;
	
	RequestsHandler(Socket socket, GamePanel gamePanel){
		this.socket = socket;
		this.gamePanel = gamePanel;
		this.tileHandler = gamePanel.tileHandler;
		this.chatHandler = gamePanel.chatHandler;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			nonPlayerGraphicsHandler = new NonPlayerGraphicsHandler(gamePanel);
		} catch (Exception e) {
			System.out.println("Could not initialize input stream. Exiting.");
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Object o = objectInputStream.readObject();

				//if object is TileMap
				if(o instanceof TileMap tileMap) {
					System.out.println("Recieved new tilemap.");

					//if they recieve a new map, that means theyre going to a new world. remove all the infos
					nonPlayerGraphicsHandler.removeAllInfos();

					tileHandler.service(tileMap);
				}

				//AWFUL CODE
				else if (o instanceof EntityInfo entityInfo) {
					if(entityInfo.getId() == gamePanel.player.playerId){
						gamePanel.player.getPlayerInfo().updatePosition(entityInfo.getWorldX(), entityInfo.getWorldY(), entityInfo.getDirection(), entityInfo.getSpriteNumber());
						gamePanel.player.worldX = entityInfo.getWorldX();
						gamePanel.player.worldY = entityInfo.getWorldY();
						continue;
					}
					else if(entityInfo.getId() == gamePanel.player.playerId + 5000){
						gamePanel.player.pet.worldX = entityInfo.getWorldX();
						gamePanel.player.pet.worldY = entityInfo.getWorldY();
						continue;
					}
					nonPlayerGraphicsHandler.service(entityInfo);
				}
				//if object is ChatMessage
				else if(o instanceof ChatMessage chatMessage) {
					chatHandler.service(chatMessage);
				}
				
		        int availableBytes = objectInputStream.available();
		        if (availableBytes > 0) {
		            objectInputStream.skipBytes(availableBytes);
		        }
		        
			} catch (SocketException socketException) {
				System.out.println("Lost connection to server.");
				System.exit(0);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void sendObject(Object o) {
		try {
			objectOutputStream.writeUnshared(o);
			objectOutputStream.flush();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public void draw(Graphics2D g2) {
		tileHandler.draw(g2);
		nonPlayerGraphicsHandler.draw(g2);
		chatHandler.draw(g2);
	}
}
