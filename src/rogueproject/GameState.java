package rogueproject;

import java.util.ArrayList;

public class GameState {
	public static final int WARRIOR = 0;
	Player player;
	ArrayList<Actor> actors;
	boolean[][] blocked;
	boolean[][] occupied; // for collision detection with actors
	NodeMap pathmap;
	
	GameState(){
		return;
	}
}
