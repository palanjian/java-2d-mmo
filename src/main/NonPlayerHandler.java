package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class NonPlayerHandler implements Runnable{
	
	private Socket socket;
	GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	private Map<Integer, PlayerInfo> allPlayerInfos;
	
	NonPlayerHandler(Socket socket, GamePanel gamePanel){
		this.socket = socket;
		this.gamePanel = gamePanel;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			allPlayerInfos = new HashMap<Integer, PlayerInfo>();

		} catch (Exception e) {
			System.out.println("Could not initialize input stream. Exiting.");
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				PlayerInfo playerInfo = (PlayerInfo) objectInputStream.readObject();
				addPlayerInfo(playerInfo);
				//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
			} catch (SocketException socketException) {
				System.out.println("Lost connection to server.");
				System.exit(0);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void draw(Graphics2D g2) {
		for(PlayerInfo p : allPlayerInfos.values()) {
			g2.setColor(Color.white);
			g2.fillRect(p.getPlayerX(), p.getPlayerY(), 48, 48); //hardcoding tile size
		}
	}
	
	public void removePlayerInfo(PlayerInfo player) {
		allPlayerInfos.remove(player.getId()); //O(1)
	}
	
	public void addPlayerInfo(PlayerInfo player) {
		allPlayerInfos.put(player.getId(), player); //O(1)
	}
}
