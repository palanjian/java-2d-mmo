package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

import packets.ChatMessage;
import packets.PlayerInfo;

public class ChatHandler {
	private GamePanel gamePanel;
	private ArrayDeque<ChatMessage> messageQueue;
	
	private int FONT_SIZE = 16; //px
	private int MAX_MESSAGES = 8; //max messages on screen
	private int MSG_DISPLAY_SECONDS = 30; //how many seconds should each message be displayed for
	private Font FONT;
	
	private KeyHandler keyHandler;
	
	public ChatHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.keyHandler = gamePanel.keyHandler;
		this.messageQueue = new ArrayDeque<ChatMessage>();
		try {
			FONT = Font.createFont(Font.TRUETYPE_FONT, new File("res/" + gamePanel.getFontFileName() + ".ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(FONT);
		}
		catch(IOException | FontFormatException e) {
			System.out.println("Error loading chat font.");
			System.exit(0);
		}
	}

	public void draw(Graphics2D g2) {
		//drawing the chat words
		g2.setFont(new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
		g2.setColor(Color.WHITE);
		
		//drawing the player's typed message
		int msgNumber = 1; // #1 is the chat input field
		if(gamePanel.getGameState() == gamePanel.typingState) {
			g2.drawString("> " + keyHandler.getCharStream() + "|", gamePanel.tileSize / 2, gamePanel.screenHeight - (gamePanel.tileSize / 2) - msgNumber*FONT_SIZE);
			++msgNumber;
		}
		
		//for drawing others's messages
		for(ChatMessage msg : messageQueue) {
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
			ChatMessage chatMessage = new ChatMessage(keyHandler.getCharStream(), String.valueOf(playerInfo.getId())); // change to username
			requestsHandler.sendObject(chatMessage);
		}
	}
}
