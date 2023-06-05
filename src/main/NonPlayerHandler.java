package main;

import java.io.ObjectInputStream;
import java.net.Socket;

public class NonPlayerHandler implements Runnable{
	
	private Socket socket;
	GamePanel gamePanel;
	private ObjectInputStream objectInputStream;
	NonPlayerHandler(Socket socket, GamePanel gamePanel){
		try {
			this.socket = socket;
			this.gamePanel = gamePanel;
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
				//System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
				gamePanel.pAddPlayer(playerInfo);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
