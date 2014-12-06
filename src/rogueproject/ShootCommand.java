package rogueproject;

import jig.Vector;


public class ShootCommand extends Command{

	private Vector direction;
	
	public ShootCommand(Vector direction){
		this.direction = direction;
	}
	
//	public ShootCommand(){
//		
//	}
	
	@Override
	public void execute(Actor actor){
		actor.shoot(this.direction);
	}
	
//	public void setDirection(Vector direction){
//		this.direction = direction;
//	}
//	
}
