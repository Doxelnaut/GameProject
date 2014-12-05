package rogueproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 *	This file is part of El Rogue del Rey.
 *
 *  El Rogue del Rey is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  El Rogue del Rey is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with El Rogue del Rey.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Zacharias Shufflebarger
 */
public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	Socket socket;
	String servName;
	int port = 1666;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;
	
	// input direction
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	
	// collective boolean for of all actors turns
	public boolean actorsTurns = false; 

	RogueGame rg;
	@Override
	public void init(GameContainer container, StateBasedGame game)throws SlickException {
	
}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		
		//display prompt for server address
		
				servName = "127.0.0.1";
				
				try {
					socket = new Socket(servName,port);
				} catch (IOException e) {
					System.out.println("Error creating client socket.");
					e.printStackTrace();
				}
				
				//open object streams
			    try {
			      socketOut = new ObjectOutputStream(socket.getOutputStream());
			      socketIn = new ObjectInputStream(socket.getInputStream());
			    }
			    catch(IOException e){
					e.printStackTrace();
					System.out.println("Error opening streams");
			    }
			    
		
		RogueGame rg = (RogueGame)game;
		rg.state.actors = new ArrayList<IsoEntity>();
		setLevel(rg);
		
		rg.state.blocked = new boolean[map.getWidth()][map.getHeight()];
		rg.state.occupied = new boolean[map.getWidth()][map.getHeight()]; 
		rg.state.pathmap = new NodeMap(map);
				
		// Build collision detection for map tiles, and fill occupied with false values.
		for (int i = 0; i < map.getWidth(); i++){
			for (int j = 0; j < map.getHeight(); j++){
				rg.state.occupied[i][j] = false; // initialize occupied
				int tileID = map.getTileId(i, j, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					rg.state.blocked[i][j] = true;
				}
			}
		}
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
							GameState.walls.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), true) );
							GameState.stop.add(new Ground(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
				    	}
				    	else if(Integer.valueOf(parts[i]) == 2){
				    		GameState.blocks.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), false) );
				    		GameState.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
				    	}
				    	else{
				    		GameState.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
				    	}

				    }
				    	r++;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			GameState.minotaur = new Minotaur(RogueGame.WORLD_SIZE, new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE),GameState.WARRIOR);
		   System.out.println( GameState.minotaur);
		   GameState.blocks.add(GameState.minotaur);
		
		for (IsoEntity ie : GameState.walls) {
			GameState.wallsandblocks.add(ie);
		}
		for (IsoEntity ie : GameState.blocks) {
			GameState.wallsandblocks.add(ie);
		}

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		
		
		if(GameState.minotaur == null){ // player died, don't render anything.
			rg.enterState(RogueGame.STARTUPSTATE);
		}
		
		
		
		g.translate(-RogueGame.camX, -RogueGame.camY);		

		for (IsoEntity ie : GameState.ground) {
			ie.render(g);
		}
		
		Collections.sort(GameState.wallsandblocks);
		for (IsoEntity ie : GameState.wallsandblocks) {
			ie.render(g);
		}
		if (GameState.fireball != null) GameState.fireball.render(g);
		g.translate(RogueGame.camX, RogueGame.camY);		

		if(GameState.minotaur != null){
			g.drawString("Level: " + (int)GameState.minotaur.getLevel() + 
					" Health: " + (int)GameState.minotaur.getHitPoints() + 
					"/" + (int)GameState.minotaur.getMaxHitPoints() +  
					" Attack: " + (int)GameState.minotaur.getAttack() + 
					" Armor: " + (int)GameState.minotaur.getArmor() + 
					" Experience: " + (int)GameState.minotaur.getExperience()
					, 100 , 10);
			g.drawString("Dungeon Level: " + rg.player.getDepth(), 100, 25);
			g.drawString("Dungeon Level: " + GameState.minotaur.getDepth(), 100, 25);
		}
		g.translate(-RogueGame.camX, -RogueGame.camY);		

		
	}
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		float x = (60*delta/1000.0f);
		
		RogueGame.playerX = GameState.minotaur.getX();
		RogueGame.playerY = GameState.minotaur.getY();
		RogueGame.camX = RogueGame.playerX - RogueGame.VIEWPORT_SIZE_X / 2;
		RogueGame.camY = RogueGame.playerY - RogueGame.VIEWPORT_SIZE_Y / 2;

		InputHandler inputHandler = new InputHandler();
		ArrayList<Command> commands = inputHandler.handleInput(input);
		if(commands.size() > 0){
			for(Command c : commands){
				try {
					socketOut.writeObject(c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(c);
				
				c.execute(GameState.minotaur,x);

			}
		}
		else GameState.minotaur.halt();
				
		if (input.isKeyPressed(Input.KEY_LCONTROL)) {
			GameState.minotaur.debugThis = !GameState.minotaur.debugThis;
			if (GameState.fireball != null) GameState.fireball.debugThis = GameState.minotaur.debugThis;
		}
		
		for(IsoEntity b : GameState.blocks) {
			if (b != GameState.minotaur && b.collides(GameState.minotaur) != null) {
				System.out.println("Ouch!");
				//minotaur.sayOuch();
				GameState.minotaur.halt();
				GameState.minotaur.ungo();
			}
		}
		
/* 		if(rg.state.actors.size()==0){
			rg.state.player.setDepth(rg.state.player.getDepth() + 1);
			rg.enterState(RogueGame.PLAYINGSTATE);
		}
*/		
//		Input input = container.getInput();
/*		// The player's turn 
		if(rg.state.player != null){
			if(rg.state.player.getTurn()){
				if(!rg.state.player.getGained()){
					rg.state.player.gainEnergy();
					rg.state.player.setGained(true);
				}
*///			if(!rg.state.player.isMoving()){ // handle all user input in this block
					// Directional Keys
					//   Q   W   E
					//    \  |  /
					// A - restS - D
					//    /  |  \
					//   Z   X   C
/**					if 		(input.isKeyDown(Input.KEY_W)) 	{
						//rg.state.player.setOrders(N);
						mapy -= 8;
						mapx += 16;
					} 		// North
					else if (input.isKeyDown(Input.KEY_X)) 	{
						//rg.state.player.setOrders(S);
						mapy += 8;
						mapx -= 16;

						} 		// South
					else if (input.isKeyDown(Input.KEY_A)) 	{
						//rg.state.player.setOrders(W);
						mapx += 16;
						mapy += 8;
						} 		// West
					else if (input.isKeyDown(Input.KEY_D)) 	{
						//rg.state.player.setOrders(E);
						mapx -= 16;
						mapy -= 8;
					} 		// East
					else if (input.isKeyDown(Input.KEY_Q)) 	{
						//rg.state.player.setOrders(NW);
						mapy -= 8;
						} 		// Northwest
					else if (input.isKeyDown(Input.KEY_E)) 	{
						//rg.state.player.setOrders(NE);
						mapx -= 8;
						} 		// Northeast
					else if (input.isKeyDown(Input.KEY_Z)) 	{
						//rg.state.player.setOrders(SW);
						mapx += 8;
						} 		// Southwest
					else if (input.isKeyDown(Input.KEY_C)) 	{
						//rg.state.player.setOrders(SE);
						mapy += 8;
						} 		// Southeast
					else if (input.isKeyDown(Input.KEY_S)) 	{rg.state.player.setOrders(REST);} 	// Rest
					else if (input.isKeyPressed(Input.KEY_ESCAPE)) {container.exit();}
**/					// Cheats:
					/* hard code modulo for the number of dungeons actually playable, 
					 * then add one, since dungeons start at level 1.
					 */
					if (input.isKeyPressed(Input.KEY_I))	{
						rg.player.setDepth(1);
						map = null;
						//rg.state.enterState(RogueGame.PLAYINGSTATE);
					}
					else if (input.isKeyPressed(Input.KEY_O)) { 
						rg.player.setDepth(2); 
						map = null;
						//rg.state.enterState(RogueGame.PLAYINGSTATE);
						}
					rg.state.player.act(rg);
					if(!rg.state.player.getTurn()){ // not player's turn after taking action
						// set actors' turns
						for(IsoEntity a : rg.state.actors){
							a.setTurn(true);
							a.setGained(false);
						}
					}
//				}
//			}
			// update player position
			if(rg.state.player.isMoving()){
				if(rg.state.player.getPosition().equals(rg.state.player.getNextTile().scale(RogueGame.TILE_SIZE))){
					//player reached destination.
					rg.state.player.setMoving(false);
					// end player's turn and begin actors' turns
					rg.state.player.setTurn(false);
		if (GameState.fireball != null) {
			GameState.fireball.update(x*1.5f);
			IsoEntity other;
			for (Iterator<IsoEntity> iie = GameState.blocks.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == GameState.minotaur) continue;
				if (GameState.fireball.collides(other) != null) {
					System.out.println("true");
					GameState.fireball.kaboom();
					iie.remove();
					GameState.wallsandblocks.clear();
					
					for (IsoEntity ie : GameState.walls) {
						GameState.wallsandblocks.add(ie);
					}
					for (IsoEntity ie : GameState.blocks) {
						GameState.wallsandblocks.add(ie);
					}
					
					break;
				}
			}
			for (Iterator<IsoEntity> iie = GameState.walls.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == GameState.minotaur) continue;
				if (GameState.fireball.collides(other) != null) {
					System.out.println("true");
					GameState.fireball.kaboom();
					break;
				}
			}
			if (GameState.fireball.done()) GameState.fireball = null;
		}
				}
			}
	}

	@Override
	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
	public void setLevel(RogueGame rg) throws SlickException{

		switch(GameState.minotaur.getDepth()){
		case 1:
			//rg.state.player.setTilePosition(1, 2);
			//map = new TiledMap("rogueproject/resource/maps/tinytestmap.tmx");
/**			//Little Zombies
			rg.state.actors.add( new Actor(0, 14, 15));
			rg.state.actors.add( new Actor(0, 10, 16));
			rg.state.actors.add( new Actor(0, 12, 28));
			rg.state.actors.add( new Actor(0, 6, 29));
			rg.state.actors.add( new Actor(0, 24, 38));
			rg.state.actors.add( new Actor(0, 25, 29));
			rg.state.actors.add( new Actor(0, 23, 7));
			rg.state.actors.add( new Actor(0, 40, 19));
			rg.state.actors.add( new Actor(0, 24, 37));
			rg.state.actors.add( new Actor(0, 26, 12));
			//Little Mummies
			rg.state.actors.add( new Actor(1, 31, 22));
			rg.state.actors.add( new Actor(1, 25, 21));
			rg.state.actors.add( new Actor(1, 23, 40));
			//Skeletons
			rg.state.actors.add( new Actor(2, 9, 30));
			rg.state.actors.add( new Actor(2, 45, 24));
			rg.state.actors.add( new Actor(2, 49, 26));
			rg.state.actors.add( new Actor(2, 37, 36));
			rg.state.actors.add( new Actor(2, 47, 38));
			//Large Zombies
			rg.state.actors.add( new Actor(3, 41, 9));
			rg.state.actors.add( new Actor(3, 43, 9));
			rg.state.actors.add( new Actor(3, 34, 36));
			rg.state.actors.add( new Actor(3, 34, 30));
			rg.state.actors.add( new Actor(3, 48, 18));
			rg.state.actors.add( new Actor(3, 56, 19));
			//Large Mummies
			rg.state.actors.add( new Actor(4, 55, 25));
			rg.state.actors.add( new Actor(4, 56, 24));
			rg.state.actors.add( new Actor(4, 54, 13));
			//Death
			rg.state.actors.add( new Actor(5, 48, 4)); 
**/			break;
		case 2:
			rg.state.player.setTilePosition(3,3);
			rg.state.player.setTilePosition(0,1);
			map = new TiledMap("rogueproject/resource/maps/tinytestmap.tmx");
		//	rg.state.player.setTilePosition(0,1);
			//map = new TiledMap("rogueproject/resource/maps/tinytestmap.tmx");
/**			// Little Spiders
			rg.state.actors.add( new Actor(6, 12, 19));
			rg.state.actors.add( new Actor(6, 7, 31));
			rg.state.actors.add( new Actor(6, 13, 34));
			rg.state.actors.add( new Actor(6, 6, 6));
			rg.state.actors.add( new Actor(6, 27, 16));
			rg.state.actors.add( new Actor(6, 37, 21));
			rg.state.actors.add( new Actor(6, 39, 31));
			rg.state.actors.add( new Actor(6, 51, 34));
			rg.state.actors.add( new Actor(6, 28, 5));
			rg.state.actors.add( new Actor(6, 59, 16));
			// Little Scorpions
			rg.state.actors.add( new Actor(7, 39, 29));
			rg.state.actors.add( new Actor(7, 34, 41));
			rg.state.actors.add( new Actor(7, 41, 29));
			rg.state.actors.add( new Actor(7, 34, 14));
			rg.state.actors.add( new Actor(7, 6, 5));
			// Slugs
			rg.state.actors.add( new Actor(8, 27, 39));
			rg.state.actors.add( new Actor(8, 37, 41));
			rg.state.actors.add( new Actor(8, 37, 42));
			// Large Spiders
			rg.state.actors.add( new Actor(9, 48, 10));
			rg.state.actors.add( new Actor(9, 22, 5));
			rg.state.actors.add( new Actor(9, 55, 19));
			// Large Scorpions
			rg.state.actors.add( new Actor(10, 46, 8));
			rg.state.actors.add( new Actor(10, 25, 6));
			rg.state.actors.add( new Actor(10, 54, 33));
			// Red Leech
			rg.state.actors.add( new Actor(11, 48, 8));
**/			break;
		default:
			break;
		}
	}

}
