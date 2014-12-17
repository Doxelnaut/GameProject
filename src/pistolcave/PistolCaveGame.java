
package pistolcave;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
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
public class PistolCaveGame extends StateBasedGame{

	double theta;
	int enemyID = 0;
	static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int HOSTSTATE = 3;
	public static final int ClientSetupState = 4;
	
	public boolean player1Connected = false;
	public boolean player2Connected = false;

	public static final int TILE_SIZE = 16; //World Coordinate tile size. Rendering will handle conversion to isometric.
	public static float playerX = 0;
	public static float playerY = 0;
	public static float player2X = 0;
	public static float player2Y = 0;
	public static float WORLD_SIZE_X = (TILE_SIZE * 100);
	public static float WORLD_SIZE_Y = (TILE_SIZE * 100);
	public static Vector WORLD_SIZE = new Vector(WORLD_SIZE_X, WORLD_SIZE_Y);
	public static float VIEWPORT_SIZE_X = 1024;
	public static float VIEWPORT_SIZE_Y = 720;
	
	public static float camX = 0;
	public static float	camY = 0;
	
	public static float offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
	public static float offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
	public static float offsetMinX = 0;
    public static float offsetMinY = 0;
	
	public final float ScreenWidth;
	public final float ScreenHeight;
	
	/* Menu graphics */
	public static final String GOLDGUI_IMG_RSC = "resource/graphics/goldui_big_pieces_0.png"; 
	public static final String GUI_MENULARGE_IMG_RSC = "resource/graphics/menu_large.png";
	public static final String ALAGARD_FONT_RSC =  "resource/fonts/alagard_by_pix3m-d6awiwp.ttf";
	public static final String host_background = "resource/graphics/host_background.png";
	public static final String clientSetup_background = "resource/graphics/clientSetup_background.png";
	
	/* Item graphics */
	public static final String potions = "resource/graphics/Potions.png";
	
	/* Map graphics */
	public static final String groundSheetPath   = "resource/graphics/flagstonetiles.png";
	public static final String tallBlockImgPath   = "resource/graphics/tallcaveblock.png";
	public static final String shortBlockImgPath   = "resource/graphics/shortcaveblock.png";
	public static final String caveTileSetPath		= "resource/graphics/tileset_cave_1.png";
	public static final String waterTileSetPath		= "resource/graphics/waterTiles.png";
	public static final String caveWallsTileSetPath		= "resource/graphics/CaveWalls.png";

	/* Player graphics */
	public static final String PLAYER_IDLE_IMG_RSC = "resource/graphics/TMIM_Heroine/wIdle_0(16,99,112).png";

	public static final String WalkLeft = "resource/graphics/TMIM_Heroine/Walk/Walk_10(15,69,108).png";
	public static final String WalkRight = "resource/graphics/TMIM_Heroine/Walk/Walk_2(15,62,111).png";
	public static final String WalkUp = "resource/graphics/TMIM_Heroine/Walk/Walk_6(15,66,111).png";
	public static final String WalkDown = "resource/graphics/TMIM_Heroine/Walk/Walk_14(15,61,108).png";
	public static final String WalkUpLeft = "resource/graphics/TMIM_Heroine/Walk/Walk_8(15,68,110).png";
	public static final String WalkUpRight = "resource/graphics/TMIM_Heroine/Walk/Walk_4(15,63,112).png";
	public static final String WalkDownLeft = "resource/graphics/TMIM_Heroine/Walk/Walk_12(15,63,108).png";
	public static final String WalkDownRight = "resource/graphics/TMIM_Heroine/Walk/Walk_0(15,63,110).png";
	
	public static final String fireLeft = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_10(4,95,109).png";
	public static final String fireRight = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_2(4,57,108).png";
	public static final String fireUp = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_6(4,85,112).png";
	public static final String fireDown = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_14(4,67,104).png";
	public static final String fireUpLeft = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_8(4,104,110).png";
	public static final String fireUpRight = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_4(4,59,117).png";
	public static final String fireDownLeft = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_12(4,67,107).png";
	public static final String fireDownRight = "resource/graphics/TMIM_Heroine/Walk_Fire/Walk_Fire_0(4,62,105).png";
	
