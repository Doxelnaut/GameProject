package pistolcave;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Corey Amoruso
 * @author Ryan Bergquist
 * @author Zacharias Shufflebarger
 * 
 *	This file is part of Pistol Cave.
 *
 *  Pistol Cave is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pistol Cave is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Pistol Cave.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Corey Amoruso, Ryan Bergquist, Zacharias Shufflebarger
 */
public class Player extends Actor {
	Animation[] walking = new Animation[8];
	Animation[] walkingFIRE = new Animation[8];
	Animation[] crouching = new Animation[8];
	Animation[] crouchingFIRE = new Animation[8];
	Animation currentAnimation;
	boolean crouch = false;
	boolean shooting = false;
	static boolean secondPlayer = false;
	double theta;
	
//	public static final int WAIT = -1, N = 0, E = 1, S = 2, W = 3, NW = 4, NE = 5, SE = 6, SW = 7, REST = 8;
	private static final int Up = 0, UpRight=1, Right=2, DownRight=3, Down=4, DownLeft=5, Left=6, UpLeft=7, CTRL=8;

	int current = Right; // current direction used for animation
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
		currentAnimation = walking[current];
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		theta = current * 45; // angle of directional unit vector from North.
		}
	
	public void getTypeImage() {
		switch(this.getType()){
			case(PistolCaveGame.WARRIOR):
				walking[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
				
				walkingFIRE[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireLeft, 108, 133), 0,0,3,0, true, 90, true);
				walkingFIRE[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireRight,103, 127), 0,0,3,0, true, 90, true);
				walkingFIRE[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireUp, 103, 126), 0,0,3,0, true, 90, true);
				walkingFIRE[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireDown, 103, 122), 0,0,3,0, true, 90, true);
				walkingFIRE[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireUpLeft, 122, 128), 0,0,3,0, true, 90, true);
				walkingFIRE[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireUpRight, 73, 136), 0,0,3,0, true, 90, true);
				walkingFIRE[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireDownLeft, 82, 131), 0,0,3,0, true, 90, true);
				walkingFIRE[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireDownRight, 118, 121), 0,0,3,0, true, 90, true);
				
				crouching[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchLeft, 99, 100), 0,0,14,0, true, 70, true);
				crouching[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchRight,80, 109), 0,0,14,0, true, 70, true);
				crouching[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchUp, 92, 111), 0,0,14,0, true, 70, true);
				crouching[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchDown, 89, 96), 0,0,14,0, true, 70, true);
				crouching[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchUpLeft, 107, 97), 0,0,14,0, true, 70, true);
				crouching[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchUpRight, 71, 114), 0,0,14,0, true, 70, true);
				crouching[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchDownLeft, 72, 99), 0,0,14,0, true, 70, true);
				crouching[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.crouchDownRight, 92, 93), 0,0,14,0, true, 70, true);
			
				crouchingFIRE[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchLeft, 116, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchRight,89, 100), 0,0,3,0, true, 70, true);
				crouchingFIRE[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchUp, 99, 107), 0,0,3,0, true, 70, true);
				crouchingFIRE[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchDown, 92, 96), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchUpLeft, 131, 98), 0,0,3,0, true, 70, true);
				crouchingFIRE[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchUpRight, 59, 115), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchDownLeft, 72, 108), 0,0,3,0, true, 70, true);
				crouchingFIRE[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.fireCrouchDownRight, 104, 90), 0,0,3,0, true, 70, true);
				
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
		this.shooting = false;
		this.lastWPosition = this.getPosition();
		theta = direction * 45; // angle of directional unit vector from North.
		Vector unitDirection = new Vector(0, -1);
		unitDirection = unitDirection.rotate(theta);
		this.setPosition(this.getPosition().add(unitDirection.scale(3)));
		this.getWalkingAnimation(direction);
		
