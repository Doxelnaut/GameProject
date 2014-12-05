package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WARRIOR = 0;
	
	boolean[][] blocked;
	boolean[][] occupied; // for collision detection with actors
	NodeMap pathmap;
	public static Player player;
	public static Player player2;
	ArrayList<IsoEntity> actors;
	public static ArrayList<IsoEntity> ground;
	public static ArrayList<IsoEntity> blocks;
	public static ArrayList<IsoEntity> walls;
	public static ArrayList<IsoEntity> wallsandblocks;
	public static ArrayList<IsoEntity> stop;
	public static Fireball fireball;
	
	GameState(){
		player = null;
		player2 = null;
		ground = new ArrayList<IsoEntity>(100);
		blocks = new ArrayList<IsoEntity>(100);
		walls = new ArrayList<IsoEntity>(100);
		wallsandblocks = new ArrayList<IsoEntity>(200);
		stop = new ArrayList<IsoEntity>(100);
		return;
	}
}
