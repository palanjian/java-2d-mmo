package packets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class TileMap implements Serializable{

	private static final long serialVersionUID = 1L;
	transient private BufferedReader br;
	private int rows;
	private int columns;
	private int layers;
	private int[][][] mapBlueprint;
	private boolean[][] collisionMap;
	
	public TileMap(InputStream is) {
		br = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			line = br.readLine();
			String numbers[] = line.split(" ");
			this.rows = Integer.parseInt(numbers[0]); // first int is rows
			this.columns = Integer.parseInt(numbers[1]); //second is columns
			this.layers = Integer.parseInt(numbers[2]); // third is layers
			mapBlueprint = new int[layers][rows][columns];
			collisionMap = new boolean[rows][columns];
			loadMap();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void loadMap() {
		try {
			for(int layer=0; layer<layers; layer++) {
				for(int row = 0; row < rows; ++row) {
					String line = br.readLine();
					String numbers[] = line.split(" ");
					for(int col = 0; col < columns; col++) {
						mapBlueprint[layer][row][col] = Integer.parseInt(numbers[col]);
					}
				}
			}
			for(int row = 0; row < rows; ++row) {
				String line = br.readLine();
				String numbers[] = line.split(" ");
				for(int col = 0; col < columns; col++) {
					collisionMap[row][col] = getBoolean(Integer.parseInt(numbers[col]));
				}
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public boolean getBoolean(int i) {
		if(i==0) return false;
		else return true;
	}
	
	public int getRows() { return rows; }
	public int getColumns() { return columns; }
	public int getLayers() { return layers; }
	public int[][][] getMapBlueprint() { return mapBlueprint; }
	public boolean[][] getCollisionMap() { return collisionMap; }
}
