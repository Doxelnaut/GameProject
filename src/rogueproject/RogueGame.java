
package rogueproject;


import jig.Entity;
import jig.ResourceManager;

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

	// other possible states: CharacterSelect, Inventory, Pause, Menu, Settings
	
	// create image, animation, and sound macros
	// ie: public static final String GAMEOVER_BANNER_RSC = "rogueproject/resource/gameover.png";
	
	public static final int TILE_SIZE = 16; //World Coordinate tile size. Rendering will handle conversion to isometric.
	public static float playerX = 0;
	public static float playerY = 0;
	public static float WORLD_SIZE_X = (TILE_SIZE * 50);
	public static float WORLD_SIZE_Y = (TILE_SIZE * 50);
	
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
	
	GameState state = new GameState();
	
	Player player;
	Player player2;
	
	public RogueGame(String title, float width, float height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
				
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		//addState(new GameOverState());
		addState(new PlayingState());
		addState(new HostState());
		
		// preload resources here
		// images: ResourceManager.loadImage(IMG_RSC);
		// sounds: ResourceManager.loadSound(SND_RSC);
		ResourceManager.loadImage(GOLDGUI_IMG_RSC);
		ResourceManager.loadImage(GUI_MENULARGE_IMG_RSC);
		ResourceManager.loadImage(PLAYER_IDLE_IMG_RSC);
		ResourceManager.loadImage(ACTOR_GOBLIN0_IMG_RSC);
		ResourceManager.loadImage(ACTOR_GOBLIN1_IMG_RSC);

		player = new Player(GameState.WARRIOR);
		player2 = new Player(GameState.WARRIOR);
		state.player = player;
		state.player2 = player2;
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
	
}
