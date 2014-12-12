package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//int[][] map;
	NetVector player;
	NetVector player2;
	boolean secondPlayer = false;
	boolean firstPlayer = false;
	ArrayList<NetVector> bullets = new ArrayList<NetVector>();
	
	public GameState(){
		
		//map = new int[100][100];
		player = new NetVector();
		player2 = new NetVector();
		return;
	}
	
	/*public String toString(){
		return "GameState: [ bullets =" + player + "]"; 
	}*/
	
}
