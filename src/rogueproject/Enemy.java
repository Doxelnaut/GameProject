package rogueproject;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

public class Enemy extends Actor {
	Animation[] walking = new Animation[8];
	Animation[] Attack = new Animation[8];
	Animation currentAnimation;
	boolean crouch = false;
	static boolean shooting = false;
	
//	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	private static final int Up = 0, UpRight=1, Right=2, DownRight=3, Down=4, DownLeft=5, Left=6, UpLeft=7, CTRL=8;

	int current; // current direction used for animation
	int shootingDirection;
	Vector wWorldSz;
	Vector lastWPosition;
	
	Sound ouch;
	
	public Enemy(Vector wWorldSize, Vector wPosition,int charClass) {
		super(wWorldSize);
		this.setType(charClass);
		getTypeImage();
		setTypeAttributes();
		wWorldSz = wWorldSize;
		removeAnimation(walking[current]);
		addAnimation(walking[current]);
		currentAnimation = walking[current];
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		}
	
	public void getTypeImage() {
		switch(this.getType()){
			case(RogueGame.Enemy1):
				walking[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
				
				Attack[Left] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchLeft, 99, 100), 0,0,14,0, true, 70, true);
				Attack[Right] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchRight,80, 109), 0,0,14,0, true, 70, true);
				Attack[Up] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUp, 92, 111), 0,0,14,0, true, 70, true);
				Attack[Down] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDown, 89, 96), 0,0,14,0, true, 70, true);
				Attack[UpLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpLeft, 107, 97), 0,0,14,0, true, 70, true);
				Attack[UpRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpRight, 71, 114), 0,0,14,0, true, 70, true);
				Attack[DownLeft] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownLeft, 72, 99), 0,0,14,0, true, 70, true);
				Attack[DownRight] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownRight, 92, 93), 0,0,14,0, true, 70, true);
			
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
	
	public void attackActor(Actor enemy){
		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
		enemy.setHitPoints(enemy.getHitPoints() - Math.max(this.getAttack() - enemy.getArmor(), 0));
		if(enemy.getHitPoints() <= 0){
			this.setExp(this.getExp() + enemy.getExp());
		}
		while(this.getExp() > (this.getLevel() * 5)){ //defeating the enemy leads to a level up!
			System.out.println("levelup!");
			this.setExp(this.getExp() - (this.getLevel() * 5));
			this.setLevel(this.getLevel() + 1);
			this.setHitPoints(this.getHitPoints() + 5);
			this.setAttack(this.getAttack() + 1);
			this.setArmor(this.getArmor() + 0.5f);
		}
	}

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
		if(crouch){
			crouch = false;
			removeAnimation(Attack[current]);
			addAnimation(walking[current]);
			currentAnimation = walking[current];
			
			
		}
		else{
			crouch = true;
			removeAnimation(walking[current]);
			addAnimation(Attack[current]);
			currentAnimation = Attack[current];
		}
	}
	
	public void shoot(Vector direction){
		//TODO: shoot bullets
	}
	
	/**
	 * starts walking animation when either crouched or standing
	 * @param direction the direction of movement in world coordinates
	 */
	public void getWalkingAnimation(int direction){ //TODO doesn't handle crouching, creates multiple animations
		
		removeAnimation(currentAnimation);

		if(crouch){
			
			
//			if (walking[current].isStopped()){
//				removeAnimation(walking[current]);
//			}
			addAnimation(Attack[current]);
			currentAnimation = Attack[current];


			if (current != direction){
				removeAnimation(Attack[current]);
				current = direction;
				addAnimation(Attack[current]);
				currentAnimation = Attack[current];
			}
			if (Attack[current].isStopped()){
				Attack[current].start();
			}
		}else{
			
			addAnimation(walking[current]);
			currentAnimation = walking[current];


//			if (crouching[current].isStopped()){
//			removeAnimation(crouching[current]);
//			}
			if (current != direction) {
				removeAnimation(walking[current]);
				current = direction;
				addAnimation(walking[current]);
				currentAnimation = walking[current];

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
			Attack[current].stop();
		}
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

}