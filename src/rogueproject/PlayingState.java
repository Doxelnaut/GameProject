package rogueproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
	

	Socket socket;                 //socket for connection to server
	int port = 1666;				
	ObjectOutputStream socketOut;	//object writer
	ObjectInputStream socketIn;		//object reader
	Player player;
	boolean secondPlayer = false;
//	TiledMap map;
//	boolean[][] blocked;
	float oldPosX;
	float oldPosY;
	
	// input direction
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	
	// collective boolean for of all actors turns
	public boolean actorsTurns = false; 

	//copy of RogueGame
	RogueGame RG;
	
	
	public void init(GameContainer container, StateBasedGame game)throws SlickException {
	
}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {
		
		//cast game to RogueGame
		RG = (RogueGame) game;
	
		//create socket on client
		try {
			socket = new Socket(RogueGame.servName,port);
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
	
		//Create map
		//First, read from file
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/resource/Map.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String line = null;

		int row = 0;
		//read in one line at a time and builds the wall and block arrays
		try {
			while ((line = reader.readLine()) != null) {

				String[] parts = line.split("\\s");
				//System.out.println(parts.length);
				for(int col = 0; col < parts.length; col++){
					RogueGame.map[row][col] = Integer.valueOf(parts[col]); // fill 2D map array
					//System.out.print(RogueGame.map[row][col] + " ");
					createMapEntities(row, col); // fill the entity arrays
				}
				System.out.print("\n");
				row++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(RG.state.secondPlayer == false){
			//create player
			RogueGame.player = new Player(RogueGame.WORLD_SIZE, new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RogueGame.player);
			player = RogueGame.player;
		}
		
		else if(RG.state.secondPlayer == true){
			
			//create player
			RogueGame.player = new Player(RogueGame.WORLD_SIZE, new Vector(RG.state.player.getX()/ RogueGame.TILE_SIZE, RG.state.player.getY()/RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RogueGame.player);
			
			//create 2nd player
			RogueGame.player2 = new Player(RogueGame.WORLD_SIZE, new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RogueGame.player2);
			secondPlayer = true;
			player = RogueGame.player2;
		}
		RogueGame.camX = 48;
		RogueGame.camY = 360;
		
		//create walls and blocks array for efficient collision detection.
		for (IsoEntity ie : RogueGame.walls) {
			RogueGame.wallsandblocks.add(ie);
		}
		for (IsoEntity ie : RogueGame.blocks) {
			RogueGame.wallsandblocks.add(ie);
		}
	}
	
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		if(player == null){ // player died, don't render anything.
			RG.enterState(RogueGame.STARTUPSTATE);
		}
		
		g.translate(-RogueGame.camX, -RogueGame.camY);		

		for (IsoEntity ie : RogueGame.ground) {
			ie.render(g);
		}
		
		Collections.sort(RogueGame.wallsandblocks);
		for (IsoEntity ie : RogueGame.wallsandblocks) {
			ie.render(g);
		}

		if (RogueGame.fireball != null) RogueGame.fireball.render(g);
		g.translate(RogueGame.camX, RogueGame.camY);		

		if(RogueGame.player != null){
			g.drawString("Level: " + (int)RogueGame.player.getLevel() + 
					" Health: " + (int)RogueGame.player.getHitPoints() + 
					"/" + (int)RogueGame.player.getMaxHitPoints() +  
					" Attack: " + (int)RogueGame.player.getAttack() + 
					" Armor: " + (int)RogueGame.player.getArmor() + 
					" Experience: " + (int)RogueGame.player.getExp()
					, 100 , 10);
			g.drawString("Dungeon Level: " , 100, 25);
		}
		
		g.translate(-RogueGame.camX, -RogueGame.camY);		

		
	}
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		float x = (60*delta/1000.0f);
		
		//reads in gamestate from the server
		try {
			RG.state = (GameState)socketIn.readObject();
		} catch (ClassNotFoundException | IOException e1) {
			System.out.println("Error reading game state from server.");
			e1.printStackTrace();
		}
		System.out.println("Players old position: " + RG.state.player.getX() + ", " + RG.state.player.getY());

		
		checkForPlayerJoin();
		updatePlayersPosition();
		
		//build and execute command from user
		getCommand(input);
		
				
		if (input.isKeyPressed(Input.KEY_LCONTROL)) {
			RogueGame.player.debugThis = !RogueGame.player.debugThis;
			if (RogueGame.fireball != null) RogueGame.fireball.debugThis = RogueGame.player.debugThis;
		}
		
		for(IsoEntity b : RogueGame.blocks) {
			if (b != RogueGame.player && b.collides(RogueGame.player) != null) {
				//System.out.println("Ouch!");
				//player.sayOuch();
				RogueGame.player.halt();
				RogueGame.player.ungo();
			}
		}
			
//		if (input.isKeyPressed(Input.KEY_ESCAPE)) {container.exit();}

			
		if (RogueGame.fireball != null) {
			RogueGame.fireball.update(x*1.5f);
			IsoEntity other;
			for (Iterator<IsoEntity> iie = RogueGame.blocks.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == RogueGame.player) continue;
				if (RogueGame.fireball.collides(other) != null) {
					System.out.println("true");
					RogueGame.fireball.kaboom();
					iie.remove();
					RogueGame.wallsandblocks.clear();
					
					for (IsoEntity ie : RogueGame.walls) {
						RogueGame.wallsandblocks.add(ie);
					}
					for (IsoEntity ie : RogueGame.blocks) {
						RogueGame.wallsandblocks.add(ie);
					}
					
					break;
				}
			}
			for (Iterator<IsoEntity> iie = RogueGame.walls.iterator(); iie.hasNext(); ) {
				other = iie.next();
				if (other == RogueGame.player) continue;
				if (RogueGame.fireball.collides(other) != null) {
					System.out.println("true");
					RogueGame.fireball.kaboom();
					break;
				}
			}
			if (RogueGame.fireball.done()) RogueGame.fireball = null;
		}
				
	}

	public int getID() {
		return RogueGame.PLAYINGSTATE;
	}
	
	/**
	 * Fills the entity arrays using the integer IDs stored in the map file.
	 * @param row x tile position of map
	 * @param col y tile position of map
	 */
	public void createMapEntities(int row, int col){
		switch(RogueGame.map[row][col]){
		case 0: // ground tiles
			RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(row*RogueGame.TILE_SIZE, col*RogueGame.TILE_SIZE)));
			break;
		case 1: // wall tiles
			RogueGame.walls.add(new Block(RogueGame.WORLD_SIZE,new Vector(row*RogueGame.TILE_SIZE, col*RogueGame.TILE_SIZE), true) );
			RogueGame.stop.add(new Ground(RogueGame.WORLD_SIZE,new Vector(row*RogueGame.TILE_SIZE, col*RogueGame.TILE_SIZE)) );
			break;
		case 2: // rock tiles
			RogueGame.blocks.add(new Block(RogueGame.WORLD_SIZE,new Vector(row*RogueGame.TILE_SIZE, col*RogueGame.TILE_SIZE), false) );
			RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(row*RogueGame.TILE_SIZE, col*RogueGame.TILE_SIZE)) );
			break;
		case 3: // potions
			break;
		default:
			break;
		}
	}
	
	void checkForPlayerJoin(){
		
		//handle second player joining game on first players instance
		if(secondPlayer == false && RG.state.secondPlayer == true){
			secondPlayer = true;
			RogueGame.player2 = new Player(RogueGame.WORLD_SIZE, new Vector(RG.state.player2.getX()/ RogueGame.TILE_SIZE, RG.state.player2.getY()/RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RogueGame.player2);
			RogueGame.wallsandblocks.add(RogueGame.player2);
		}
	}
//-----------------------------------------------------------------------------------------------------------------------
	
	void updatePlayersPosition(){

		//you are second player
		if(secondPlayer){
			RogueGame.player2.setPosition(RG.state.player2.getX(),RG.state.player2.getY());
			RogueGame.camX = RogueGame.player2.getPosition().getX() - RogueGame.VIEWPORT_SIZE_X / 2;
			RogueGame.camY = RogueGame.player2.getPosition().getX() + RogueGame.VIEWPORT_SIZE_Y / 2;
			RogueGame.player.setPosition(RG.state.player.getX(), RG.state.player.getY());
			
		}
		
		//you are first player
		else{
			RogueGame.player.setPosition(RG.state.player.getX(), RG.state.player.getY());
			//RogueGame.camX = RogueGame.playerX - RogueGame.VIEWPORT_SIZE_X / 2;
			//RogueGame.camY = RogueGame.playerY - RogueGame.VIEWPORT_SIZE_Y / 2;
			
			//if You are first Player, and second player exists update player 2 location
			if(RG.state.secondPlayer){
				RogueGame.player2.setPosition(RG.state.player2.getX(), RG.state.player2.getY());
			}
		}	
	}
	
//----------------------------------------------------------------------------------------------------------------------------
	
	//gets user input, builds command, sends command to server, and performs dead reckoning
	void getCommand(Input input){
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
				
//				System.out.println("Command from user: " + c.direction);
				c.execute(player);
			}	
		}
		
		else {
			RogueGame.player.halt();
			MoveCommand c = null;
			try {
				socketOut.writeObject(c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
}
