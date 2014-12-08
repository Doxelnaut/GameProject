package rogueproject;

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
	
		//read in vector representing bitmap from the server
		try{
			RG.state = (GameState) socketIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error getting map from server.");
		}
	
		//builds local arrays storing map information from vector recieved from server.
		int r = 0;
		int c = 0;
		try {
			for(int j = 0; j < 100; j ++){
				for(int i = 0; i< 100;i++){
					if(RG.state.map[c] == 1){
						RogueGame.walls.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), true) );
						RogueGame.stop.add(new Ground(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
					}
					else if(RG.state.map[c] == 2){
						RogueGame.blocks.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), false) );
						RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
					}
					else{
					    RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
					}
					c++;
				 }
				
			    r++;
			    }
				
			} catch (NumberFormatException e) {
				e.printStackTrace();}
		
		
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
		
		if(secondPlayer){
			RogueGame.player2.setPosition(RG.state.player2.getX(),RG.state.player2.getY());
			
			RogueGame.camX = RogueGame.player2.getPosition().getX() - RogueGame.player.getX() /3;
			RogueGame.camY = RogueGame.player2.getPosition().getX() + RogueGame.player.getX() /2;
			
			RogueGame.player.setPosition(RG.state.player.getX(), RG.state.player.getY());
			
		}
		
		else{
			
			RogueGame.player.setPosition(RG.state.player.getX(), RG.state.player.getY());
			
			RogueGame.camX = RogueGame.player.getPosition().getX() - RogueGame.player.getX() /3; //- RogueGame.VIEWPORT_SIZE_X / 2;
			RogueGame.camY = RogueGame.player.getPosition().getY() + RogueGame.player.getY()/2; //- RogueGame.VIEWPORT_SIZE_Y / 2;
			
			if(RG.state.secondPlayer){
				RogueGame.player2.setPosition(RG.state.player2.getX(), RG.state.player2.getY());
			}
		}	
				
		
		//build command object
		InputHandler inputHandler = new InputHandler();
		ArrayList<Command> commands = inputHandler.handleInput(input);
		
		//send command object to the server
		if(commands.size() > 0){
			for(Command c : commands){
				try {
					socketOut.writeObject(c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Command from user: " + c.toString());
				c.execute(player);
			}
		}
		else {
			RogueGame.player.halt();
			Command c = null;
			try {
				socketOut.writeObject(c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
				
		if (input.isKeyPressed(Input.KEY_LCONTROL)) {
			RogueGame.player.debugThis = !RogueGame.player.debugThis;
			if (RogueGame.fireball != null) RogueGame.fireball.debugThis = RogueGame.player.debugThis;
		}
		
		for(IsoEntity b : RogueGame.blocks) {
			if (b != RogueGame.player && b.collides(RogueGame.player) != null) {
				System.out.println("Ouch!");
				//player.sayOuch();
				RogueGame.player.halt();
				RogueGame.player.ungo();
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
					
//				}
//			}
			
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
	
	public void setLevel(RogueGame rg) throws SlickException{

	}

}
