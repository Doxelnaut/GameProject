package pistolcave;


import java.awt.geom.Point2D;
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

		//Create map
		//First, read from file
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/resource/maps/Map.txt"));
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
					PistolCaveGame.map[row][col] = Integer.valueOf(parts[col]); // fill 2D map array
					//System.out.print(RogueGame.map[row][col] + " ");
					createMapEntities(row, col); // fill the entity arrays
					//updateVisibleBlockList(row,col);
				}
				System.out.print("\n");
				row++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			PC.currentPlayer = PistolCaveGame.player;

			addEnemies();
			newState = new clientState(1);
		}

		else{

			//create 1st player for rendering purposes
			PistolCaveGame.player = new Player(PistolCaveGame.WORLD_SIZE, PC.state.player.getPos(),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player);
			addEnemies();

			//create 2nd player
			PistolCaveGame.player2 = new Player(PistolCaveGame.WORLD_SIZE, new Vector(2*PistolCaveGame.TILE_SIZE, 2*PistolCaveGame.TILE_SIZE),1);
			PistolCaveGame.blocks.add(PistolCaveGame.player2);
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
		for (IsoEntity ie : PistolCaveGame.enemies) {
			PistolCaveGame.wallsandblocks.add(ie);
		}
	

		
	}


	private void addEnemies() {
		PistolCaveGame.enemy1 = new Actor(PistolCaveGame.WORLD_SIZE,new Vector(8*PistolCaveGame.TILE_SIZE,11*PistolCaveGame.TILE_SIZE),2);
		PistolCaveGame.enemies.add(PistolCaveGame.enemy1);
		PistolCaveGame.enemy2 = new Actor(PistolCaveGame.WORLD_SIZE,new Vector(15*PistolCaveGame.TILE_SIZE,10*PistolCaveGame.TILE_SIZE),3);
		PistolCaveGame.enemies.add(PistolCaveGame.enemy2);	
		for(IsoEntity ie : PistolCaveGame.enemies){
			if(secondPlayer){
				((Actor) ie).pathFinder(PistolCaveGame.player2);

			}else{
				((Actor) ie).pathFinder(PistolCaveGame.player);
				
			}
		}

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if(PC.currentPlayer == null){ // player died, don't render anything.
			PC.enterState(PistolCaveGame.STARTUPSTATE);
		}


		g.translate(-PistolCaveGame.camX, -PistolCaveGame.camY);		
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
				g.drawString("Dungeon Level: " , 100, 25);
			}

			g.translate(-PistolCaveGame.camX, -PistolCaveGame.camY);		

		}


		for(Bullet b : PC.bullets)
			b.render(g);

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
		getCommand(input);

		//build clientState and send to server
		buildClientState(delta);


		updateEnemyPaths();
			
					
	}
//
	private void updateEnemyPaths() {
		int endrow,endcol,startrow,startcol;
	
		if(secondPlayer){
			for(IsoEntity ie : PistolCaveGame.enemies){
				startrow= (int) (PistolCaveGame.player2.wPosition.getY() /PistolCaveGame.TILE_SIZE);
				startcol=(int)(PistolCaveGame.player2.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				endrow=(int)(PistolCaveGame.player2.wPosition.getY()/PistolCaveGame.TILE_SIZE);
				endcol=(int)(PistolCaveGame.player2.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				if(startrow != ie.getPath().startrow && startcol != ie.getPath().startcol){
					((Actor) ie).pathFinder(PistolCaveGame.player2);
				}
			}
		
		}else{
			for(IsoEntity ie : PistolCaveGame.enemies){
				startrow= (int) (PistolCaveGame.player.wPosition.getY() /PistolCaveGame.TILE_SIZE);
				startcol=(int)(PistolCaveGame.player.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				endrow=(int)(PistolCaveGame.player.wPosition.getY()/PistolCaveGame.TILE_SIZE);
				endcol=(int)(PistolCaveGame.player.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				double xx = (endrow - startrow) * (endrow - startrow);
				double y = (endcol-startcol) * (endcol-startcol);
				double z = Math.sqrt(xx+y); //distance formula
				    
				    //Enemy is too far away
				if( (int)z > 10) {
				   	return;
				}
				//Player is in a different 
				if(startrow != ie.getPath().startrow && startcol != ie.getPath().startcol){
					((Actor) ie).pathFinder(PistolCaveGame.player);
				}
			}
			
		}
	}
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
			}
		}	
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
		System.out.println("tx: " + tx + " ty: " + ty);
		System.out.println("row " + row + " col: " + col);
	}

	//----------------------------------------------------------------------------------------------------------------------------


	//gets user input, and executes command.
	void getCommand(Input input){
		
		//rotate player model towards mouse
		PC.currentPlayer.updateDirection(PC.theta);
		
		InputHandler inputHandler = new InputHandler();
		ArrayList<Command> commands = inputHandler.handleInput(input,PC);

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
		System.out.println("Theta = " + PC.theta);
		

				
	}
	//-----------------------------------------------------------------------------------------------------------------------------

	void buildClientState(int delta){

		//build state representing first player
		if(!secondPlayer){
			newState.playerNewState.setPos(PistolCaveGame.player.getPosition());
			newState.playerNewState.setCrouched(PistolCaveGame.player.crouch);
			newState.playerNewState.setDirection(PistolCaveGame.player.current);
		}
		//build state representing second player
		else{
			newState.playerNewState.setPos(PistolCaveGame.player2.getPosition());
			newState.playerNewState.setCrouched(PistolCaveGame.player2.crouch);
			newState.playerNewState.setDirection(PistolCaveGame.player2.current);
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
