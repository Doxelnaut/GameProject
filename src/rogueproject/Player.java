package rogueproject;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

public class Player extends Actor {
	Animation[] walking = new Animation[8];
	Animation[] walkingFIRE = new Animation[8];
	Animation[] crouching = new Animation[8];
	Animation[] crouchingFIRE = new Animation[8];
	
//	static final int LEFT = 0;
//	static final int RIGHT = 1;
//	static final int UP = 2;
//	static final int DOWN = 3;
//	static final int UpLEFT = 4;
//	static final int UpRIGHT = 5;
//	static final int DownLEFT = 6;
//	static final int DownRIGHT = 7;
	static boolean crouch = false;
	static boolean shooting = false;
	
//	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	private static final int Up = 0, UpRight=1, Right=2, DownRight=3, Down=4, DownLeft=5, Left=6, UpLeft=7, CTRL=8;

	int current; // current direction used for animation
	int shootingDirection;
	Vector wWorldSz;
	Vector lastWPosition;
	
	Sound ouch;
	
	public Player(Vector wWorldSize, Vector wPosition,int charClass) {
		super(wWorldSize);
		this.setType(charClass);
		getTypeImage();
		setTypeAttributes();
		wWorldSz = wWorldSize;
		removeAnimation(walking[current]);
		addAnimation(walking[current]);
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		}
	
	public void getTypeImage() {
		switch(this.getType()){
			case(RogueGame.WARRIOR):
				walking[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
				
				walkingFIRE[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireLeft, 108, 133), 0,0,3,0, true, 70, true);
				walkingFIRE[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireRight,103, 127), 0,0,3,0, true, 70, true);
				walkingFIRE[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUp, 103, 126), 0,0,3,0, true, 70, true);
				walkingFIRE[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDown, 103, 122), 0,0,3,0, true, 70, true);
				walkingFIRE[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUpLeft, 122, 128), 0,0,3,0, true, 70, true);
				walkingFIRE[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUpRight, 73, 136), 0,0,3,0, true, 70, true);
				walkingFIRE[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDownLeft, 82, 131), 0,0,3,0, true, 70, true);
				walkingFIRE[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDownRight, 118, 121), 0,0,3,0, true, 70, true);
				
				crouching[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchLeft, 99, 100), 0,0,14,0, true, 70, true);
				crouching[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchRight,80, 109), 0,0,14,0, true, 70, true);
				crouching[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUp, 92, 111), 0,0,14,0, true, 70, true);
				crouching[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDown, 89, 96), 0,0,14,0, true, 70, true);
				crouching[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpLeft, 107, 97), 0,0,14,0, true, 70, true);
				crouching[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpRight, 71, 114), 0,0,14,0, true, 70, true);
				crouching[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownLeft, 72, 99), 0,0,14,0, true, 70, true);
				crouching[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownRight, 92, 93), 0,0,14,0, true, 70, true);
			
				crouchingFIRE[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchLeft, 116, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchRight,89, 100), 0,0,3,0, true, 70, true);
				crouchingFIRE[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUp, 99, 107), 0,0,3,0, true, 70, true);
				crouchingFIRE[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDown, 92, 96), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUpLeft, 131, 98), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUpRight, 59, 115), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDownLeft, 72, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDownRight, 104, 90), 0,0,3,0, true, 70, true);
				
				current = Right;

				//System.out.println("Minotaur zh:" + zHeight);
				
				//ouch = ResourceManager.getSound(IsoWorldGame.ouchSoundPath);
			}
		}
	
	public void setTypeAttributes(){
		switch(this.getType()){
		case 0:
			this.setLevel(1);
			this.setMaxHitPoints(10);
			this.setHitPoints(10);
			this.setAttack(2);
			this.setArmor(0);
			this.setExp(0);
			break;
		default:
			break;
		}
	}
	
