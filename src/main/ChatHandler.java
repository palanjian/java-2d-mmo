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
	private int MAX_MESSAGES = 8;
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
		int msgNumber = 1;
		for(ChatMessage msg : messageQueue) {
			String formatted = "";
			g2.setFont(new Font(FONT.getFontName(), Font.PLAIN, FONT_SIZE));
			
			//make more elegant
			switch(msg.getRecipient()) {
			case("all"):
				formatted += "[Server] " + msg.getMessage();
				g2.setColor(new Color(209, 85, 80));
				break;
			case("world"):
				formatted += "[World] " + msg.getSender() + ": " + msg.getMessage(); //can change to player.getWorldName() once worlds are implemented
				g2.setColor(Color.WHITE);
				break;
			case("guild"):
				formatted += "[Guild] " + msg.getSender() + ": " + msg.getMessage();
				g2.setColor(new Color(37, 127, 39));
				break;
			case("whisper"):
				formatted += "[Whisper] " + msg.getSender() + ": " + msg.getMessage();
				g2.setColor(new Color(207, 159, 255));;
				g2.setFont(new Font(FONT.getFontName(), Font.ITALIC, FONT_SIZE));
				break;
			}		
			
			g2.drawString(formatted, gamePanel.tileSize / 2, gamePanel.screenHeight - (gamePanel.tileSize / 2) - msgNumber*FONT_SIZE);
			++msgNumber;
		}
	}
	
	public void service(ChatMessage message) {
		if(messageQueue.size() == MAX_MESSAGES) {
			messageQueue.removeLast();
		}
		messageQueue.addFirst(message);
	}
}
