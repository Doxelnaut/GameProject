package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WARRIOR = 0;
	Player player;
	Player player2;
	ArrayList<Actor> actors;
	
	boolean[][] blocked;
	boolean[][] occupied; // for collision detection with actors
	NodeMap pathmap;
	
	GameState(){
		player = null;
		player2 = null;
		return;
	}
}
