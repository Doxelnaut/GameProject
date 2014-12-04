package rogueproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import jig.Entity;
import jig.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * IsoWorld Demo with:
 * 
 * - ground level tiles
 * - tall and short obstacles (currently no collision detection)
 * - a roaming minotaur (user controllable)
 * 
 * the ground is drawn first, then all tiles with a vertical extend
 * are drawn in sorted order.
 * 
 *
 */
public class IsoWorldGame extends BasicGame {

	public static final String groundSheetPath   = "resource/flagstonetiles.png";
	public static final String tallBlockImgPath   = "resource/tallcaveblock.png";
	public static final String shortBlockImgPath   = "resource/shortcaveblock.png";
	public static final String WalkLeft = "resource/Walk_10(15,69,108).png";
	public static final String WalkRight = "resource/Walk_2(15,62,111).png";
	public static final String WalkUp = "resource/Walk_6(15,66,111).png";
	public static final String WalkDown = "resource/Walk_14(15,61,108).png";
	public static final String WalkUpLeft = "resource/Walk_8(15,68,110).png";
	public static final String WalkUpRight = "resource/Walk_4(15,63,112).png";
	public static final String WalkDownLeft = "resource/Walk_12(15,63,108).png";
	public static final String WalkDownRight = "resource/Walk_0(15,63,110).png";
	public static final String fireballSheetPath = "resource/fireball.png";
	public static final String explosionSheetPath   = "resource/explosion.png";
	public static final String ouchSoundPath = "resource/ouch.wav";
	
	public static int tileSize = 16;
	public static float playerX = 0;
	public static float playerY = 0;
	public static float WORLD_SIZE_X = (tileSize * 100);
	public static float WORLD_SIZE_Y = (tileSize * 100);
	
	public static float VIEWPORT_SIZE_X = 1024;
	public static float VIEWPORT_SIZE_Y = 720;
	
	public static float camX = 0;
	public static float	camY = 0;
	
	public static float offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
	public static float offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
	public static float offsetMinX = 0;
    public static float offsetMinY = 0;
	private final int screenWidth;
	private final int screenHeight;

	private ArrayList<IsoEntity> ground;
	private ArrayList<IsoEntity> blocks;
	private ArrayList<IsoEntity> walls;
	private ArrayList<IsoEntity> wallsandblocks;
	private ArrayList<IsoEntity> stop;
	private Minotaur minotaur;
	private Fireball fireball;

	private Vector worldSize = new Vector(WORLD_SIZE_X, WORLD_SIZE_Y);
		
	
	
