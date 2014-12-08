package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int[] map;
	ArrayList<NetVector> bullets;
	
	GameState(){
		
		map = new int[10000];
		bullets = new ArrayList<NetVector>();
		return;
	}
	
}
