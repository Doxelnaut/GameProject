package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int[] map;
	NetVector player;
	NetVector player2;
	boolean secondPlayer = false;
	boolean firstPlayer = false;
	int playerDirection = 0;
	int player2Direction = 0;
	boolean playerCrouch = false;
	boolean player2Crouch = false;

	
	public GameState(){
		
		map = new int[10000];
		player = new NetVector();
		player2 = new NetVector();
		return;
	}
	
	/*public String toString(){
		return "GameState: [ bullets =" + player + "]"; 
	}*/
	
}
