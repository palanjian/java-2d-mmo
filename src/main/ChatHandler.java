package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import packets.ChatMessage;
import packets.PlayerInfo;

public class ChatHandler {
	private GamePanel gamePanel;
	private ArrayDeque<ChatMessage> messageQueue;
	
	private int MAX_MESSAGES = 8; //max messages on screen
	private int MSG_DISPLAY_SECONDS = 30; //how many seconds should each message be displayed for
	private Font FONT;
	private int FONT_SIZE;
	
	private KeyHandler keyHandler;
	
	public ChatHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.keyHandler = gamePanel.keyHandler;
		this.messageQueue = new ArrayDeque<ChatMessage>();
		this.FONT = gamePanel.font;
		this.FONT_SIZE = gamePanel.fontSize;

	}

	public void draw(Graphics2D g2) {
		//drawing the chat words
		g2.setFont(new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
		g2.setColor(Color.WHITE);
		//drawing the player's typed message
		int msgNumber = 1; // #1 is the chat input field
		if(gamePanel.getGameState() == GameState.Typing) {
			g2.drawString("> " + keyHandler.getCharStream() + "|", gamePanel.tileSize / 2, gamePanel.screenHeight - (gamePanel.tileSize / 2) - msgNumber*FONT_SIZE);
			++msgNumber;
		}
		
		//for drawing others's messages
		for(ChatMessage msg : messageQueue) {
			if(msg.getSender().equals("Server")) g2.setColor(Color.ORANGE);
			else g2.setColor(Color.WHITE);
			
			if(System.currentTimeMillis() - (MSG_DISPLAY_SECONDS * 1000) > msg.getTimeRecieved()) {
				messageQueue.remove(msg);
			}
			
			else {
				
				String formatted = msg.getSender() + ": " + msg.getMessage();
				g2.drawString(formatted, gamePanel.tileSize / 2, gamePanel.screenHeight - (gamePanel.tileSize / 2) - msgNumber*FONT_SIZE);
				++msgNumber;	
			}
		}
	}
	
	public void service(ChatMessage message) {
		if(messageQueue.size() == MAX_MESSAGES) {
			messageQueue.removeLast();
		}
		message.setTimeRecieved(System.currentTimeMillis());
		messageQueue.addFirst(message);
	}
	
	public void sendMessage() {
		RequestsHandler requestsHandler = gamePanel.requestsHandler;
		if(!keyHandler.getCharStream().strip().equals("")) {
			PlayerInfo playerInfo = gamePanel.player.getPlayerInfo();
			ChatMessage chatMessage = new ChatMessage(keyHandler.getCharStream(), playerInfo.getUsername()); // change to username
			requestsHandler.sendObject(chatMessage);
		}
	}
}
