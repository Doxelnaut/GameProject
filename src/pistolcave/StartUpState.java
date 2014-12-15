package pistolcave;


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
public class StartUpState extends BasicGameState {
	PistolCaveGame PC;
	
	/* List of startup buttons */
	Button startButton;
	Button exitButton;
	Button title;
	Button hostButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(false);
		PC = (PistolCaveGame)game;
		
		
		// Create Title
		title = new Button("Pistol Cave",
				(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.2f), 48);
		
		// Create Start Menu Buttons
		startButton = new Button("Join Game", 
				(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.5f), Button.MENU_LARGE, 20);
		exitButton = new Button("Exit", 
				PC.ScreenWidth * 0.5f, (PC.ScreenHeight * 0.5f) + 100, Button.MENU_LARGE, 20);
		hostButton = new Button("Host Game", 
				PC.ScreenWidth * 0.5f, (PC.ScreenHeight * 0.5f) + 50, Button.MENU_LARGE, 20);
		

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		
		// Render Title
		title.render(g);
		// Render Buttons
		startButton.render(g);
		hostButton.render(g);
		exitButton.render(g);
		
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= startButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= startButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= startButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= startButton.getCoarseGrainedMinY()){

			PC.enterState(PistolCaveGame.ClientSetupState);	
			//System.out.print("mouse pressed\n");
		}
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= exitButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= exitButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= exitButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= exitButton.getCoarseGrainedMinY()){
			System.out.println("exit!!");
			container.exit();
		}
		
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= hostButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= hostButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= hostButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= hostButton.getCoarseGrainedMinY()){
			PC.enterState(PistolCaveGame.HOSTSTATE);
		}
	}
	
	@Override
	public int getID() {
		return PistolCaveGame.STARTUPSTATE;
	}
	
}