//	public void attackActor(Actor enemy){
//		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
//		enemy.setHitPoints(enemy.getHitPoints() - Math.max(this.getAttack() - enemy.getArmor(), 0));
//		if(enemy.getHitPoints() <= 0){
//			this.setExp(this.getExp() + enemy.getExp());
//		}
//		while(this.getExp() > (this.getLevel() * 5)){ //defeating the enemy leads to a level up!
//			System.out.println("levelup!");
//			this.setExp(this.getExp() - (this.getLevel() * 5));
//			this.setLevel(this.getLevel() + 1);
//			this.setHitPoints(this.getHitPoints() + 5);
//			this.setAttack(this.getAttack() + 1);
//			this.setArmor(this.getArmor() + 0.5f);
//		}
//	}

	/**
	 * Reciever method for MoveUpCommand. 
	 */
	@Override
	public void move(int direction) {
		//TODO: move character. Need to change collision detection to use tiles again, 
		// or implement a scan line to lower the number of entities that are checked
		// for collision. Perhaps the scan line can just use the camera view coords?
		// I would much rather re-implement tiles to save on speed. I'm sure part of 
		// the slow down came from checking for collisions too much.
		
		// Set movement in world coordinates using a unit vector that points in any one
		// of the cardinal or diagonal directions. 
		int theta = direction * 45; // angle of directional unit vector from North.
		Vector unitDirection = new Vector(0, -1);
		unitDirection = unitDirection.rotate(theta);
		this.setPosition(this.getPosition().add(unitDirection.scale(5)));
		this.getWalkingAnimation(direction);
				
		// TODO: basic movement handled. Need to check for collision first, though.
		// TODO: choose and create animation based on direction.
	}

	@Override
	/**
	 * toggle crouch
	 */
	public void crouch(){
		crouch = crouch == true? false : true;
	}
	
	public void shoot(Vector direction){
		//TODO: shoot bullets
	}
	
	/**
	 * starts walking animation when either crouched or standing
	 * @param direction the direction of movement in world coordinates
	 */
	public void getWalkingAnimation(int direction){ //TODO doesn't handle crouching, creates multiple animations
		if(crouch){
//			if (walking[current].isStopped()){
				removeAnimation(walking[current]);
//			}
			if (current != direction){
				removeAnimation(crouching[current]);
				current = direction;
				addAnimation(crouching[current]);
			}
			if (crouching[current].isStopped()){
				crouching[current].start();
			}
		}else{
//			if (crouching[current].isStopped()){
				removeAnimation(crouching[current]);
//			}
			if (current != direction) {
				removeAnimation(walking[current]);
				current = direction;
				addAnimation(walking[current]);
			} 
			if(walking[current].isStopped()){
				walking[current].start();
			}
		}
	}
	
	public void sayOuch() {
		if (ouch.playing()) return;
		ouch.play();
	}
	
	public void go(int dir, float n,int image) {
		float temp = n;
		
			if(crouch == true){
				temp /= 2;
				if (current != image) {
					removeAnimation(crouching[current]);
					current = image;
					addAnimation(crouching[current]);
				}
				if (crouching[current].isStopped()) crouching[current].start();
				

			}
			else{
			
				if (current != image) {
					removeAnimation(walking[current]);
					current = image;
					addAnimation(walking[current]);
				}
				if (walking[current].isStopped()) walking[current].start();
			
			}
		
		
		
	
		float x, y;
		if (dir < 2) {x = temp; y = 0;} 
		else {x = 0; y=temp;}
		if (dir % 2 == 0) {x = -x; y = -y;}
		
		lastWPosition = getPosition();
		setPosition(lastWPosition.add(new Vector(x,y)));
	}
	
	/**
	 * Undo the minotaur's last move, e.g., if he hit something.
	 */
	public void ungo() {
		setPosition(lastWPosition);
	}
	
	public void halt() {
		if(crouch == false){
			walking[current].stop();
			
		}
		else{
			crouching[current].stop();
		}
	}
	
	public Fireball launchFireball() {
		Fireball f = new Fireball(wWorldSz, wPosition, current);
		return f;
	}
	
	public static boolean canMove(){
		IsoEntity other;
		for (Iterator<IsoEntity> iie = RogueGame.stop.iterator(); iie.hasNext(); ) {
			other = iie.next();
			
			if (RogueGame.player.collides(other) != null) {
				return false;
			}
			
		}
		return true;
	
	}

	public void shoot() {
		if(crouch == true){
			
				if(shooting == false){
					removeAnimation(crouching[current]);
					addAnimation(crouchingFIRE[current]);
					crouchingFIRE[current].setCurrentFrame(0);
					crouchingFIRE[current].stopAt(3);
					crouchingFIRE[current].start();
					shooting = true;
					shootingDirection = current;
				}
				
				
				//addAnimation(crouching[current]);

		}
		else{
				if(shooting == false) {
					removeAnimation(walking[current]);
					addAnimation(walkingFIRE[current]);
					walkingFIRE[current].setCurrentFrame(0);
					walkingFIRE[current].stopAt(3);
					walkingFIRE[current].start();
					shooting = true;
					shootingDirection = current;

				}
				

		}
	}
