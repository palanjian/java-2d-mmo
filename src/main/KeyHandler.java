package main;

import enums.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	private GamePanel gamePanel;
	private String charStream;
	
	public KeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.charStream = "";
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		//special cases
		switch(gamePanel.getGameState()) {
			case Playing:
				playingKeyPressed(code);
				break;
			case Typing:
				typingKeyPressed(code);
				break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_W) upPressed = false;
		if(code == KeyEvent.VK_S) downPressed = false;
		if(code == KeyEvent.VK_A) leftPressed = false;
		if(code == KeyEvent.VK_D) rightPressed = false;
	}
	
	public void playingKeyPressed(int code) {
		if(code == KeyEvent.VK_W) upPressed = true;
		if(code == KeyEvent.VK_S) downPressed = true;
		if(code == KeyEvent.VK_A) leftPressed = true;
		if(code == KeyEvent.VK_D) rightPressed = true;

		if(code == KeyEvent.VK_ENTER) {
			gamePanel.setGameState(GameState.Typing);
		}
	}
	
	public void typingKeyPressed(int code) {
		if(code == KeyEvent.VK_ENTER) {
			gamePanel.chatHandler.sendMessage();
			gamePanel.setGameState(GameState.Playing);
			charStream = "";
		}
		if(code == KeyEvent.VK_ESCAPE) {
			gamePanel.setGameState(GameState.Playing);
			charStream = "";
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(gamePanel.getGameState() == GameState.Typing) {
			char code = e.getKeyChar();
			if(code >= 32 && code <= 126 && charStream.length() <= 48) {
				//if code is between ascii values of space and ~ (no white spaces)
				charStream += code;
			}
			else if(code == KeyEvent.VK_BACK_SPACE && charStream.length() > 0) charStream = charStream.substring(0, charStream.length()-1);
		}
	}
	
	public String getCharStream() {return charStream; }
	public void setCharStream(String charStream) { this.charStream = charStream; }
}