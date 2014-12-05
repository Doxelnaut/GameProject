package rogueproject;

public class MoveCommand extends Command {
	
	private int direction;
	
	public MoveCommand(int direction){
		this.direction = direction;
		
	}
	
	public void execute(Minotaur minotaur, float x){
		minotaur.start(direction, x);
		
		
		
	
	}
}