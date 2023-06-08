package main;

import java.awt.Graphics2D;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import graphics.NonPlayerGraphicsHandler;
import graphics.TileHandler;
import packets.ChatMessage;
import packets.PlayerInfo;
import packets.TileMap;

public class RequestsHandler implements Runnable{
	
	private Socket socket;
	GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	private NonPlayerGraphicsHandler nonPlayerGraphicsHandler;
	private TileHandler tileHandler;
	
	RequestsHandler(Socket socket, GamePanel gamePanel){
		this.socket = socket;
		this.gamePanel = gamePanel;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			nonPlayerGraphicsHandler = new NonPlayerGraphicsHandler(gamePanel);
			tileHandler = new TileHandler(gamePanel);
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
				
				//if object is PlayerInfo
				if (o instanceof PlayerInfo) {
				    PlayerInfo playerInfo = (PlayerInfo)o;
				    //System.out.println(((PlayerInfo) o).getId());
				    nonPlayerGraphicsHandler.service(playerInfo);
				}
				
				//if object is TileMap
				else if(o instanceof TileMap) {
					System.out.println("Recieved new tilemap.");
					TileMap tileMap = (TileMap)o; 
					tileHandler.service(tileMap);
				}
				//if object is ChatMessage
				else if(o instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)o;
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
	
	public void draw(Graphics2D g2) {
		tileHandler.draw(g2);
		nonPlayerGraphicsHandler.draw(g2);
	}
}
