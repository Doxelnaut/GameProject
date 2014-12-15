
package pistolcave;



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
	public static final int STARTUPSTATE = 0;
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
	public static final int Enemy1 = 2;
	public static final int Enemy2 = 3;
	
	ArrayList<IsoEntity> actors;
	public static ArrayList<IsoEntity> ground;
	public static ArrayList<IsoEntity> blocks;
	public static ArrayList<IsoEntity> walls;
	public static ArrayList<IsoEntity> wallsandblocks;
	public static ArrayList<IsoEntity> stop;
	public static ArrayList<IsoEntity> enemies;
	public static ArrayList<PathFinder> enemiePaths;
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
		enemies = new ArrayList<IsoEntity>(100);
		enemiePaths = new ArrayList<PathFinder>(100);
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
		//update player 1
		if(playerState.playerNum == 1){
			
			/**
			 * This collision detection does not work. The first time an object inside of this
			 * is referenced, the client stalls. Somehow, the host needs to be able to check 
			 * the positions of each Entity as well as use the camera coordinates.
			 */
			//check for collisions here then if ok update player position
			ArrayList<IsoEntity> scanTable = new ArrayList<IsoEntity>();
			// set scan line to start one screen tile to the left of the player
			float scanline = playerState.playerNewState.getPos().getX() - this.TILE_SIZE*2;
			// stop when the scan line is further than one screen tile to the right of the player.
			while(scanline <= (playerState.playerNewState.getPos().getX() + this.TILE_SIZE*2)){
				//System.out.println("scanline: " + scanline);
				for(IsoEntity ie : this.wallsandblocks){
					if(ie.getCoarseGrainedMinX() <= scanline && !scanTable.contains(ie)){
						scanTable.add(ie);
					}
					else if (ie.getCoarseGrainedMaxX() <= scanline && scanTable.contains(ie)){
						scanTable.remove(ie);
					}
				}
				scanline+=2;
			}
			// check each entity that is close enough to see if the player collides
			if(!scanTable.isEmpty()){
				for(int i = 0; i < scanTable.size(); i++){
					IsoEntity ie = scanTable.get(i);
					if(playerState.playerNewState.minX < ie.getCoarseGrainedMaxX()
							&& playerState.playerNewState.maxX > ie.getCoarseGrainedMinX() 
							&& playerState.playerNewState.minY < ie.getCoarseGrainedMaxY() 
							&& playerState.playerNewState.maxY > ie.getCoarseGrainedMinY()){
						

					}
				}
			}
			
			//update player position
			this.state.player.setPos(playerState.playerNewState.getPos());
			this.state.player.setDirection(playerState.playerNewState.getDirection());
			this.state.player.setCrouched(playerState.playerNewState.getCrouched());
		}
		
		//update player 2
		else if(playerState.playerNum == 2){
			
			//check for collisions here then if ok update player position
			
			
			//update player position
			this.state.player2.setPos(playerState.playerNewState.getPos());
			this.state.player2.setDirection(playerState.playerNewState.getDirection());
			this.state.player2.setCrouched(playerState.playerNewState.getCrouched());
		}
		
		NetVector temp;
		this.bullets.clear();
		
		for(NetVector b : playerState.bullets){
			this.bullets.add(new Bullet(PistolCaveGame.WORLD_SIZE, b.getPos(),b.theta,0));
		}
		
		//update bullet positions
		for(Bullet b : this.bullets){
			b.update(playerState.delta);
		}
		
		for (Iterator<Bullet> i = this.bullets.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		
		state.bullets.clear();
		for(Bullet b : this.bullets){
			temp = new NetVector();
			temp.setPos(b.getEPosition());
			temp.theta = b.theta;
			//add bullet to state array to send to client
			state.bullets.add(temp);
		}
		
		
	}
	
}
