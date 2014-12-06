package rogueproject;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

public class Player extends IsoEntity {
	Animation[] walking = new Animation[8];
	Animation[] walkingFIRE = new Animation[8];
	Animation[] crouching = new Animation[8];
	Animation[] crouchingFIRE = new Animation[8];
	
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int DOWN = 3;
	static final int UpLEFT = 4;
	static final int UpRIGHT = 5;
	static final int DownLEFT = 6;
	static final int DownRIGHT = 7;
	static boolean crouch = false;
	static boolean shooting = false;
	
	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;


	int current;
	int shootingDirection;
	Vector wWorldSz;
	Vector lastWPosition;
	
	Sound ouch;
	
	public Player(Vector wWorldSize, Vector wPosition,int charClass) {
		super(wWorldSize, IsoWorldGame.tileSize);
		getTypeImage();
		setTypeAttributes();
		wWorldSz = wWorldSize;
		
		removeAnimation(walking[current]);
		wWorldSz = wWorldSize;
		this.setType(charClass);
		
		addAnimation(walking[current]);
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		}
	
	private void getTypeImage() {
		switch(this.getType()){
			case(GameState.WARRIOR):{
				walking[LEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[RIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[UP] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[DOWN] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
				
				walkingFIRE[LEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireLeft, 108, 133), 0,0,3,0, true, 70, true);
				walkingFIRE[RIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireRight,103, 127), 0,0,3,0, true, 70, true);
				walkingFIRE[UP] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUp, 103, 126), 0,0,3,0, true, 70, true);
				walkingFIRE[DOWN] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDown, 103, 122), 0,0,3,0, true, 70, true);
				walkingFIRE[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUpLeft, 122, 128), 0,0,3,0, true, 70, true);
				walkingFIRE[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireUpRight, 73, 136), 0,0,3,0, true, 70, true);
				walkingFIRE[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDownLeft, 82, 131), 0,0,3,0, true, 70, true);
				walkingFIRE[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireDownRight, 118, 121), 0,0,3,0, true, 70, true);
				
				crouching[LEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchLeft, 99, 100), 0,0,14,0, true, 70, true);
				crouching[RIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchRight,80, 109), 0,0,14,0, true, 70, true);
				crouching[UP] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUp, 92, 111), 0,0,14,0, true, 70, true);
				crouching[DOWN] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDown, 89, 96), 0,0,14,0, true, 70, true);
				crouching[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpLeft, 107, 97), 0,0,14,0, true, 70, true);
				crouching[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchUpRight, 71, 114), 0,0,14,0, true, 70, true);
				crouching[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownLeft, 72, 99), 0,0,14,0, true, 70, true);
				crouching[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.crouchDownRight, 92, 93), 0,0,14,0, true, 70, true);
			
				crouchingFIRE[LEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchLeft, 116, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[RIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchRight,89, 100), 0,0,3,0, true, 70, true);
				crouchingFIRE[UP] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUp, 99, 107), 0,0,3,0, true, 70, true);
				crouchingFIRE[DOWN] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDown, 92, 96), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUpLeft, 131, 98), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchUpRight, 59, 115), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDownLeft, 72, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireCrouchDownRight, 104, 90), 0,0,3,0, true, 70, true);
				
				current = RIGHT;

				//System.out.println("Minotaur zh:" + zHeight);
				
				//ouch = ResourceManager.getSound(IsoWorldGame.ouchSoundPath);
			}
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
			this.setEnergy(0);
			this.setGain(1);
			this.setExperience(0);
			break;
		default:
			break;
		}
	}
	public void attackActor(IsoEntity enemy){
		// damage done to enemy is player's attack minus enemies armor. if that is less than 0, do 0 damage instead.
		enemy.setHitPoints(enemy.getHitPoints() - Math.max(this.getAttack() - enemy.getArmor(), 0));
		if(enemy.getHitPoints() <= 0){
			this.setExperience(this.getExperience() + enemy.getExperience());
		}
		while(this.getExperience() > (this.getLevel() * 5)){ //defeating the enemy leads to a level up!
			System.out.println("levelup!");
			this.setExperience(this.getExperience() - (this.getLevel() * 5));
			this.setLevel(this.getLevel() + 1);
			this.setHitPoints(this.getHitPoints() + 5);
			this.setAttack(this.getAttack() + 1);
			this.setArmor(this.getArmor() + 0.5f);
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
		for (Iterator<IsoEntity> iie = GameState.stop.iterator(); iie.hasNext(); ) {
			other = iie.next();
			
			if (GameState.player.collides(other) != null) {
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

	public void start(float x, float direction) {
		isShooting();
		if(direction == 9){
			System.out.println("SHOOT");
			
			//System.out.println(GameState.current);
				
				shoot();
			//	GameState.fireball = GameState.launchFireball();
				
				
		
			
		}
		
			if(direction == 0){
				System.out.println("crouch");
				toggleCrouch();
			}
			else if(direction == 5){
				go(UP, x,UpRIGHT); 
				if(canMove()){
					go(RIGHT, x,UpRIGHT); 
					if(!canMove()){
						halt();
						ungo();
					}

				}
				else{
					halt();
					ungo();
				}
			}
			else if(direction == 6){
				go(UP, x,UpLEFT); 
				if(canMove()){
					go(LEFT, x,UpLEFT); 
					if(!canMove()){
						halt();
						ungo();
					}
				}
				else{
					halt();
					ungo();
				}
			}
			else if(direction == 7){
				go(DOWN, x,DownLEFT); 
				if(canMove()){
					go(LEFT, x,DownLEFT); 
					if(!canMove()){
						halt();
						ungo();
					}
				}
				else{
					halt();
					ungo();
				}
			}
			else if(direction == 8){
				go(DOWN, x,DownRIGHT);
				if(canMove()){
					go(RIGHT, x,DownRIGHT);
					if(!canMove()){
						halt();
						ungo();
					}
				}
				else{
					halt();
					ungo();
				}
			}
			else if (direction == 1){
				go(UP, x,UP);
				if(!canMove()){
					halt();
					ungo();
				}
			}
			else if (direction == 4){
				go(LEFT, x,LEFT);
				if(!canMove()){
					halt();
					ungo();
				}
			}
			else if (direction == 3){
				go(DOWN, x,DOWN);
				if(!canMove()){
					halt();
					ungo();
				}
			}
			else if (direction == 2){
				go(RIGHT, x,RIGHT);
				if(!canMove()){
					halt();
					ungo();
				}
			}
		}		
	}
	