//		if(!canMove()){
//			ungo();
//		}
				
		// TODO: basic movement handled. Need to check for collision first, though.
		// TODO: choose and create animation based on direction.
	}

	@Override
	/**
	 * toggle crouch
	 */
	public void crouch(){
		removeAnimation(currentAnimation);
		if(crouch){
			crouch = false;
			addAnimation(walking[current]);
			currentAnimation = walking[current];
			
			
		}
		else{
			crouch = true;
			addAnimation(crouching[current]);
			currentAnimation = crouching[current];
		}
	}
	
	public void shoot(Vector direction, StateBasedGame game){
		//TODO: shoot bullets
		this.shooting = true;
		this.shoot();
		PistolCaveGame RG = (PistolCaveGame) game;
		RG.bullets.add(new Bullet(PistolCaveGame.WORLD_SIZE, direction, RG.theta,1));
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
			addAnimation(crouching[current]);
			currentAnimation = crouching[current];


			if (current != direction){
				removeAnimation(crouching[current]);
				current = direction;
				addAnimation(crouching[current]);
				currentAnimation = crouching[current];
			}
			if (crouching[current].isStopped()){
				crouching[current].start();
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
	
	public void go(int dir, float n,int image) {
		float temp = n;
		
			if(crouch == true){
				temp /= 2;
				if (current != image) {
					removeAnimation(crouching[current]);
					current = image;
					addAnimation(crouching[current]);
					currentAnimation = crouching[current];
				}
				if (crouching[current].isStopped()) crouching[current].start();
				

			}
			else{
			
				if (current != image) {
					removeAnimation(walking[current]);
					current = image;
					addAnimation(walking[current]);
					currentAnimation = walking[current];
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
	
	public static boolean canMove(){
		IsoEntity other;
		for (Iterator<IsoEntity> iie = PistolCaveGame.stop.iterator(); iie.hasNext(); ) {
			other = iie.next();
			if(secondPlayer){
				
				if (PistolCaveGame.player2.collides(other) != null) return false;
			}
			else{
				
				if (PistolCaveGame.player.collides(other) != null) return false;
			}
			
		}
		return true;
	
	}

	public void shoot() {
		if(crouch == true){
			
				if(shooting){
					removeAnimation(currentAnimation);
					addAnimation(crouchingFIRE[current]);
					currentAnimation = crouchingFIRE[current];
					crouchingFIRE[current].setCurrentFrame(0);
					crouchingFIRE[current].start();
					crouchingFIRE[current].setLooping(false);
					shooting = true;
					shootingDirection = current;
				}
		}
		else{
				if(shooting) {
					removeAnimation(currentAnimation);
					addAnimation(walkingFIRE[current]);
					currentAnimation = walkingFIRE[current];
					walkingFIRE[current].setCurrentFrame(0);
					walkingFIRE[current].start();
					walkingFIRE[current].setLooping(false);
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
	
	public void updateDirection(double theta){
		
		//get player direction based on mouse angle to player
		if(-70 < theta && theta < -5){
			this.current = Right;
		}
		else if(-6 < theta && theta < 20){
			this.current = DownRight;
		}
		else if(19 < theta && theta <50){
			this.current = Down;
		}
		else if(49 < theta && theta < 115){
			this.current = DownLeft;
		}
		else if(-69 > theta && theta > -107){
			this.current = UpRight;
		}
		else if(-106 > theta && theta > -150){
			this.current = Up;
		}
		else if(-149 > theta && theta > -176){
			this.current = UpLeft;
		}
		else{
			this.current = Left;
		}
		
		removeAnimation(currentAnimation);

		if(shooting){
			if(crouch){
				addAnimation(crouchingFIRE[current]);
				crouchingFIRE[current].setCurrentFrame(0);
				crouchingFIRE[current].stop();
				currentAnimation = crouchingFIRE[current];
			}
			else{
				addAnimation(walkingFIRE[current]);
				walkingFIRE[current].setCurrentFrame(0);
				walkingFIRE[current].stop();
				currentAnimation = walkingFIRE[current];

			}
		}
		else{
			if(crouch){
				addAnimation(crouching[current]);
				currentAnimation = crouching[current];

			}
			else{
				addAnimation(walking[current]);
				currentAnimation = walking[current];

			}
		}
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

/*	public boolean isShooting() {
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
	
	*/
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
	

