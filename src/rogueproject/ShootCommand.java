package rogueproject;

import org.newdawn.slick.state.StateBasedGame;

import jig.Vector;


public class ShootCommand extends Command{

	private Vector direction;
	RogueGame RG;
	
	public ShootCommand(Vector direction,StateBasedGame game){
		this.direction = direction;
		RG = (RogueGame)game;
	}
	
//	public ShootCommand(){
//		
//	}
	
	@Override
	public void execute(Actor actor){
		actor.shoot(this.direction,RG);
	}
	
//	public void setDirection(Vector direction){
//		this.direction = direction;
//	}
//	
}
