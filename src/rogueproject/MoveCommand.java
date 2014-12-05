package rogueproject;

public class MoveCommand extends Command {
	
	private int direction;
	
	public MoveCommand(int direction){
		this.direction = direction;
		
	}
	
	public void execute(Player player, float x){
		player.start(direction, x);
		
		
		
	
	}
}