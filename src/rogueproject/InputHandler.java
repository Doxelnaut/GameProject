package rogueproject;

import java.util.ArrayList;

import org.newdawn.slick.Input;

public class InputHandler {

	private Command key_w = new MoveUpCommand(), 
			key_a = new MoveLeftCommand(), 
			key_s = new MoveDownCommand(), 
			key_d = new MoveRightCommand(), 
			mouse_left = new MeleeAttackCommand(), 
			mouse_right = new RangeAttackCommand();
	
	/**
	 * 
	 */
	public ArrayList<Command> handleInput(Input input){
		ArrayList<Command> commands = new ArrayList<Command>();
		if(input.isKeyDown(Input.KEY_W)){
			commands.add(key_w);
		}
		if(input.isKeyDown(Input.KEY_A)){
			commands.add(key_a);
		}
		if(input.isKeyDown(Input.KEY_S)){
			commands.add(key_s);
		}
		if(input.isKeyDown(Input.KEY_D)){
			commands.add(key_d);
		}
		if(input.isButton1Pressed(Input.MOUSE_LEFT_BUTTON)){
			commands.add(mouse_left);
		}
		if(input.isButton3Pressed(Input.MOUSE_RIGHT_BUTTON)){
			commands.add(mouse_right);
		}
		
		// no buttons pressed, no command created
		return commands;
	}
	
}
