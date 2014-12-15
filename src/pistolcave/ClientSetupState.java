package pistolcave;
import jig.ResourceManager;
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
/*
 * Screen that is displayed when user clicks join game. Gets the server address from the user as well as an alias.
 */
public class ClientSetupState extends BasicGameState {

	Button	TitleButton; 
	Button hostAddressButton;
	Button connectButton;
	PistolCaveGame PC;

	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		PC = (PistolCaveGame) game;
		
		//create buttons
		TitleButton = new Button("Enter Host Address",
				(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.1f), 50);
		hostAddressButton = new Button("Host Address:",
				(PC.ScreenWidth * 0.3f), (PC.ScreenHeight * 0.35f), 25);
		connectButton = new Button("Connect",
				(PC.ScreenWidth * 0.7f), (PC.ScreenHeight * 0.9f), Button.MENU_LARGE, 25);
	}
		
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//draw setup screen
		g.drawImage(ResourceManager
			.getImage(PistolCaveGame.clientSetup_background), 0,0);		
		
		TitleButton.render(g);
		hostAddressButton.render(g);
		connectButton.render(g);
				
	}

	
	public void update(GameContainer container, StateBasedGame game, int g)
			throws SlickException {
		PistolCaveGame PC = (PistolCaveGame) game;
		Input input = container.getInput();
		
		//detect click of the connect button
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= connectButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= connectButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= connectButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= connectButton.getCoarseGrainedMinY()){

			PC.enterState(PistolCaveGame.PLAYINGSTATE);
		}
	}

	public int getID() {
		return PistolCaveGame.ClientSetupState;
	}

}