	public static final String crouchLeft = "resource/graphics/TMIM_Heroine/Crouch/Crouch_10(15,79,68).png";
	public static final String crouchRight = "resource/graphics/TMIM_Heroine/Crouch/Crouch_2(15,38,90).png";
	public static final String crouchUp = "resource/graphics/TMIM_Heroine/Crouch/Crouch_6(15,73,92).png";
	public static final String crouchDown = "resource/graphics/TMIM_Heroine/Crouch/Crouch_14(15,41,63).png";
	public static final String crouchUpLeft = "resource/graphics/TMIM_Heroine/Crouch/Crouch_8(15,87,80).png";
	public static final String crouchUpRight = "resource/graphics/TMIM_Heroine/Crouch/Crouch_4(15,46,96).png";
	public static final String crouchDownLeft = "resource/graphics/TMIM_Heroine/Crouch/Crouch_12(15,56,61).png";
	public static final String crouchDownRight = "resource/graphics/TMIM_Heroine/Crouch/Crouch_0(15,35,76).png";
	
	public static final String fireCrouchLeft = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_10(4,83,76).png";
	public static final String fireCrouchRight = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_2(4,39,76).png";
	public static final String fireCrouchUp = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_6(4,71,81).png";
	public static final String fireCrouchDown = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_14(4,49,70).png";
	public static final String fireCrouchUpLeft = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_8(4,91,80).png";
	public static final String fireCrouchUpRight = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_4(4,42,85).png";
	public static final String fireCrouchDownLeft = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_12(4,55,73).png";
	public static final String fireCrouchDownRight = "resource/graphics/TMIM_Heroine/Crouch_Fire/Crouch_Fire_0(4,44,72).png";
	
	/* Enemy graphics */
	public static final String ACTOR_GOBLIN0_IMG_RSC = "resource/graphics/goblin_spearman_0.png";
	public static final String ACTOR_GOBLIN1_IMG_RSC = "resource/graphics/goblin_spearman_elite.png";

	public static final String Enemy1WalkLeft = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkRight = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkUp = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkDown = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkUpLeft = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkUpRight = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkDownLeft = "resource/graphics/Enemy1_walk.png";
	public static final String Enemy1WalkDownRight = "resource/graphics/Enemy1_walk.png";
	
	public static final String Enemy1AttackLeft = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackRight = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackUp = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackDown = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackUpLeft = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackUpRight = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackDownLeft = "resource/graphics/Enemy1_Attack.png";
	public static final String Enemy1AttackDownRight = "resource/graphics/Enemy1_Attack.png";
	
	public static final String Enemy2WalkLeft = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkRight = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkUp = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkDown = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkUpLeft = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkUpRight = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkDownLeft = "resource/graphics/Enemy2_walk.png";
	public static final String Enemy2WalkDownRight = "resource/graphics/Enemy2_walk.png";
	
	public static final String Enemy2AttackLeft = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackRight = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackUp = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackDown = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackUpLeft = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackUpRight = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackDownLeft = "resource/graphics/Enemy2_Attack.png";
	public static final String Enemy2AttackDownRight = "resource/graphics/Enemy2_Attack.png";
	
	/* Miscellaneous graphics */
	public static final String explosionSheetPath   = "resource/graphics/explosion.png";
	public static final String bulletResource = "resource/graphics/bullet.png";
	
	/* Sounds */
	//public static final String ouchSoundPath = "resource/sounds/ouch.wav";
	
	public static final String servName = "127.0.0.1";
	
	public static final int WARRIOR = 1;
	
	//I dont think this is needed
	//public static final int Enemy1 = 2;
	//public static final int Enemy2 = 3;
	
	ArrayList<IsoEntity> actors;
	public static ArrayList<IsoEntity> ground;
	public static ArrayList<IsoEntity> blocks;
	public static ArrayList<IsoEntity> walls;
	public static ArrayList<IsoEntity> wallsandblocks;
	public static ArrayList<IsoEntity> stop;
	
	public static ArrayList<Actor> enemies;  //list of enemy entities to be used by the client for rendering, server does not touch this
	public static ArrayList<NetVector> sEnemies; //list of NetVectors to represent enemies on the server  
	public static ArrayList<NetVector> sEPaths; 
	public ArrayList<Bullet> bullets;

