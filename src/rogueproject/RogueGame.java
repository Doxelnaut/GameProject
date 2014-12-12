
package rogueproject;



import java.util.ArrayList;
import java.util.Iterator;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
/* All this to write text. */


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
public class RogueGame extends StateBasedGame{

	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int HOSTSTATE = 3;
	public static final int ClientSetupState = 4;
	
	public boolean player1Connected = false;
	public boolean player2Connected = false;

	// other possible states: CharacterSelect, Inventory, Pause, Menu, Settings
	
	// create image, animation, and sound macros
	// ie: public static final String GAMEOVER_BANNER_RSC = "rogueproject/resource/gameover.png";
	
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
	
	public static final String GOLDGUI_IMG_RSC = "rogueproject/resource/goldui_big_pieces_0.png"; 
	public static final String GUI_MENULARGE_IMG_RSC = "rogueproject/resource/menu_large.png";
	public static final String PLAYER_IDLE_IMG_RSC = "rogueproject/resource/graphics/TMIM_Heroine/wIdle_0(16,99,112).png";
	public static final String ACTOR_GOBLIN0_IMG_RSC = "rogueproject/resource/graphics/goblin_spearman_0.png";
	public static final String ACTOR_GOBLIN1_IMG_RSC = "rogueproject/resource/graphics/goblin_spearman_elite.png";
	public static final String ALAGARD_FONT_RSC =  "rogueproject/resource/fonts/alagard_by_pix3m-d6awiwp.ttf";
	public static final String groundSheetPath   = "resource/flagstonetiles.png";
	public static final String tallBlockImgPath   = "resource/tallcaveblock.png";
	public static final String shortBlockImgPath   = "resource/shortcaveblock.png";
	public static final String host_background = "resource/host_background.png";
	public static final String clientSetup_background = "resource/clientSetup_background.png";
	public static final String potions = "resource/Potions.png";
	
	public static final String WalkLeft = "resource/Walk_10(15,69,108).png";
	public static final String WalkRight = "resource/Walk_2(15,62,111).png";
	public static final String WalkUp = "resource/Walk_6(15,66,111).png";
	public static final String WalkDown = "resource/Walk_14(15,61,108).png";
	public static final String WalkUpLeft = "resource/Walk_8(15,68,110).png";
	public static final String WalkUpRight = "resource/Walk_4(15,63,112).png";
	public static final String WalkDownLeft = "resource/Walk_12(15,63,108).png";
	public static final String WalkDownRight = "resource/Walk_0(15,63,110).png";
	
	public static final String fireLeft = "resource/Walk_Fire_10(4,95,109).png";
	public static final String fireRight = "resource/Walk_Fire_2(4,57,108).png";
	public static final String fireUp = "resource/Walk_Fire_6(4,85,112).png";
	public static final String fireDown = "resource/Walk_Fire_14(4,67,104).png";
	public static final String fireUpLeft = "resource/Walk_Fire_8(4,104,110).png";
	public static final String fireUpRight = "resource/Walk_Fire_4(4,59,117).png";
	public static final String fireDownLeft = "resource/Walk_Fire_12(4,67,107).png";
	public static final String fireDownRight = "resource/Walk_Fire_0(4,62,105).png";
	
	public static final String crouchLeft = "resource/Crouch_10(15,79,68).png";
	public static final String crouchRight = "resource/Crouch_2(15,38,90).png";
	public static final String crouchUp = "resource/Crouch_6(15,73,92).png";
	public static final String crouchDown = "resource/Crouch_14(15,41,63).png";
	public static final String crouchUpLeft = "resource/Crouch_8(15,87,80).png";
	public static final String crouchUpRight = "resource/Crouch_4(15,46,96).png";
	public static final String crouchDownLeft = "resource/Crouch_12(15,56,61).png";
	public static final String crouchDownRight = "resource/Crouch_0(15,35,76).png";
	
	public static final String fireCrouchLeft = "resource/Crouch_Fire_10(4,83,76).png";
	public static final String fireCrouchRight = "resource/Crouch_Fire_2(4,39,76).png";
	public static final String fireCrouchUp = "resource/Crouch_Fire_6(4,71,81).png";
	public static final String fireCrouchDown = "resource/Crouch_Fire_14(4,49,70).png";
	public static final String fireCrouchUpLeft = "resource/Crouch_Fire_8(4,91,80).png";
	public static final String fireCrouchUpRight = "resource/Crouch_Fire_4(4,42,85).png";
	public static final String fireCrouchDownLeft = "resource/Crouch_Fire_12(4,55,73).png";
	public static final String fireCrouchDownRight = "resource/Crouch_Fire_0(4,44,72).png";
	
