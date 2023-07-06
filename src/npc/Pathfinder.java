package npc;

import java.util.ArrayList;

import graphics.TileHandler;
import main.GamePanel;

public class Pathfinder {
	GamePanel gamePanel;
	Node[][] node;
	ArrayList<Node> openList = new ArrayList<>();
	public ArrayList<Node> pathList = new ArrayList<>();
	Node startNode, goalNode, currentNode;
	boolean goalReached = false;
	int step = 0;
	
	TileHandler tileHandler = gamePanel.tileHandler;
	
	public Pathfinder(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	

}
