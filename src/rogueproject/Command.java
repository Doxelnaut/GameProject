package rogueproject;

public class Command {

	/**
	 * Generic constructor.
	 */
	public Command(){}
	
	/**
	 * Generic method to be overridden by child classes.
	 * @param minotaur 
	 * @param x 
	 */
	public void execute(Minotaur minotaur, float x){}
	
}
