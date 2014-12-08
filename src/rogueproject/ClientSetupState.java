package rogueproject;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/*
 * Screen that is displayed when user clicks join game. Gets the server address from the user as well as an alias.
 */
public class ClientSetupState extends BasicGameState {

	Button	TitleButton; 
	Button hostAddressButton;
	Button connectButton;
	RogueGame RG;

	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		RG = (RogueGame) game;
		
		//create buttons
		TitleButton = new Button("Enter Host Address",
				(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.1f), 50);
		hostAddressButton = new Button("Host Address:",
				(RG.ScreenWidth * 0.3f), (RG.ScreenHeight * 0.35f), 25);
		connectButton = new Button("Connect",
				(RG.ScreenWidth * 0.7f), (RG.ScreenHeight * 0.9f), Button.MENU_LARGE, 25);
	}
		
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//draw setup screen
		g.drawImage(ResourceManager
			.getImage(RogueGame.clientSetup_background), 0,0);		
		
		TitleButton.render(g);
		hostAddressButton.render(g);
		connectButton.render(g);
				
	}

	
	public void update(GameContainer container, StateBasedGame game, int g)
			throws SlickException {
		RogueGame RG = (RogueGame) game;
		Input input = container.getInput();
		
		//detect click of the connect button
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
				&& input.getAbsoluteMouseX() <= connectButton.getCoarseGrainedMaxX()
				&& input.getAbsoluteMouseX() >= connectButton.getCoarseGrainedMinX()
				&& input.getAbsoluteMouseY() <= connectButton.getCoarseGrainedMaxY()
				&& input.getAbsoluteMouseY() >= connectButton.getCoarseGrainedMinY()){

			RG.enterState(RogueGame.PLAYINGSTATE);
		}
	}

	public int getID() {
		return RogueGame.ClientSetupState;
	}

}
