package main;

import java.awt.Graphics2D;
import java.util.ArrayDeque;

import packets.ChatMessage;

public class ChatHandler {
	private GamePanel gamePanel;
	private ArrayDeque<ChatMessage> messageQueue;
	
	private int FONT_SIZE = 30; //30px
	
	public ChatHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.messageQueue = new ArrayDeque<ChatMessage>();
	}

	public void draw(Graphics2D g2) {
		int msgNumber = 1;
		for(ChatMessage msg : messageQueue) {
			g2.drawString(msg.getMessage(), gamePanel.tileSize / 2, gamePanel.screenHeight - (gamePanel.tileSize / 2) - msgNumber*FONT_SIZE);
			++msgNumber;
		}
	}
	
	public void service(ChatMessage message) {
		if(messageQueue.size() == 6) {
			messageQueue.removeLast();
		}
		messageQueue.addFirst(message);
	}
}
