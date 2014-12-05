package rogueproject;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

public class Player extends IsoEntity {
	Animation[] walking = new Animation[8];
	
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int DOWN = 3;
	static final int UpLEFT = 4;
	static final int UpRIGHT = 5;
	static final int DownLEFT = 6;
	static final int DownRIGHT = 7;

	private int classtype;


	int current;
	Vector wWorldSz;
	Vector lastWPosition;
	
	Sound ouch;
	
	public Player(Vector wWorldSize, Vector wPosition,int charClass) {
		super(wWorldSize, IsoWorldGame.tileSize);
		wWorldSz = wWorldSize;
		
		classtype = charClass;
		
		removeAnimation(walking[current]);
		wWorldSz = wWorldSize;
		this.setType(charClass);
		
		getTypeImage();
		setTypeAttributes();
		addAnimation(walking[current]);
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		}
	
	private void getTypeImage() {
		switch(this.getType()){
			case(GameState.WARRIOR):{
				walking[LEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[RIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[UP] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[DOWN] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
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
		if (current != image) {
			removeAnimation(walking[current]);
			current = image;
			addAnimation(walking[current]);
		}
		if (walking[current].isStopped()) walking[current].start();
		
	
		float x, y;
		if (dir < 2) {x = n; y = 0;} 
		else {x = 0; y=n;}
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
		walking[current].stop();
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
	
	int getCharClass(){
		return classtype;
	}
	
}