	public static final String Enemy1WalkLeft = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkRight = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkUp = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkDown = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkUpLeft = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkUpRight = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkDownLeft = "resource/Enemy1_walk.png";
	public static final String Enemy1WalkDownRight = "resource/Enemy1_walk.png";
	
	public static final String Enemy1AttackLeft = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackRight = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackUp = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackDown = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackUpLeft = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackUpRight = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackDownLeft = "resource/Enemy1_Attack.png";
	public static final String Enemy1AttackDownRight = "resource/Enemy1_Attack.png";
	
	public static final String Enemy2WalkLeft = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkRight = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkUp = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkDown = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkUpLeft = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkUpRight = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkDownLeft = "resource/Enemy2_walk.png";
	public static final String Enemy2WalkDownRight = "resource/Enemy2_walk.png";
	
	public static final String Enemy2AttackLeft = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackRight = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackUp = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackDown = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackUpLeft = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackUpRight = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackDownLeft = "resource/Enemy2_Attack.png";
	public static final String Enemy2AttackDownRight = "resource/Enemy2_Attack.png";
	
	
	public static final String fireballSheetPath = "resource/fireball.png";
	public static final String explosionSheetPath   = "resource/explosion.png";
	public static final String bulletResource = "resource/bullet.png";
	//public static final String ouchSoundPath = "resource/ouch.wav";
	
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
	public static ArrayList<IsoEntity> visible;
	public ArrayList<Bullet> bullets;

	public static Fireball fireball;

	GameState state = new GameState();
	
	public static int[][]map = new int[(int)WORLD_SIZE_X][(int)WORLD_SIZE_Y];
	public static int[][]walkable = new int[(int)WORLD_SIZE_X][(int)WORLD_SIZE_Y];

	public static Player player = null;
	public static Player player2 = null;
	public static Actor enemy1 = null;
	public static Actor enemy2 = null;
	
	public RogueGame(String title, float width, float height) {
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
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		//addState(new GameOverState());
		addState(new PlayingState());
		addState(new HostState());
		addState(new ClientSetupState());
		
		// preload resources here
		// images: ResourceManager.loadImage(IMG_RSC);
		// sounds: ResourceManager.loadSound(SND_RSC);
		ResourceManager.loadImage(GOLDGUI_IMG_RSC);
		ResourceManager.loadImage(GUI_MENULARGE_IMG_RSC);
		ResourceManager.loadImage(PLAYER_IDLE_IMG_RSC);
		ResourceManager.loadImage(ACTOR_GOBLIN0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_GOBLIN1_IMG_RSC);
		ResourceManager.loadImage(groundSheetPath);
		ResourceManager.loadImage(tallBlockImgPath);
		ResourceManager.loadImage(shortBlockImgPath);
		ResourceManager.loadImage(potions);
		ResourceManager.loadImage(WalkLeft);
		ResourceManager.loadImage(WalkRight);
		ResourceManager.loadImage(WalkUp);
		ResourceManager.loadImage(WalkDown);
		ResourceManager.loadImage(WalkUpLeft);
		ResourceManager.loadImage(WalkUpRight);
		ResourceManager.loadImage(WalkDownLeft);
		ResourceManager.loadImage(WalkDownRight);
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
		ResourceManager.loadImage(fireballSheetPath);
		//ResourceManager.loadImage(explosionSheetPath);
		ResourceManager.loadImage(host_background);
		ResourceManager.loadImage(clientSetup_background);

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
		//ResourceManager.loadImage(ouchSoundPath);

		

	//	state.player = new Player(GameState.WARRIOR);
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new RogueGame("El Rogue del Rey", RogueGame.VIEWPORT_SIZE_X, RogueGame.VIEWPORT_SIZE_Y));
			app.setDisplayMode((int)RogueGame.VIEWPORT_SIZE_X, (int)RogueGame.VIEWPORT_SIZE_Y, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized void update(clientState playerState){
		//update player 1
		if(playerState.playerNum == 1){
			
			//check for collisions here then if ok update player position
			
			
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
			this.bullets.add(new Bullet(RogueGame.WORLD_SIZE, b.getPos()));
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
			temp.setPos(new Vector(b.getX(),b.getY()));
			//add bullet to state array to send to client
			state.bullets.add(temp);
		}
		
		
	}
	
}
