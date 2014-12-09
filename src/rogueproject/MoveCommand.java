package rogueproject;

public class MoveCommand extends Command {
	
	public int direction;
	
	public MoveCommand(int direction){
		this.direction = direction;
		
	}
	
	@Override
	public void execute(Actor actor){
		actor.move(this.direction);
	}
}