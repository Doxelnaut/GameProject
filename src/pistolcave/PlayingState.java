package pistolcave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Corey Amoruso
 * @author Ryan Bergquist
 * @author Zacharias Shufflebarger
 * 
 *	This file is part of Pistol Cave.
 *
 *  Pistol Cave is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pistol Cave is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Pistol Cave.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Corey Amoruso, Ryan Bergquist, Zacharias Shufflebarger
 */
public class PlayingState extends BasicGameState {


	Socket socket;                 //socket for connection to server
	int port = 1666;				
	ObjectOutputStream socketOut;	//object writer
	ObjectInputStream socketIn;		//object reader
	boolean secondPlayer = false;
	float oldPosX;
	float oldPosY;
	boolean joined = false;
	int mouseX;
	int mouseY;
	
	//clientState used to send player's desired state to server
	clientState newState;

	// input direction
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;

	// collective boolean for of all actors turns
	public boolean actorsTurns = false; 

	//copy of RogueGame
	PistolCaveGame PC;


	public void init(GameContainer container, StateBasedGame game)throws SlickException {

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) 
			throws SlickException {

		//cast game to RogueGame
		PC = (PistolCaveGame) game;

		//create socket on client
		try {
			socket = new Socket(PistolCaveGame.servName,port);
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

		//read game state from server. Needed in order to set player ones position in the second players instance

		try {
			PC.state = (GameState) socketIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error reading GameState.");
			e.printStackTrace();
		}


		if(PC.state.secondPlayer == false){
			//create player
			PistolCaveGame.player = new Player(PistolCaveGame.WORLD_SIZE, new Vector(3*PistolCaveGame.TILE_SIZE, 2*PistolCaveGame.TILE_SIZE),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player);
			PistolCaveGame.wallsandblocks.add(PistolCaveGame.player);
			PC.currentPlayer = PistolCaveGame.player;
			newState = new clientState(1);
		}

		else{

			//create 1st player for rendering purposes
			PistolCaveGame.player = new Player(PistolCaveGame.WORLD_SIZE, PC.state.player.getPos(),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player);
			PistolCaveGame.wallsandblocks.add(PistolCaveGame.player);
			//create 2nd player
			PistolCaveGame.player2 = new Player(PistolCaveGame.WORLD_SIZE, new Vector(2*PistolCaveGame.TILE_SIZE, 2*PistolCaveGame.TILE_SIZE),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player2);
			PistolCaveGame.wallsandblocks.add(PistolCaveGame.player2);
			PistolCaveGame.player2.secondPlayer = true;
			secondPlayer = true;
			PC.currentPlayer = PistolCaveGame.player2;
			newState = new clientState(2);

		}


		//create walls and blocks array for efficient collision detection.
		for (IsoEntity ie : PistolCaveGame.walls) {
			PistolCaveGame.wallsandblocks.add(ie);
		}
		for (IsoEntity ie : PistolCaveGame.blocks) {
			PistolCaveGame.wallsandblocks.add(ie);
		}

		
	}

	public void addEnemies(){
		for(NetVector v : PC.state.enemies){
			Actor tempE = new Actor(PistolCaveGame.WORLD_SIZE, v.getPos(),v.type);
			tempE.current = v.direction;
			PC.enemies.add(tempE);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if(PC.currentPlayer == null){ // player died, don't render anything.
			PC.enterState(PistolCaveGame.STARTUPSTATE);
		}

		// add enemies to render list
		for(Actor a : PC.enemies){
			PistolCaveGame.wallsandblocks.add(a);
		}
		
		g.translate(-PistolCaveGame.camX, -PistolCaveGame.camY);	
		
		// Second Player render -----------------------------------------------------------------------------------
		if(secondPlayer){
			for (IsoEntity ie : PistolCaveGame.ground) {
				if(ie.getX() >= ((PistolCaveGame.player2.getX() - PistolCaveGame.VIEWPORT_SIZE_X/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
						&& ie.getX() <= ((PistolCaveGame.player2.getX() + PistolCaveGame.VIEWPORT_SIZE_X/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
					if(ie.getY() >= ((PistolCaveGame.player2.getY() - PistolCaveGame.VIEWPORT_SIZE_Y/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
							&& ie.getY() <= ((PistolCaveGame.player2.getY() + PistolCaveGame.VIEWPORT_SIZE_Y/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
						ie.render(g);
					}
				}
			}
			
			Collections.sort(PistolCaveGame.wallsandblocks);
			for (IsoEntity ie : PistolCaveGame.wallsandblocks) {
				if(ie.getX() >= ((PistolCaveGame.player2.getX() - PistolCaveGame.VIEWPORT_SIZE_X/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
						&& ie.getX() <= ((PistolCaveGame.player2.getX() + PistolCaveGame.VIEWPORT_SIZE_X/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
					if(ie.getY() >= ((PistolCaveGame.player2.getY() - PistolCaveGame.VIEWPORT_SIZE_Y/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
							&& ie.getY() <= ((PistolCaveGame.player2.getY() + PistolCaveGame.VIEWPORT_SIZE_Y/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
						ie.render(g);						
					}
				}
			}

			g.translate(PistolCaveGame.camX, PistolCaveGame.camY);		

			if(PistolCaveGame.player2 != null){
				g.drawString("Level: " + (int)PistolCaveGame.player2.getLevel() + 
						" Health: " + (int)PistolCaveGame.player2.getHitPoints() + 
						"/" + (int)PistolCaveGame.player2.getMaxHitPoints() +  
						" Attack: " + (int)PistolCaveGame.player2.getAttack() + 
						" Armor: " + (int)PistolCaveGame.player2.getArmor() + 
						" Experience: " + (int)PistolCaveGame.player2.getExp()
						, 100 , 10);
				g.drawString("Dungeon Level: " , 100, 25);
			}

			g.translate(-PistolCaveGame.camX, -PistolCaveGame.camY);		
		// End Second Player render -------------------------------------------------------------------------------
		// First Player render ------------------------------------------------------------------------------------
		}else{
			for (IsoEntity ie : PistolCaveGame.ground) {
				if(ie.getX() >= ((PistolCaveGame.player.getX() - PistolCaveGame.VIEWPORT_SIZE_X/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
						&& ie.getX() <= ((PistolCaveGame.player.getX() + PistolCaveGame.VIEWPORT_SIZE_X/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
					if(ie.getY() >= ((PistolCaveGame.player.getY() - PistolCaveGame.VIEWPORT_SIZE_Y/2)- (PistolCaveGame.TILE_SIZE*2)) - 5 
							&& ie.getY() <= ((PistolCaveGame.player.getY() + PistolCaveGame.VIEWPORT_SIZE_Y/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
						ie.render(g);
					}
				}
			}
			
			Collections.sort(PistolCaveGame.wallsandblocks);
			for (IsoEntity ie : PistolCaveGame.wallsandblocks) {
				if(ie.getX() >= ((PistolCaveGame.player.getX() - PistolCaveGame.VIEWPORT_SIZE_X/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
						&& ie.getX() <= ((PistolCaveGame.player.getX() + PistolCaveGame.VIEWPORT_SIZE_X/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
					if(ie.getY() >= ((PistolCaveGame.player.getY() - PistolCaveGame.VIEWPORT_SIZE_Y/2) - (PistolCaveGame.TILE_SIZE*2)) - 5
							&& ie.getY() <= ((PistolCaveGame.player.getY() + PistolCaveGame.VIEWPORT_SIZE_Y/2) + (PistolCaveGame.TILE_SIZE*2)) + 5){
						ie.render(g);						
					}
				}
			}

			g.translate(PistolCaveGame.camX, PistolCaveGame.camY);		

			if(PistolCaveGame.player != null){
				g.drawString("Level: " + (int)PistolCaveGame.player.getLevel() + 
						" Health: " + (int)PistolCaveGame.player.getHitPoints() + 
						"/" + (int)PistolCaveGame.player.getMaxHitPoints() +  
						" Attack: " + (int)PistolCaveGame.player.getAttack() + 
						" Armor: " + (int)PistolCaveGame.player.getArmor() + 
						" Experience: " + (int)PistolCaveGame.player.getExp()
						, 100 , 10);
				g.drawString("World Pos: " + PistolCaveGame.player.getPosition() + " Entity Pos: " + PistolCaveGame.player.getX()+ ", " + PistolCaveGame.player.getY(), 100, 25);
			}

			g.translate(-PistolCaveGame.camX, -PistolCaveGame.camY);		

		}
		// End First Player render --------------------------------------------------------------------------------
		for(Bullet b : PC.bullets)
			b.render(g);
		
		// remove enemies from render list
		for(Actor a : PC.enemies){
			PistolCaveGame.wallsandblocks.remove(a);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		Input input = container.getInput();
		float x = (60*delta/1000.0f);

		//reads in gamestate from the server
		try {
			PC.state = (GameState)socketIn.readObject();
		} catch (ClassNotFoundException | IOException e1) {
			System.out.println("Error reading game state from server.");
			e1.printStackTrace();
		}

		checkForPlayerJoin();
		updatePlayersPosition();

		updateBullets();
		//build and execute command from user
		getCommand(input, x);

		//build clientState and send to server
		buildClientState(delta);

			
					
	}
//
	
	public int getID() {
		return PistolCaveGame.PLAYINGSTATE;
	}

	/**
	 * Fills the entity arrays using the integer IDs stored in the map file.
	 * @param row x tile position of map
	 * @param col y tile position of map
	 */
	public void createMapEntities(int row, int col){
		switch(PistolCaveGame.map[row][col]){
		case 0: // ground tiles
			PistolCaveGame.walkable[row][col] = 1;
			break;
		case 1: // wall tiles
			PistolCaveGame.walls.add(new Block(PistolCaveGame.WORLD_SIZE,new Vector(row*PistolCaveGame.TILE_SIZE, col*PistolCaveGame.TILE_SIZE), true) );
			PistolCaveGame.stop.add(new Ground(PistolCaveGame.WORLD_SIZE,new Vector(row*PistolCaveGame.TILE_SIZE, col*PistolCaveGame.TILE_SIZE)) );
			PistolCaveGame.walkable[row][col] = 0;
			break;
		case 2: // rock tiles
			PistolCaveGame.blocks.add(new Block(PistolCaveGame.WORLD_SIZE,new Vector(row*PistolCaveGame.TILE_SIZE, col*PistolCaveGame.TILE_SIZE), false) );
			PistolCaveGame.walkable[row][col] = 0;
			break;
		case 3: // potions
			PistolCaveGame.blocks.add(new Items(PistolCaveGame.WORLD_SIZE,new Vector(row*PistolCaveGame.TILE_SIZE, col*PistolCaveGame.TILE_SIZE), 2) );
			PistolCaveGame.walkable[row][col] = 0;
			break;
		default:
			break;
		}
		PistolCaveGame.ground.add(new Ground(PistolCaveGame.WORLD_SIZE, new Vector(row*PistolCaveGame.TILE_SIZE, col*PistolCaveGame.TILE_SIZE)) );
	}
	//------------------------------------------------------------------------------------------------------------------	
	void checkForPlayerJoin(){

		//handle second player joining game on first players instance
		if(joined == false && PC.state.secondPlayer == true && secondPlayer == false){
			joined = true;
			PistolCaveGame.player2 = new Player(PistolCaveGame.WORLD_SIZE, PC.state.player2.getPos(),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player2);
			PistolCaveGame.wallsandblocks.add(PistolCaveGame.player2);
		}
	}
	//-----------------------------------------------------------------------------------------------------------------------

	void updatePlayersPosition(){

		//you are second player
		if(secondPlayer){
			PistolCaveGame.player2.setPosition(PC.state.player2.getPos());
			PistolCaveGame.camX = PistolCaveGame.player2.getX() - PistolCaveGame.VIEWPORT_SIZE_X / 2;
			PistolCaveGame.camY = PistolCaveGame.player2.getY() - PistolCaveGame.VIEWPORT_SIZE_Y / 2;
			PistolCaveGame.player2.halt();
			//update first players model
			PistolCaveGame.player.setPosition(PC.state.player.getPos());
			PistolCaveGame.player.crouch = PC.state.player.getCrouched();
			PistolCaveGame.player.getWalkingAnimation(PC.state.player.getDirection());	
			PistolCaveGame.player.halt();
			PistolCaveGame.player.shooting = PC.state.player.attacking;
			PistolCaveGame.player.shoot();
		}

		//you are first player
		else{
			PistolCaveGame.player.setPosition(PC.state.player.getPos());
			PistolCaveGame.camX = PistolCaveGame.player.getX() - PistolCaveGame.VIEWPORT_SIZE_X / 2;
			PistolCaveGame.camY = PistolCaveGame.player.getY() - PistolCaveGame.VIEWPORT_SIZE_Y / 2;
			PistolCaveGame.player.halt();

			//if You are first Player, and second player exists update player 2 location
			if(joined){
				PistolCaveGame.player2.setPosition(PC.state.player2.getPos());
				PistolCaveGame.player2.crouch = PC.state.player2.getCrouched();
				PistolCaveGame.player2.getWalkingAnimation(PC.state.player2.getDirection());
				PistolCaveGame.player2.halt();
				PistolCaveGame.player2.shooting = PC.state.player2.attacking;
				PistolCaveGame.player2.shoot();
			}
		}	
		
		PC.enemies.clear();
		addEnemies();

	}

	//----------------------------------------------------------------------------------------------------------------------------
	public void updateVisibleBlockList(int row, int col){
		float tx;
		float ty;
		if(secondPlayer){
			tx = PistolCaveGame.player2.getX()/PistolCaveGame.TILE_SIZE;
			ty = PistolCaveGame.player2.getY()/PistolCaveGame.TILE_SIZE;
		}
		else{
			tx = PistolCaveGame.player.getX()/PistolCaveGame.TILE_SIZE;
			ty = PistolCaveGame.player.getY()/PistolCaveGame.TILE_SIZE;

		}

	}

	//----------------------------------------------------------------------------------------------------------------------------


	//gets user input, and executes command.
	void getCommand(Input input, float x){
		
		//rotate player model towards mouse
		PC.currentPlayer.updateDirection(PC.theta);
		
		InputHandler inputHandler = new InputHandler();
		ArrayList<Command> commands = inputHandler.handleInput(input,PC,x);

		if(commands.size() > 0){
			for(Command c : commands){
				c.execute(PC.currentPlayer);
			}	
		}
		
		//get mouse coordinates
		mouseX = input.getMouseX();
		mouseY = input.getMouseY();
		
		//created new vector with end point on top of player model
		Vector p = new Vector(550,340);
		//gets angle to mouse
		PC.theta = p.angleTo(new Vector(mouseX,mouseY));	
				
	}
	//-----------------------------------------------------------------------------------------------------------------------------

	void buildClientState(int delta){

		//build state representing first player
		if(!secondPlayer){
			newState.playerNewState.setPos(PistolCaveGame.player.getPosition());
			newState.playerNewState.setCrouched(PistolCaveGame.player.crouch);
			newState.playerNewState.setDirection(PistolCaveGame.player.current);
			newState.playerNewState.setMinX(PistolCaveGame.player.getCoarseGrainedMinX());
			newState.playerNewState.setMaxX(PistolCaveGame.player.getCoarseGrainedMaxX());
			newState.playerNewState.setMinY(PistolCaveGame.player.getCoarseGrainedMinY());
			newState.playerNewState.setMaxY(PistolCaveGame.player.getCoarseGrainedMaxY());
			newState.playerNewState.attacking = PistolCaveGame.player.shooting;

		}
		//build state representing second player
		else{
			newState.playerNewState.setPos(PistolCaveGame.player2.getPosition());
			newState.playerNewState.setCrouched(PistolCaveGame.player2.crouch);
			newState.playerNewState.setDirection(PistolCaveGame.player2.current);
			newState.playerNewState.setMinX(PistolCaveGame.player2.getCoarseGrainedMinX());
			newState.playerNewState.setMaxX(PistolCaveGame.player2.getCoarseGrainedMaxX());
			newState.playerNewState.setMinY(PistolCaveGame.player2.getCoarseGrainedMinY());
			newState.playerNewState.setMaxY(PistolCaveGame.player2.getCoarseGrainedMaxY());
			newState.playerNewState.attacking = PistolCaveGame.player2.shooting;
		}

		newState.delta = delta;

		newState.bullets.clear();
		NetVector temp;
		for(Bullet b : PC.bullets){
			temp = new NetVector();
			temp.setPos(b.getEPosition());
			temp.theta = b.theta;
			newState.bullets.add(temp);
		}


		//send clientState to server
		try {
			socketOut.reset();
			socketOut.writeObject(newState);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error Writing game state to client.");
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------

	void updateBullets(){
		PC.bullets.clear();
		//add bullets updated from server
		for(NetVector b : PC.state.bullets)
			PC.bullets.add(new Bullet(PistolCaveGame.WORLD_SIZE, b.getPos(),b.theta,0));
	}

}
