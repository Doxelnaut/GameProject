package rogueproject;

import java.util.ArrayList;

import jig.Vector;

import org.newdawn.slick.Input;

public class InputHandler {

	private static final int N=1, E=2, S=3, W=4, NE=5, NW=6, SW=7, SE=8, SHOOT = 9;
	
	private Command key_w = new MoveCommand(N), 
			key_a = new MoveCommand(W), 
			key_s = new MoveCommand(S), 
			key_d = new MoveCommand(E),
			keys_wa = new MoveCommand(NW),
			keys_wd = new MoveCommand(NE),
			keys_sa = new MoveCommand(SW),
			keys_sd = new MoveCommand(SE),
			keys_c = new MoveCommand(0),
			key_space = new MoveCommand(SHOOT) ,
			mouse_left/*, 
			mouse_right = new RangeAttackCommand()*/;
	
	/**
	 * 
	 */
	public ArrayList<Command> handleInput(Input input){
		ArrayList<Command> commands = new ArrayList<Command>();
		if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)){
			commands.add(keys_wa);
		}
		else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)){
			commands.add(keys_wd);
		}
		else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)){
			commands.add(keys_sa);
		}
		else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)){
			commands.add(keys_sd);
		}
		else if(input.isKeyDown(Input.KEY_W)){
			commands.add(key_w);
		}
		else if(input.isKeyDown(Input.KEY_A)){
			commands.add(key_a);
		}
		else if(input.isKeyDown(Input.KEY_S)){
			commands.add(key_s);
		}
		else if(input.isKeyDown(Input.KEY_D)){
			commands.add(key_d);
		}
		else if(input.isKeyDown(Input.KEY_SPACE)){
			commands.add(key_space);
		}
		else if(input.isKeyDown(Input.KEY_C)){
			commands.add(keys_c);
		}
		if(input.isButton1Pressed(Input.MOUSE_LEFT_BUTTON)){
			mouse_left = new ShootCommand(new Vector(input.getMouseX(), input.getMouseY()));
			commands.add(mouse_left);
		}
		/*else if(input.isButton3Pressed(Input.MOUSE_RIGHT_BUTTON)){
			commands.add(mouse_right);
		}*/
		
		// no buttons pressed, no command created
		return commands;
	}

	
	
}
