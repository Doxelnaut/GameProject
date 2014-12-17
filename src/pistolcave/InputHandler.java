package pistolcave;

import java.util.ArrayList;
import org.newdawn.slick.Input;
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
public class InputHandler {

	// Enumerated so rotations are multiples of direction integers
	private static final int N = 0, NE=1, E=2, SE=3, S=4, SW=5, W=6, NW=7;
	
	PistolCaveGame PC;
	
	private MoveCommand key_w = new MoveCommand(N), 
			key_a = new MoveCommand(W), 
			key_s = new MoveCommand(S), 
			key_d = new MoveCommand(E),
			keys_wa = new MoveCommand(NW),
			keys_wd = new MoveCommand(NE),
			keys_sa = new MoveCommand(SW),
			keys_sd = new MoveCommand(SE);
	private CrouchCommand keys_c = new CrouchCommand();
//			key_space = new MoveCommand(SHOOT) ,
	private ShootCommand mouse_left; 
			//mouse_right = new RangeAttackCommand()*/;
	
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public ArrayList<Command> handleInput(Input input,StateBasedGame game,float x){
		PC = (PistolCaveGame)game;
		ArrayList<Command> commands = new ArrayList<Command>();
		if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)){
			keys_wa.dist = x;
			commands.add(keys_wa);
		}
		else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)){
			keys_wd.dist = x;
			commands.add(keys_wd);
		}
		else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)){
			keys_sa.dist = x;
			commands.add(keys_sa);
		}
		else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)){
			keys_sd.dist = x;
			commands.add(keys_sd);
		}
		else if(input.isKeyDown(Input.KEY_W)){
			key_w.dist = x;
			commands.add(key_w);
		}
		else if(input.isKeyDown(Input.KEY_A)){
			key_a.dist = x;
			commands.add(key_a);
		}
		else if(input.isKeyDown(Input.KEY_S)){
			key_s.dist = x;
			commands.add(key_s);
		}
		else if(input.isKeyDown(Input.KEY_D)){
			key_d.dist = x;
			commands.add(key_d);
		}
//		else if(input.isKeyDown(Input.KEY_SPACE)){
//			commands.add(key_space);
//		}
		if(input.isKeyPressed(Input.KEY_C)){ // consumes a single key press event, used to toggle crouching
			commands.add(keys_c);
		}
		if(input.isKeyPressed(Input.KEY_F)){
			mouse_left = new ShootCommand(PC.currentPlayer.getPosition(),game);
			commands.add(mouse_left);
			
		}
//		dd	}
		/*else if(input.isButton3Pressed(Input.MOUSE_RIGHT_BUTTON)){
			commands.add(mouse_right);
		}*/
		
		// no buttons pressed, no command created
		return commands;
	}

	
	
}
