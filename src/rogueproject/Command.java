package rogueproject;

import java.io.Serializable;

public class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Generic constructor.
	 */
	public Command(){}
	
	/**
	 * Generic method to be overridden by child classes.
	 * @param actor
	 */
	public void execute(Actor actor){}

	
	
}
