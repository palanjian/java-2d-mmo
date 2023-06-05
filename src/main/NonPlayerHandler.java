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
				//create a painthandler in gamepanel, pass to this class, shouldnt be a thread
				//if the player does not already exist in the vector of players in painthandler add it to vector
				//if player disconnects, remove from thread
				//if the player does exist, send the new location the thread
				//run a for loop on the painthandler that iterates through every player and paints them 
				
				//google if theres anything wrong w creating static classes
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
