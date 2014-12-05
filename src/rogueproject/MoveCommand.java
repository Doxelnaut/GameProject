package rogueproject;

public class MoveCommand extends Command {
	
	private int direction;
	
	public MoveCommand(int direction){
		this.direction = direction;
	}
	
	@Override
	public void execute(Minotaur minotaur, float x){
		System.out.println(minotaur + " " + x + " " + direction);
		if(direction == InputHandler.getShoot()){
			GameState.fireball = GameState.minotaur.launchFireball();
		}
		if(direction == 5){
			minotaur.go(Minotaur.UP, x,Minotaur.UpRIGHT); 
			if(Minotaur.canMove()){
				minotaur.go(Minotaur.RIGHT, x,Minotaur.UpRIGHT); 
				if(!Minotaur.canMove()){
					minotaur.halt();
					minotaur.ungo();
				}

			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if(direction == 6){
			minotaur.go(Minotaur.UP, x,Minotaur.UpLEFT); 
			if(Minotaur.canMove()){
				minotaur.go(Minotaur.LEFT, x,Minotaur.UpLEFT); 
				if(!Minotaur.canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if(direction == 7){
			minotaur.go(Minotaur.DOWN, x,Minotaur.DownLEFT); 
			if(Minotaur.canMove()){
				minotaur.go(Minotaur.LEFT, x,Minotaur.DownLEFT); 
				if(!Minotaur.canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if(direction == 8){
			minotaur.go(Minotaur.DOWN, x,Minotaur.DownRIGHT);
			if(Minotaur.canMove()){
				minotaur.go(Minotaur.RIGHT, x,Minotaur.DownRIGHT);
				if(!Minotaur.canMove()){
					minotaur.halt();
					minotaur.ungo();
				}
			}
			else{
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (direction == 1){
			minotaur.go(Minotaur.UP, x,Minotaur.UP);
			if(!Minotaur.canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (direction == 4){
			minotaur.go(Minotaur.LEFT, x,Minotaur.LEFT);
			if(!Minotaur.canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (direction == 3){
			minotaur.go(Minotaur.DOWN, x,Minotaur.DOWN);
			if(!Minotaur.canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		else if (direction == 2){
			minotaur.go(Minotaur.RIGHT, x,Minotaur.RIGHT);
			if(!Minotaur.canMove()){
				minotaur.halt();
				minotaur.ungo();
			}
		}
		
	}
	
}