/*
	public void changeImage() {
		if (walkingFIRE[current].isStopped()) {
			removeAnimation(walkingFIRE[current]);
			addAnimation(walking[current]);
			shooting = false;
			}
		if (crouchingFIRE[current].isStopped()) {
			removeAnimation(crouchingFIRE[current]);
			addAnimation(crouching[current]);
			shooting = false;
		}
	}

	public boolean isShooting() {
		return shooting;
	}
	*/
	public boolean getCrouch(){
		return crouch;
	}
	
	public void toggleCrouch(){
		
		if(crouch == true){
			crouch = false;
			removeAnimation(crouching[current]);
			addAnimation(walking[current]);
		}
		else {
			crouch = true;
			removeAnimation(walking[current]);
			addAnimation(crouching[current]);
		}
	}

	public boolean isShooting() {
		if(crouch){
			if(shooting){
				if(crouchingFIRE[shootingDirection].isStopped()) {
					removeAnimation(crouchingFIRE[shootingDirection]);
					addAnimation(crouching[current]);
					shooting = false;
					return false;
				}
			}
			
		}
		else{
			if(shooting){
				if(walkingFIRE[shootingDirection].isStopped()) {
					removeAnimation(walkingFIRE[shootingDirection]);
					addAnimation(walking[current]);
					shooting = false;
					return false;
				}
			}
		}
		return true;
	}
}
//	public void start(float x, float direction) {
//		isShooting();
//		if(direction == 9){
//			System.out.println("SHOOT");
//			
//			//System.out.println(GameState.current);
//				
//				shoot();
//			//	GameState.fireball = GameState.launchFireball();
//				
//				
//		
//			
//		}
//		
//			if(direction == 0){
//				System.out.println("crouch");
//				toggleCrouch();
//			}
//			else if(direction == 5){
//				go(UP, x,UpRIGHT); 
//				if(canMove()){
//					go(RIGHT, x,UpRIGHT); 
//					if(!canMove()){
//						halt();
//						ungo();
//					}
//
//				}
//				else{
//					halt();
//					ungo();
//				}
//			}
//			else if(direction == 6){
//				go(UP, x,UpLEFT); 
//				if(canMove()){
//					go(LEFT, x,UpLEFT); 
//					if(!canMove()){
//						halt();
//						ungo();
//					}
//				}
//				else{
//					halt();
//					ungo();
//				}
//			}
//			else if(direction == 7){
//				go(DOWN, x,DownLEFT); 
//				if(canMove()){
//					go(LEFT, x,DownLEFT); 
//					if(!canMove()){
//						halt();
//						ungo();
//					}
//				}
//				else{
//					halt();
//					ungo();
//				}
//			}
//			else if(direction == 8){
//				go(DOWN, x,DownRIGHT);
//				if(canMove()){
//					go(RIGHT, x,DownRIGHT);
//					if(!canMove()){
//						halt();
//						ungo();
//					}
//				}
//				else{
//					halt();
//					ungo();
//				}
//			}
//			else if (direction == 1){
//				go(UP, x,UP);
//				if(!canMove()){
//					halt();
//					ungo();
//				}
//			}
//			else if (direction == 4){
//				go(LEFT, x,LEFT);
//				if(!canMove()){
//					halt();
//					ungo();
//				}
//			}
//			else if (direction == 3){
//				go(DOWN, x,DOWN);
//				if(!canMove()){
//					halt();
//					ungo();
//				}
//			}
//			else if (direction == 2){
//				go(RIGHT, x,RIGHT);
//				if(!canMove()){
//					halt();
//					ungo();
//				}
//			}
//		}		
//	}
	

