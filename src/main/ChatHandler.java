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

public class ChatHandler {
	private GamePanel gamePanel;
	private ArrayDeque<ChatMessage> messageQueue;
	
	private int FONT_SIZE = 16; //px
	private int MAX_MESSAGES = 8; //max messages on screen
	private int MSG_DISPLAY_SECONDS = 20; //how many minutes should each message be displayed for
	private Font FONT;
	
	public ChatHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
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
		g2.setFont(new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
		g2.setColor(Color.WHITE);
		
		int msgNumber = 1;
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
}
