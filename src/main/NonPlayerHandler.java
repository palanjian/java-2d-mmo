package main;

import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JPanel;

public class NonPlayerHandler extends JPanel implements Runnable{
	
	private Socket socket;
	private ObjectInputStream objectInputStream;
	NonPlayerHandler(Socket socket){
		try {
			this.socket = socket;
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
				System.out.println(playerInfo.username + "'s position: X=" + playerInfo.playerX + " Y=" + playerInfo.playerY);
				//if the player does not already exist in the vector of players, make thread, add to vector of threads, and start
				//if player disconnects, remove from thread
				//if the player does exist, send the new location the thread
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