	GameState state = new GameState();
	static PathFinder x;
	public static int[][]map = new int[(int)WORLD_SIZE_X][(int)WORLD_SIZE_Y];
	public static int[][]walkable = new int[(int)WORLD_SIZE_X][(int)WORLD_SIZE_Y];

	public static Player player = null;
	public static Player player2 = null;
	public static Actor enemy1 = null;
	static PathFinder enemy1Path;
	public static Actor enemy2 = null;
	public Player currentPlayer;
	static PathFinder enemy2Path = null;
	
	public PistolCaveGame(String title, float width, float height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		
		ground = new ArrayList<IsoEntity>(100);
		blocks = new ArrayList<IsoEntity>(100);
		walls = new ArrayList<IsoEntity>(100);
		wallsandblocks = new ArrayList<IsoEntity>(200);
		stop = new ArrayList<IsoEntity>(100);
		bullets = new ArrayList<Bullet>(10);
		enemies = new ArrayList<Actor>(100);
		sEnemies = new ArrayList<NetVector>(100);
		sEPaths = new ArrayList<NetVector>(100);
		
		addEnemies();
		state.enemies = sEnemies;
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		//addState(new GameOverState());
		addState(new PlayingState());
		addState(new HostState());
		addState(new ClientSetupState());
		
		/* preload resources here */
		
		// preload menu screen graphics
		ResourceManager.loadImage(GOLDGUI_IMG_RSC);
		ResourceManager.loadImage(GUI_MENULARGE_IMG_RSC);
		ResourceManager.loadImage(host_background);
		ResourceManager.loadImage(clientSetup_background);
		
		// preload map graphics
		ResourceManager.loadImage(groundSheetPath);
		ResourceManager.loadImage(tallBlockImgPath);
		ResourceManager.loadImage(shortBlockImgPath);
		ResourceManager.loadImage(caveTileSetPath);
		ResourceManager.loadImage(caveWallsTileSetPath);
		ResourceManager.loadImage(waterTileSetPath);
		
		// preload Enemy graphics
		ResourceManager.loadImage(ACTOR_GOBLIN0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_GOBLIN1_IMG_RSC);
		ResourceManager.loadImage(Enemy1WalkLeft);
		ResourceManager.loadImage(Enemy1WalkRight);
		ResourceManager.loadImage(Enemy1WalkUp);
		ResourceManager.loadImage(Enemy1WalkDown);
		ResourceManager.loadImage(Enemy1WalkUpLeft);
		ResourceManager.loadImage(Enemy1WalkUpRight);
		ResourceManager.loadImage(Enemy1WalkDownLeft);
		ResourceManager.loadImage(Enemy1WalkDownRight);
		ResourceManager.loadImage(Enemy2WalkLeft);
		ResourceManager.loadImage(Enemy2WalkRight);
		ResourceManager.loadImage(Enemy2WalkUp);
		ResourceManager.loadImage(Enemy2WalkDown);
		ResourceManager.loadImage(Enemy2WalkUpLeft);
		ResourceManager.loadImage(Enemy2WalkUpRight);
		ResourceManager.loadImage(Enemy2WalkDownLeft);
		ResourceManager.loadImage(Enemy2WalkDownRight);
		ResourceManager.loadImage(Enemy1AttackLeft);
		ResourceManager.loadImage(Enemy1AttackRight);
		ResourceManager.loadImage(Enemy1AttackUp);
		ResourceManager.loadImage(Enemy1AttackDown);
		ResourceManager.loadImage(Enemy1AttackUpLeft);
		ResourceManager.loadImage(Enemy1AttackUpRight);
		ResourceManager.loadImage(Enemy1AttackDownLeft);
		ResourceManager.loadImage(Enemy1AttackDownRight);
		ResourceManager.loadImage(Enemy2AttackLeft);
		ResourceManager.loadImage(Enemy2AttackRight);
		ResourceManager.loadImage(Enemy2AttackUp);
		ResourceManager.loadImage(Enemy2AttackDown);
		ResourceManager.loadImage(Enemy2AttackUpLeft);
		ResourceManager.loadImage(Enemy2AttackUpRight);
		ResourceManager.loadImage(Enemy2AttackDownLeft);
		ResourceManager.loadImage(Enemy2AttackDownRight);

		//load player animations
		ResourceManager.loadImage(PLAYER_IDLE_IMG_RSC);
		ResourceManager.loadImage(WalkLeft);
		ResourceManager.loadImage(WalkRight);
		ResourceManager.loadImage(WalkUp);
		ResourceManager.loadImage(WalkDown);
		ResourceManager.loadImage(WalkUpLeft);
		ResourceManager.loadImage(WalkUpRight);
		ResourceManager.loadImage(WalkDownLeft);
		ResourceManager.loadImage(WalkDownRight);
		ResourceManager.loadImage(fireLeft);
		ResourceManager.loadImage(fireRight);
		ResourceManager.loadImage(fireUp);
		ResourceManager.loadImage(fireDown);
		ResourceManager.loadImage(fireUpLeft);
		ResourceManager.loadImage(fireUpRight);
		ResourceManager.loadImage(fireDownLeft);
		ResourceManager.loadImage(fireDownRight);
		ResourceManager.loadImage(crouchLeft);
		ResourceManager.loadImage(crouchRight);
		ResourceManager.loadImage(crouchUp);
		ResourceManager.loadImage(crouchDown);
		ResourceManager.loadImage(crouchUpLeft);
		ResourceManager.loadImage(crouchUpRight);
		ResourceManager.loadImage(crouchDownLeft);
		ResourceManager.loadImage(crouchDownRight);
		ResourceManager.loadImage(fireCrouchLeft);
		ResourceManager.loadImage(fireCrouchRight);
		ResourceManager.loadImage(fireCrouchUp);
		ResourceManager.loadImage(fireCrouchDown);
		ResourceManager.loadImage(fireCrouchUpLeft);
		ResourceManager.loadImage(fireCrouchUpRight);
		ResourceManager.loadImage(fireCrouchDownLeft);
		ResourceManager.loadImage(fireCrouchDownRight);
		ResourceManager.loadImage(bulletResource);
		
		// preload item graphics
		ResourceManager.loadImage(potions);
		
		// preload sounds
		//ResourceManager.loadImage(ouchSoundPath);

		generateMap();		
		
		//create walls and blocks array for efficient collision detection.
		for (IsoEntity ie : PistolCaveGame.walls) {
			PistolCaveGame.wallsandblocks.add(ie);
		}
		for (IsoEntity ie : PistolCaveGame.blocks) {
			PistolCaveGame.wallsandblocks.add(ie);
		}
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new PistolCaveGame("Pistol Cave", PistolCaveGame.VIEWPORT_SIZE_X, PistolCaveGame.VIEWPORT_SIZE_Y));
			app.setDisplayMode((int)PistolCaveGame.VIEWPORT_SIZE_X, (int)PistolCaveGame.VIEWPORT_SIZE_Y, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized void update(clientState playerState){
		
		sEnemies = playerState.enemies;
		
		//update player 1
		if(playerState.playerNum == 1){

			//Ryan here is a call to a method to handle path finding, the method is down below

		//	updateEnemyPaths(playerState.playerNewState.getPos(),1);
			
			//update the gamestate enemy array with data from the server's list of enemies(sEnemies)
			this.state.enemies = sEnemies;

			//check for collisions here then if ok update player position
			boolean canMove = true;
			//check against walls
			for(IsoEntity ie : stop){ // smoke and mirrors: bad collision detection, but small map/few actors
				if(playerState.playerNewState.wPosition.getX() - TILE_SIZE/2 < ie.getPosition().getX() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getX() + TILE_SIZE/2 > ie.getPosition().getX() - TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() - TILE_SIZE/2 < ie.getPosition().getY() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() + TILE_SIZE/2 > ie.getPosition().getY() - TILE_SIZE/2){
					canMove = false;
					break;
				}
			}
			// check against enemies
			for(NetVector ie : sEnemies){ // smoke and mirrors: bad collision detection, but small map/few actors
				if(playerState.playerNewState.wPosition.getX() - TILE_SIZE/2 < ie.getPos().getX() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getX() + TILE_SIZE/2 > ie.getPos().getX() - TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() - TILE_SIZE/2 < ie.getPos().getY() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() + TILE_SIZE/2 > ie.getPos().getY() - TILE_SIZE/2){
					canMove = false;
					break;
				}
			}

			//update player position
			if(canMove){
				this.state.player.setPos(playerState.playerNewState.getPos());
			}
			//update player direction and crouch, regardless of collisions
			this.state.player.setDirection(playerState.playerNewState.getDirection());
			this.state.player.setCrouched(playerState.playerNewState.getCrouched());

		}

		//update player 2
		else if(playerState.playerNum == 2){

			//Ryan here is a call to a method to handle path finding, the method is down below
			//updateEnemyPaths(playerState.playerNewState.getPos(),2);
			
			//update the gamestate enemy array with data from the server's list of enemies(sEnemies)
			this.state.enemies = sEnemies;

			//check for collisions here then if ok update second player position
			boolean canMove = true;
			// check against walls
			for(IsoEntity ie : stop){
				if(playerState.playerNewState.wPosition.getX() - TILE_SIZE/2 < ie.getPosition().getX() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getX() + TILE_SIZE/2 > ie.getPosition().getX() - TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() - TILE_SIZE/2 < ie.getPosition().getY() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() + TILE_SIZE/2 > ie.getPosition().getY() - TILE_SIZE/2){
					canMove = false;
					break;
				}
			}
			// check against enemies
			for(NetVector ie : sEnemies){ 
				if(playerState.playerNewState.wPosition.getX() - TILE_SIZE/2 < ie.getPos().getX() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getX() + TILE_SIZE/2 > ie.getPos().getX() - TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() - TILE_SIZE/2 < ie.getPos().getY() + TILE_SIZE/2
						&& playerState.playerNewState.wPosition.getY() + TILE_SIZE/2 > ie.getPos().getY() - TILE_SIZE/2){
					canMove = false;
					break;
				}
			}

			//update second player position
			if(canMove){
				this.state.player2.setPos(playerState.playerNewState.getPos());
			}
			//update second player direction and crouch, regardless of collisions
			this.state.player2.setDirection(playerState.playerNewState.getDirection());
			this.state.player2.setCrouched(playerState.playerNewState.getCrouched());
		}
		
		NetVector temp;
		this.bullets.clear();
		
		for(NetVector b : playerState.bullets){
//			System.out.println("NetVector bullet wPos: " + b.getPos() + ", ePos: " + b.getEPos());
			Bullet newSB = new Bullet(PistolCaveGame.WORLD_SIZE, b.getPos(),b.theta,0,playerState.playerNewState.getDamage());
			this.bullets.add(newSB);
//			System.out.println("new Bullet wPos: " + newSB.getPosition() + ", ePos: " + newSB.getEPosition());
		}
		
		//update bullet positions
		for(Bullet b : this.bullets){
//			System.out.println("before update wPos: " + b.getPosition() + ", ePos: " + b.getEPosition());
			b.update(playerState.delta);
//			System.out.println("after update wPos: " + b.getPosition() + ", ePos: " + b.getEPosition());

			// collision detection against enemies
			for(NetVector nv : sEnemies){
//				System.out.println("nv bounds: " + nv.getPos() + ", b pos: " + b.getPosition());
				if(b.getPosition().getX() - TILE_SIZE/2 < nv.getPos().getX() + TILE_SIZE/2
						&& b.getPosition().getX() + TILE_SIZE/2 > nv.getPos().getX() - TILE_SIZE/2
						&& b.getPosition().getY() - TILE_SIZE/2 < nv.getPos().getY() + TILE_SIZE/2
						&& b.getPosition().getY() + TILE_SIZE/2 > nv.getPos().getY() - TILE_SIZE/2){
					b.setActiveVar(false);
					nv.doDamage(b.getDamage());
//					System.out.println("Bullet collides with enemy.");
				}
			}
		}
		
		for (Iterator<Bullet> i = this.bullets.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		
		state.bullets.clear();
		for(Bullet b : this.bullets){
			temp = new NetVector();
			temp.setPos(b.getPosition());
			temp.theta = b.theta;
			//add bullet to state array to send to client
			state.bullets.add(temp);
		}
		
		
	}
	

	//private void updateEnemyPaths(Vector p) {
		
	//	int endrow,endcol,startrow,startcol;
	
	//run dijkstras here, updating the positions of the enemies stored in sEnemies.	
		
		
		/*if(secondPlayer){
			for(IsoEntity ie : PistolCaveGame.enemies){
				startrow= (int) (PistolCaveGame.player2.wPosition.getY() /PistolCaveGame.TILE_SIZE);
				startcol=(int)(PistolCaveGame.player2.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				endrow=(int)(PistolCaveGame.player2.wPosition.getY()/PistolCaveGame.TILE_SIZE);
				endcol=(int)(PistolCaveGame.player2.wPosition.getX()/PistolCaveGame.TILE_SIZE);
				if(startrow != ie.getPath().startrow && startcol != ie.getPath().startcol){
					((Actor) ie).pathFinder(PistolCaveGame.player2,2);
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
					((Actor) ie).pathFinder(PistolCaveGame.player,1);
				}
			}
			
>>>>>>> refs/heads/Issue4
		}


	//run dijkstras here, updating the positions of the enemies stored in sEnemies.	

	}
	public void pathFinder(Vector user, NetVector enemy) {
		int[] source = new int[2];
		source[0]= (int) user.getX()/16;
		source[1] =(int) user.getY()/16;
		Dijkstra pathToUser = new Dijkstra(source);
		
		System.out.println(source[0] + " " + source[1]);
		int[][] TP = pathToUser.weights;
		for(int i = 0; i < TP.length; i++){
			for(int j = 0; j < TP.length; j++){
				System.out.print(pathToUser.G[i][j].getCost() + "    ");
			}
			System.out.println();
		}
	}
*/	
	//creates the enemies on the server, you will update the positions of these object instead of an Entity or Actor on the server,
	//the sEnemies array will be passed to the client which will inturn build the "enemies" array from the data stored in these objects.
	private void addEnemies() {
		NetVector enemy1 = new NetVector(new Vector(8*PistolCaveGame.TILE_SIZE,11*PistolCaveGame.TILE_SIZE));
		enemy1.direction = 5;
		enemy1.type = 2;
		enemy1.enemyID = enemyID++;
		PistolCaveGame.sEnemies.add(enemy1);
		NetVector enemy2 = new NetVector(new Vector(15*PistolCaveGame.TILE_SIZE,10*PistolCaveGame.TILE_SIZE));
		enemy2.direction = 5;
		enemy2.type = 3;
		enemy2.enemyID = enemyID++;
		PistolCaveGame.sEnemies.add(enemy2);	
	}
	
	public void generateMap(){
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
					map[row][col] = Integer.valueOf(parts[col]); // fill 2D map array
					//System.out.print(RogueGame.map[row][col] + " ");
					createMapEntities(row, col); // fill the entity arrays
					//updateVisibleBlockList(row,col);
				}
//				System.out.print("\n");
				row++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fills the entity arrays using the integer IDs stored in the map file.
	 * @param row x tile position of map
	 * @param col y tile position of map
	 */
	public void createMapEntities(int row, int col){
		switch(map[row][col]){
		case 0: // ground tiles
			walkable[row][col] = 1;
			ground.add(new Ground(WORLD_SIZE, new Vector(row*TILE_SIZE, col*TILE_SIZE)) );

			break;
		case 1: // wall tiles
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 0) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 2: // rock tiles
			blocks.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 1) );
			walkable[row][col] = 0;
			break;
		case 3: // potions
			blocks.add(new Items(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 2) );
			ground.add(new Ground(WORLD_SIZE, new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 4: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 2) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 5: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 3) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 6: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 4) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 7: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 5) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 8: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 6) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 9: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 7) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 10: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 8) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		case 11: //walls
			walls.add(new Block(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE), 9) );
			stop.add(new Ground(WORLD_SIZE,new Vector(row*TILE_SIZE, col*TILE_SIZE)) );
			walkable[row][col] = 0;
			break;
		default:
			break;
		}
	}
}