	public IsoWorldGame(int w, int h) {
		super("IsoWorldDemo");
		screenWidth = w;
		screenHeight = h;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ground = new ArrayList<IsoEntity>(100);
		blocks = new ArrayList<IsoEntity>(100);
		walls = new ArrayList<IsoEntity>(100);
		wallsandblocks = new ArrayList<IsoEntity>(200);
		stop = new ArrayList<IsoEntity>(100);
		minotaur = new Minotaur(worldSize, new Vector(2*tileSize, 2*tileSize),GameState.WARRIOR);
		@SuppressWarnings("resource")
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/resource/Map.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	        String line = null;
	        
	        int r = 0;
		    try {
				while ((line = reader.readLine()) != null) {

				    String[] parts = line.split("\\s");
				    System.out.println(parts.length);
				    for(int i = 0; i < parts.length;i++){
				    	if(Integer.valueOf(parts[i]) == 1){
							walls.add(new Block(worldSize,new Vector(r*tileSize, i*tileSize), true) );
							stop.add(new Ground(worldSize,new Vector(r*tileSize, i*tileSize)) );
				    	}
				    	else if(Integer.valueOf(parts[i]) == 2){
							blocks.add(new Block(worldSize,new Vector(r*tileSize, i*tileSize), false) );
							ground.add(new Ground(worldSize, new Vector(r*tileSize, i*tileSize)) );
				    	}
				    	else{
				    		ground.add(new Ground(worldSize, new Vector(r*tileSize, i*tileSize)) );
				    	}

				    }
				    	r++;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		     
		blocks.add(minotaur);
		
		for (IsoEntity ie : walls) {
			wallsandblocks.add(ie);
		}
		for (IsoEntity ie : blocks) {
			wallsandblocks.add(ie);
		}

	}
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
	    g.translate(-camX, -camY);		

		for (IsoEntity ie : ground) {
			ie.render(g);
		}
		
		Collections.sort(wallsandblocks);
		for (IsoEntity ie : wallsandblocks) {
			ie.render(g);
		}
		if (fireball != null) fireball.render(g);

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();
		float x = (60*delta/1000.0f);
		playerX = minotaur.getX();
		playerY = minotaur.getY();
		camX = playerX - VIEWPORT_SIZE_X / 2;
		camY = playerY - VIEWPORT_SIZE_Y / 2;

		//System.out.println(canMove());
		if (input.isKeyDown(Input.KEY_LEFT) && input.isKeyDown(Input.KEY_UP)){
			minotaur.go(Minotaur.UP, x,Minotaur.UpLEFT); 
			if(canMove()){
				minotaur.go(Minotaur.LEFT, x,Minotaur.UpLEFT); 
				if(!canMove()){
					minotaur.halt();
					minotaur.ungo();
				}

			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_LEFT) && input.isKeyDown(Input.KEY_DOWN)){
			minotaur.go(Minotaur.DOWN, x,Minotaur.DownLEFT); 
			if(canMove()){
				minotaur.go(Minotaur.LEFT, x,Minotaur.DownLEFT); 
				if(!canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_UP)){
			minotaur.go(Minotaur.UP, x,Minotaur.UpRIGHT); 
			if(canMove()){
				minotaur.go(Minotaur.RIGHT, x,Minotaur.UpRIGHT); 
				if(!canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN)){
			minotaur.go(Minotaur.DOWN, x,Minotaur.DownRIGHT);
			if(canMove()){
				minotaur.go(Minotaur.RIGHT, x,Minotaur.DownRIGHT);
				if(!canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			minotaur.go(Minotaur.LEFT, x,Minotaur.LEFT);
			if(!canMove()){
				minotaur.halt();
				minotaur.ungo();
			}

		}
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			minotaur.go(Minotaur.RIGHT, x,Minotaur.RIGHT);
			if(!canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_UP)) {
			minotaur.go(Minotaur.UP, x,Minotaur.UP); 
			if(!canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			minotaur.go(Minotaur.DOWN, x,Minotaur.DOWN);
			if(!canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		
		else minotaur.halt();
		

	
		
		if (input.isKeyPressed(Input.KEY_LCONTROL)) {
			minotaur.debugThis = !minotaur.debugThis;
			if (fireball != null) fireball.debugThis = minotaur.debugThis;
		}
		
		for(IsoEntity b : blocks) {
			if (b != minotaur && b.collides(minotaur) != null) {
				System.out.println("Ouch!");
				minotaur.sayOuch();
				minotaur.halt();
				minotaur.ungo();
			}
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			fireball = minotaur.launchFireball();
		}
		if (fireball != null) {
			fireball.update(x*1.5f);
			IsoEntity other;
			for (Iterator<IsoEntity> iie = blocks.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == minotaur) continue;
				if (fireball.collides(other) != null) {
					System.out.println("true");
					fireball.kaboom();
					iie.remove();
					wallsandblocks.clear();
					
					for (IsoEntity ie : walls) {
						wallsandblocks.add(ie);
					}
					for (IsoEntity ie : blocks) {
						wallsandblocks.add(ie);
					}
					
					break;
				}
			}
			for (Iterator<IsoEntity> iie = walls.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == minotaur) continue;
				if (fireball.collides(other) != null) {
					System.out.println("true");
					fireball.kaboom();
					break;
				}
			}
			if (fireball.done()) fireball = null;
		}
		
		
	}
	public boolean canMove(){
		IsoEntity other;
		for (Iterator<IsoEntity> iie = stop.iterator(); iie.hasNext(); ) {
			other = iie.next();
			
			if (minotaur.collides(other) != null) {
				return false;
			}
			
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        AppGameContainer app;
        IsoWorldGame iwg;
		try {
			iwg = new IsoWorldGame((int)VIEWPORT_SIZE_X, (int)VIEWPORT_SIZE_Y);
			app = new AppGameContainer(iwg);
			app.setDisplayMode(iwg.screenWidth, iwg.screenHeight, false);
	        app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	

	
}
