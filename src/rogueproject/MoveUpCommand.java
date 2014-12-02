package rogueproject;

public class MoveUpCommand extends Command {
	
	@Override
	public void execute(Actor actor){
		actor.moveUp();
	}
	
}
