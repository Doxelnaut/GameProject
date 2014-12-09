package rogueproject;

public class CrouchCommand extends Command{

	@Override
	public void execute(Actor actor){
		actor.crouch();
	}
	
}
