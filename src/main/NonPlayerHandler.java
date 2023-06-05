package main;

import java.io.ObjectInputStream;
import java.net.Socket;

public class NonPlayerHandler implements Runnable{
	
	private Socket socket;
	GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	
	NonPlayerHandler(Socket socket, GamePanel gamePanel){
		this.socket = socket;
		this.gamePanel = gamePanel;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
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
				gamePanel.pAddPlayer(playerInfo);
				//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
			} catch(Exception e) { e.printStackTrace(); }
		}
	}
}
